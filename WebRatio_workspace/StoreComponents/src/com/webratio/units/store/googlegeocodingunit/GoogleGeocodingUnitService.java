package com.webratio.units.store.googlegeocodingunit;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.dom4j.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.webratio.rtx.RTXContentUnitService;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.RTXOperationUnitService;
import com.webratio.rtx.core.AbstractService;
import com.webratio.rtx.core.BeanHelper;

/**
 * The runtime service for the Google Geocoding unit.
 */
public class GoogleGeocodingUnitService extends AbstractService implements RTXContentUnitService, RTXOperationUnitService {

    private static final String GEOCODER_REQUEST_PREFIX_FOR_XML = "http://maps.google.com/maps/api/geocode/xml";

    /**
     * Constructs a new Google Geocoding Unit service
     * 
     * @param id
     *            the id of the unit.
     * @param mgr
     *            the runtime manager.
     * @param descr
     *            the unit descriptor element.
     * @throws RTXException
     *             in case an error occurs initializing the service.
     */
    public GoogleGeocodingUnitService(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#computeParameterValue(java.lang.String, java.util.Map, java.util.Map)
     */
    public Object computeParameterValue(String outputParamName, Map pageContext, Map sessionContext) throws RTXException {
        GoogleGeocodingUnitBean bean = (GoogleGeocodingUnitBean) pageContext.get(getId());
        if (bean == null) {
            bean = (GoogleGeocodingUnitBean) execute(pageContext, sessionContext);
        }
        if (outputParamName.equalsIgnoreCase("coords")) {
            return bean.getMapCoordinates();
        }
        if (outputParamName.equalsIgnoreCase("firstAddr")) {
            return bean.getFirstAddr();
        }
        if (outputParamName.equalsIgnoreCase("long")) {
            return bean.getLongitude();
        }
        if (outputParamName.equalsIgnoreCase("lat")) {
            return bean.getLatitude();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#execute(java.util.Map, java.util.Map)
     */
    public Object execute(Map operationContext, Map sessionContext) throws RTXException {
        GoogleGeocodingUnitBean bean = new GoogleGeocodingUnitBean();
        bean.setFullAddress(BeanHelper.asString(operationContext.get(getId() + ".fulladdr")));
        bean.setAddress(BeanHelper.asString(operationContext.get(getId() + ".addr")));
        bean.setCity(BeanHelper.asString(operationContext.get(getId() + ".city")));
        bean.setStateOrProvince(BeanHelper.asString(operationContext.get(getId() + ".sop")));
        bean.setCountry(BeanHelper.asString(operationContext.get(getId() + ".country")));
        bean.setMapCoordinates(BeanHelper.asString(operationContext.get(getId() + ".coords")));
        bean.setResultCode(GoogleGeocodingUnitBean.OK);

        String fullAddress = bean.getFullAddress();
        if ((fullAddress == null) || (fullAddress.length() == 0)) {
            fullAddress = bean.getAddress() + ", " + bean.getCity() + ", " + bean.getStateOrProvince() + ", " + bean.getCountry();
            bean.setFullAddress(fullAddress);
        }

        if (bean.getMapCoordinates() == null) {

            HttpURLConnection conn = null;
            // prepare a URL to the geocoder
            try {
                URL url;

                url = new URL(GEOCODER_REQUEST_PREFIX_FOR_XML + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")
                        + "&sensor=false");

                // prepare an HTTP connection to the geocoder
                conn = (HttpURLConnection) url.openConnection();

                Document geocoderResultDocument = null;

                // open the connection and get results as InputSource
                conn.connect();
                InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

                // read result and parse into XML Document
                geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);

                // prepare XPath
                XPath xpath = XPathFactory.newInstance().newXPath();

                // extract the result
                NodeList resultNodeList = null;

                // obtain the formatted_address field for every result
                resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result/formatted_address", geocoderResultDocument,
                        XPathConstants.NODESET);
                if (resultNodeList.getLength() == 1) {
                    bean.setFirstValidAddress(resultNodeList.item(0).getFirstChild().getNodeValue());
                }

                // extract the coordinates of the first result
                resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/*", geocoderResultDocument,
                        XPathConstants.NODESET);
                float lat = Float.NaN;
                float lng = Float.NaN;
                for (int i = 0; i < resultNodeList.getLength(); ++i) {
                    Node node = resultNodeList.item(i);
                    if ("lat".equals(node.getNodeName())) {
                        lat = Float.parseFloat(node.getFirstChild().getNodeValue());
                        bean.setLatitude(Float.toString(lat));
                    }
                    if ("lng".equals(node.getNodeName())) {
                        lng = Float.parseFloat(node.getFirstChild().getNodeValue());
                        bean.setLongitude(Float.toString(lng));
                    }
                }
                if (!(Float.isNaN(lat) && Float.isNaN(lng))) {
                    bean.setMapCoordinates(Float.toString(lat) + ',' + Float.toString(lng));
                }
            } catch (Exception e) {
                logError("Unable to execute the Google Geocoding Unit service", e);
                bean.setResultCode(GoogleGeocodingUnitBean.KO);
                throw new RTXException(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        operationContext.put('_' + getId(), bean);
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.RTXContentUnitService#execute(java.util.Map, java.util.Map)
     */
    public void dispose() {
    }
}