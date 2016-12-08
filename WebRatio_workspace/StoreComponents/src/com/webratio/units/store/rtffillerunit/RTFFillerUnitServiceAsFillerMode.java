package com.webratio.units.store.rtffillerunit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.rtx.core.DescriptorHelper;
import com.webratio.units.store.rtffillerunit.AbstractRTFUnitService.ParseResult.Token;

/**
 * This service fill a given RTF template replacing the given place holder names with the place holder values in according a replacing
 * map.
 * 
 * Used mode = filler.
 * 
 * Input: RTF template (pre-computed or from-coupling) the place holder name-to-value conversion map.
 * 
 * Output: The new filled RTF document.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class RTFFillerUnitServiceAsFillerMode extends AbstractRTFUnitService {

    /** The place holders */
    private final Map placeHolders = new HashMap();// key - new value

    public RTFFillerUnitServiceAsFillerMode(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr, "filler");
        preComputePlaceHolders(descr);
    }

    /**
     * Obtains the place holder names with default values defined at modeling time: obtained from sub units or parsing the file set
     * into unit property. The parsing of file has been done by the logic.template.
     * 
     * @param descr
     *            the descriptor of the service.
     * 
     * @return the never null map containing the name-value conversion.
     * @throws RTXException
     *             if a error occurs reading the given descriptor.
     */
    private void preComputePlaceHolders(Element descr) throws RTXException {
        for (Iterator iterator = DescriptorHelper.getChildren(descr, "PlaceHolder", false, this).iterator(); iterator.hasNext();) {
            Element elem = (Element) iterator.next();
            String key = elem.attributeValue("name", "");
            String value = elem.getText();
            placeHolders.put(key, value);
        }
    }

    private Map computeReplacingMap(final Map operationContext, final Map replacingMap) throws RTXException {

        final boolean hasPreComputePlaceHolders = (!placeHolders.isEmpty());

        /*
         * fill place holder values from coupling
         */
        if (hasPreComputePlaceHolders) {
            for (Iterator i = placeHolders.keySet().iterator(); i.hasNext();) {
                String placeHolderKey = (String) i.next();
                String defaultValue = StringUtils.defaultString((String) placeHolders.get(placeHolderKey));
                String currentValue = BeanHelper.asString(operationContext.get(getId() + '.' + placeHolderKey));
                String value = StringUtils.defaultIfEmpty(currentValue, defaultValue);
                replacingMap.put(placeHolderKey, value);
            }
        }

        /*
         * fill place holder values and dynamic names from coupling
         */
        String[] placeHolderNames = BeanHelper.asStringArray(operationContext.get(getId() + ".placeHolderNames"));
        int placeHolderNamesSize = placeHolderNames.length;
        String[] placeHolderValues = BeanHelper.asStringArray(operationContext.get(getId() + ".placeHolderValues"));
        int placeHolderValuesSize = placeHolderValues.length;
        if (placeHolderNamesSize != placeHolderValuesSize) {
            throw new RTXException("Invalid runtime Place Holders size: " + placeHolderValuesSize + " instead of "
                    + placeHolderNamesSize);
        }
        for (int i = 0; i < placeHolderNamesSize; i++) {
            String placeHolderKey = StringUtils.defaultString(placeHolderNames[i]);
            String value = StringUtils.defaultString(placeHolderValues[i]);
            replacingMap.put(placeHolderKey, value);
        }

        if (placeHolderNamesSize > 0 && hasPreComputePlaceHolders) {
            if (getLog().isWarnEnabled()) {
                getLog().warn("Place Holder names are defined at both modeling time and runtime");
            }
        }

        return replacingMap;
    }

    private final String fillRTF(final String inputDocument, final Map replacingMap, final List tokens) {
        String newDocument = inputDocument;
        for (Iterator i = tokens.iterator(); i.hasNext();) {
            final Token token = (Token) i.next();
            final String placeHolderKey = token.getKey();

            /*
             * remove token.subPart from token.offset
             */
            final int index = newDocument.indexOf(token.subPart, token.offset);
            if ((index < 0) || (index > (token.offset + token.length))) {
                throw new RuntimeException("Invalid index=" + index);
            }
            final String s0 = StringUtils.substring(newDocument, 0, index);
            final String s1 = StringUtils.substring(newDocument, index + token.subPart.length());

            /*
             * place new value
             */
            String newValue = "";
            if (placeHolderKey != null) {
                if (index != token.offset) {
                    throw new RuntimeException("Invalid index: " + index + " instead of " + token.offset);
                }
                // compute new value
                newValue = computeNewValue(replacingMap, placeHolderKey);
            }
            newDocument = s0 + newValue + s1;

        }

        return newDocument;
    }

    private String computeNewValue(Map replacingMap, String placeHolderKey) {
        String value = (String) replacingMap.get(placeHolderKey);
        try {
            value = '{' + "{\\*\\" + placeHolderKey + "}" + RTFValueConverter.process(value) + '}';
        } catch (DocumentException e) {
            if (getLog().isWarnEnabled() || getLog().isErrorEnabled()) {
                String msg = "The place holder value '" + value + "' may be not well formed";
                if (getLog().isWarnEnabled()) {
                    logWarning(msg);
                }
                if (getLog().isErrorEnabled()) {
                    logError(msg, e);
                }
            }
        } catch (RTXException e) {
            if (getLog().isErrorEnabled()) {
                logError(e.getLocalizedMessage(), e);
            }
        }
        return StringUtils.defaultString(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.rtffillerunit.AbstractRTFUnitService#doExecute (com.webratio.rtx.beans.ExtendedOperationUnitBean,
     * java.util.Map, java.util.Map)
     */
    final void doExecute(ExtendedOperationUnitBean bean, Map operationContext, Map sessionContext) throws RTXException {
        /*
         * compute new content replacing place holders with given fragments
         */

        final String inputDocument = getRTFTemplate(operationContext);

        /*
         * fill maps of associations
         * 
         * 1) from placeHolderKey to the new value to replace
         * 
         * 2) from placeHolderKey to all old values to replace with the new one
         */
        final Map replacingMap = new HashMap();
        computeReplacingMap(operationContext, replacingMap);

        final ParseResult parseResult = parse(inputDocument);
        final List tokens = parseResult.tokens;

        /*
         * create new document
         */
        final String newDocument = fillRTF(inputDocument, replacingMap, tokens);
        if (StringUtils.isEmpty(newDocument)) {
            throw new RTXException("Invalid empty output document");
        }

        /*
         * write to file the new rtf
         */
        String relPath = writeTempFile(newDocument, operationContext);

        RTXBLOBData rtfDocumentBlobData = BLOBHelper.getRTXBLOBData(relPath, getManager());
        bean.put("rtfDocument", rtfDocumentBlobData);
        // see output.template
    }
}
