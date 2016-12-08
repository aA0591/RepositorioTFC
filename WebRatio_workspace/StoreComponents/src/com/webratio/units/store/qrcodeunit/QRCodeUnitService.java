package com.webratio.units.store.qrcodeunit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.webratio.commons.lang.ClassHelper;
import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXCachedContentUnitService;
import com.webratio.rtx.RTXConstants;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.RTXOperationUnitService;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.rtx.core.DescriptorHelper;
import com.webratio.rtx.db.AbstractDBService;

/**
 * The runtime service for QR Code units.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class QRCodeUnitService extends AbstractDBService implements RTXCachedContentUnitService, RTXOperationUnitService {

    /** The QRCode image size in pixels */
    private final int defaultImageSize;

    /** The QRCodes colors */
    private final Color foregroundColor;
    private final Color backgroundColor;

    /**
     * Constructs a new service.
     * 
     * @param id
     *            the unique identifier of the service.
     * @param mgr
     *            the shared runtime manager.
     * @param descr
     *            the XML descriptor.
     * @throws RTXException
     *             if an error occurs building the service.
     */
    public QRCodeUnitService(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr);
        this.defaultImageSize = NumberUtils.toInt(DescriptorHelper.getChildValue(descr, "Size", false, this), 64);
        this.backgroundColor = getColor(DescriptorHelper.getChildValue(descr, "BackgroundColor", false, this), Color.white);
        this.foregroundColor = getColor(DescriptorHelper.getChildValue(descr, "ForegroundColor", false, this), Color.black);
    }

    private static Color getColor(String code, Color defaultColor) {
        try {
            code = StringUtils.removeStart(code, "#");
            return new Color(Integer.parseInt(code, 16));
        } catch (Exception e) {
            return defaultColor;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#computeParameterValue(java.lang.String, java.util.Map, java.util.Map)
     */
    public Object computeParameterValue(String outputParamName, Map pageContext, Map sessionContext) throws RTXException {
        Object unitBean = getUnitBean(pageContext, sessionContext);
        if (unitBean == null) {
            return null;
        }
        return BeanHelper.getBeanProperty(unitBean, outputParamName, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#execute(java.util.Map, java.util.Map)
     */
    public Object execute(Map localContext, Map sessionContext) throws RTXException {
        return getUnitBean(localContext, sessionContext);
    }

    /**
     * Gets (or recomputes) the unit bean.
     * 
     * @param localContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = request).
     * @param sessionContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = session).
     * @return the unit bean.
     * @throws RTXException
     *             if an error occurred computing the bean.
     */
    protected Object getUnitBean(Map localContext, Map sessionContext) throws RTXException {
        Object unitBean = localContext.get('_' + getId());
        if (unitBean == null || Boolean.TRUE.equals(localContext.get(RTXConstants.IN_OPERATION_KEY))) {
            unitBean = createUnitBean(localContext, sessionContext);
        } else {
            if (BeanHelper.getBeanProperty(unitBean, "qrCode", this) == null) {
                unitBean = createUnitBean(localContext, sessionContext);
            }
        }
        localContext.put('_' + getId(), unitBean);
        return unitBean;
    }

    /**
     * Creates the unit bean.
     * 
     * @param localContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = r).
     * @param sessionContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = session).
     * @return the unit bean.
     * @throws RTXException
     *             if an error occurred computing the bean.
     */
    public Object createUnitBean(Map localContext, Map sessionContext) throws RTXException {
        ExtendedOperationUnitBean bean = new ExtendedOperationUnitBean();
        String text = BeanHelper.asString(localContext.get(getId() + ".text"));
        if (StringUtils.isBlank(text)) {
            bean.setResultCode(RTXConstants.SUCCESS_CODE);
            return bean;
        }
        try {
            String imageName = BeanHelper.asString(localContext.get(getId() + ".imageName"));
            int imageSize = NumberUtils.toInt(BeanHelper.asString(localContext.get(getId() + ".size")), defaultImageSize);
            bean.put("qrCode", createQRCodeImage(imageName, text, imageSize, localContext));
            bean.put("text", text);
            bean.put("size", new Integer(imageSize));
            bean.setResultCode(RTXConstants.SUCCESS_CODE);
        } catch (Throwable e) {
            logError("Unable to execute the QRCode unit service", e);
            if (Boolean.TRUE.equals(localContext.get(RTXConstants.IN_OPERATION_KEY))) {
                return new ExtendedOperationUnitBean(false);
            } else { // content unit
                throw new RTXException(e);
            }
        }
        return bean;
    }

    private RTXBLOBData createQRCodeImage(String imageName, String text, int imageSize, Map localContext) throws RTXException {
        OutputStream out = null;
        try {
            QRCode qrCode = new QRCode();
            qrCode.setECLevel(ErrorCorrectionLevel.L);
            Encoder.encode(text, qrCode.getECLevel(), qrCode);
            
            /* generates the QRCode buffered image */
            int magnify = imageSize / qrCode.getMatrixWidth();
            byte[][] matrix = qrCode.getMatrix().getArray();
            int size = qrCode.getMatrixWidth() * magnify;
            int delta = (imageSize - size) / 2;

            /* creates the buffered image */
            BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor(backgroundColor);
            g.fillRect(0, 0, imageSize, imageSize);
            g.setColor(foregroundColor);
            int mSize = qrCode.getMatrixWidth();
            for (int y = 0; y < mSize; y++) {
                for (int x = 0; x < mSize; x++) {
                    if (matrix[y][x] == 1) {
                        g.fillRect(x * magnify + delta, y * magnify + delta, magnify, magnify);
                    }
                }
            }

            /* create the image file */
            if (StringUtils.isBlank(imageName)) {
                imageName = ClassHelper.toValidClassName(getName()) + ".png";
            }
            File scaledImageFile = BLOBHelper.createVersionedFile(new File(getManager().getUploadDirectory() + "/"
                    + RTXConstants.VIRTUAL_UPLOAD_DIR), imageName);
            out = new FileOutputStream(scaledImageFile);
            BLOBHelper.registerVirtualFile(localContext, scaledImageFile);
            ImageIO.write(image, StringUtils.substringAfterLast(imageName, "."), out);
            return BLOBHelper.getRTXBLOBData(getManager().getBLOBService().getRelativePath(scaledImageFile), getManager());
        } catch (Exception e) {
            throw new RTXException("Unable to compute QRCode image for '" + text + "'", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXService#dispose()
     */
    public void dispose() {

    }
}
