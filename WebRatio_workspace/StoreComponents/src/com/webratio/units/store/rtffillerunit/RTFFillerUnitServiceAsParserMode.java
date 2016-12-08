package com.webratio.units.store.rtffillerunit;

import java.util.Map;

import org.dom4j.Element;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.blob.BLOBHelper;

/**
 * This service returns the place holder names found parsing a given RTF template. Returns a copy of the given RTF template too.
 * 
 * Used mode = parser.
 * 
 * Input: RTF template (pre-computed or from-coupling)
 * 
 * Output: used RTF template and place holder names
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class RTFFillerUnitServiceAsParserMode extends AbstractRTFUnitService {

    public RTFFillerUnitServiceAsParserMode(String id, RTXManager mgr, Element descr) throws RTXException {
        super(id, mgr, descr, "parser");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.rtffillerunit.AbstractRTFUnitService#doExecute (com.webratio.rtx.beans.ExtendedOperationUnitBean,
     * java.util.Map, java.util.Map)
     */
    void doExecute(ExtendedOperationUnitBean bean, Map operationContext, Map sessionContext) throws RTXException {
        /*
         * parse input document extracting the place holder names
         */

        final String inputDocument = getRTFTemplate(operationContext);

        final ParseResult parseResult = parse(inputDocument);

        bean.put("placeHolderNames", parseResult.placeHolderKeysAsArray);
        bean.put("placeHolderLabels", parseResult.placeHolderKeysAsArray);
        // see output.template

        /*
         * rewrite into temp file the rtf template
         */
        String relPath = writeTempFile(inputDocument, operationContext);
        RTXBLOBData rtfTemplateBlobData = BLOBHelper.getRTXBLOBData(relPath, getManager());
        bean.put("rtfTemplate", rtfTemplateBlobData);
        // see output.template
    }

}
