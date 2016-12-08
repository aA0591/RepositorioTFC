package com.webratio.units.store.rtffillerunit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXBLOBService;
import com.webratio.rtx.RTXConstants;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.RTXOperationUnitService;
import com.webratio.rtx.beans.ExtendedOperationUnitBean;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.rtx.core.AbstractService;
import com.webratio.rtx.core.DescriptorHelper;

/**
 * The abstract runtime service for a RTF Filler unit able to manage a given RTF template with other given info expressed in well
 * formed XHTML.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
abstract class AbstractRTFUnitService extends AbstractService implements RTXOperationUnitService {

    /** The RTF template encoding */
    protected static final String RTF_FILE_ENCONDING = "ASCII";

    /** The execution mode */
    private final String mode;

    /** The base name of temp file */
    private final String baseNameTempFile;

    /** The pre-computed RTF template */
    private final String preComputedTemplate;

    /**
     * Constructs a new RTF unit service.
     * 
     * @param id
     *            the identifier of the service.
     * @param rtx
     *            the runtime manager.
     * @param descr
     *            the descriptor of the service.
     * @param expectedMode
     *            the expected mode (used for check with Mode value)
     * @throws RTXException
     *             if a error occurs creating the service.
     */
    public AbstractRTFUnitService(String id, RTXManager mgr, Element descr, String expectedMode) throws RTXException {
        super(id, mgr, descr);
        this.mode = StringUtils.defaultString(DescriptorHelper.getChildValue(descr, "Mode", true, this));
        if (!this.mode.equals(expectedMode)) {
            throw new RTXException("Invalid mode '" + this.mode + "' instead of '" + expectedMode + "'");
        }
        this.baseNameTempFile = getId() + ".rtf";
        this.preComputedTemplate = readRTFFile(id, mgr, descr);
    }

    private final String readRTFFile(String id, RTXManager mgr, Element descr) throws RTXException {
        final String rtfFile = DescriptorHelper.getChildValue(descr, "RTFTemplate", false, this);
        if (StringUtils.isBlank(rtfFile)) {
            return null;
        }
        InputStream inStream = null;
        try {
            inStream = mgr.getResourceStream(rtfFile);
            return IOUtils.toString(inStream, RTF_FILE_ENCONDING);
        } catch (Exception e) {
            throw new RTXException("Unable to read RTF file " + rtfFile, e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }

    /**
     * Returns the content of the RTF template if received from coupling, otherwise returns the pre-computed template in case it's from
     * unit property.
     * 
     * @param operationContext
     *            the local context
     * @return the content of RTF template
     * @throws RTXException
     *             thrown is the template is not available.
     */
    final String getRTFTemplate(Map operationContext) throws RTXException {
        RTXBLOBData documentBlobData = BLOBHelper.getRTXBLOBData(operationContext.get(getId() + ".rtfTemplate"), getManager());
        if (documentBlobData == null) {
            /*
             * return pre-computed template
             */
            if (preComputedTemplate == null) {
                throw new RTXException("Missing template");
            }
            return preComputedTemplate;
        }

        /*
         * read blob received from coupling
         */
        String content = "";
        InputStream inStream = null;
        try {
            inStream = documentBlobData.openFileInputStream();
            content = IOUtils.toString(inStream, RTF_FILE_ENCONDING);
        } catch (Exception e) {
            throw new RTXException("Unable to read template " + documentBlobData, e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        if (StringUtils.isEmpty(content)) {
            throw new RTXException("Invalid empty template");
        }

        content = preprocessText(content);

        return content;
    }

    /**
     * Writes a given input document into temp file. Register it into operation context and returns its relative path.
     * 
     * @param content
     *            the content to write
     * @param operationContext
     *            the local context
     * @return the relative path of the created temp file containing the given content
     * @throws RTXException
     *             if an exception occurred
     */
    protected final String writeTempFile(String content, Map operationContext) throws RTXException {
        RTXBLOBService blobService = getManager().getBLOBService();
        File tempFile = blobService.createTempFile(baseNameTempFile);
        tempFile.deleteOnExit();
        try {
            FileUtils.writeStringToFile(tempFile, content, RTF_FILE_ENCONDING);
        } catch (IOException e) {
            throw new RTXException("Cannot write temp file " + tempFile, e);
        }
        BLOBHelper.registerVirtualFile(operationContext, tempFile);
        return blobService.getRelativePath(tempFile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.webml.rtx.RTXOperationUnitService#execute(java.util.Map, java.util.Map)
     */
    public Object execute(Map operationContext, Map sessionContext) throws RTXException {
        ExtendedOperationUnitBean bean = new ExtendedOperationUnitBean();
        try {
            doExecute(bean, operationContext, sessionContext);

            /*
             * done
             */
            bean.setResultCode(RTXConstants.SUCCESS_CODE);
        } catch (Exception e) {
            logError("An error occurred executing the RTF Filler unit service in '" + mode + "' mode", e);
            bean.setResultCode(RTXConstants.ERROR_CODE);
        }
        return bean;
    }

    /**
     * Execute and fill the given bean.
     * 
     * @param bean
     *            the bean to fill with the result.
     * @param operationContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = request).
     * @param sessionContext
     *            a set of name-to-object bindings, preloaded with values of parameters (having scope = session).
     * @throws RTXException
     *             if an error occurred executing the service.
     */
    abstract void doExecute(ExtendedOperationUnitBean bean, Map operationContext, Map sessionContext) throws RTXException;

    public void dispose() {
    }

    public static void main(String[] args) {
        // TODO:remove
        File folder = new File("C:/Users/FulvioCiapessoni/Snaps/RTF-TEMP/Concessioni/RTFTemplate");
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().startsWith("NEW_")) {
                continue;
            }
            System.out.println("******************************************************************");
            System.out.println("> File: " + file);
            System.out.println("******************************************************************");
            try {
                String inputDocument = FileUtils.readFileToString(file);
                String newInputDocument = preprocessText(inputDocument);
                {
                    File newFile = new File(file.getParentFile(), "NEW_" + file.getName());
                    if (newFile.exists()) {
                        newFile.delete();
                    }
                    if (!inputDocument.equals(newInputDocument)) {
                        FileUtils.writeStringToFile(newFile, newInputDocument);
                    }
                }
                inputDocument = newInputDocument;
                ParseResult result = parse(inputDocument);
                List tokens = new ArrayList(result.tokens);
                Collections.reverse(tokens);
                StringBuffer sb = new StringBuffer();
                for (Iterator it = tokens.iterator(); it.hasNext();) {
                    ParseResult.Token token = (ParseResult.Token) it.next();
                    String s = token.toString();
                    sb.append(s + "\n");
                    if (s.length() > 100) {
                        throw new RuntimeException("Lenght problem");
                    }
                }
                System.out.println(sb.toString());
            } catch (Throwable e) {
                System.out.println("Error");
                // new Exception("Error in " + file, e).printStackTrace();
            }
            System.out.println("******************************************************************\n\n");
        }
    }

    private static final java.util.regex.Pattern pattern = java.util.regex.Pattern
            .compile("\\$((?:\\\\(?!'|-|\\*|:|\\\\|_|\\{|\\||}|~|bullet|cell|chatn|chdate|chdpa|chdpl|chftn|chftnsep|chftnsepc|chpgn|chtime|column|emdash|emspace|endash|enspace|lbrN|ldblquote|line|lquote|ltrmark|nestcell|nestrow|page|par|qmspace|rdblquote|row|rquote|rtlmark|sect|sectnum|tab|zwbo|zwj|zwnbo)[A-Za-z0-9]+ ?)|[{}\\r\\n])*\\$");

    private static String preprocessText(String inputDocument) {
        return pattern.matcher(inputDocument).replaceAll("\\$\\$");
    }

    static ParseResult parse(String inputDocument) {
        return new ParseResult(inputDocument);
    }

    static final class ParseResult {

        final List placeHolderKeys;

        final String[] placeHolderKeysAsArray;

        final List tokens;

        private ParseResult(final String inputDocument) {
            if (inputDocument.indexOf("$$$$") >= 0) {
                throw new RuntimeException("Invalid document content: found $$$$ token.");
            }
            if (inputDocument.length() <= 0) {
                placeHolderKeys = new ArrayList();
                placeHolderKeysAsArray = new String[0];
                tokens = new ArrayList();
                return;
            }

            final List placeHolderKeysTemp = new ArrayList();
            final List tokensTemp = new ArrayList();

            final int len = inputDocument.length();
            List substrings = new ArrayList();
            final String separator = "$$";
            final int separatorLength = separator.length();
            int numberOfSubstrings = 0;
            int beg = 0;
            int end = 0;
            int cnt = 0;
            while (end < len) {
                end = inputDocument.indexOf(separator, beg);
                String str = null;
                int indexBegin = -1;
                if (end > -1) {
                    if (end > beg) {
                        numberOfSubstrings++;
                        str = inputDocument.substring(beg, end);
                        indexBegin = beg;
                        substrings.add(str);
                        cnt++;
                        beg = end + separatorLength;
                    } else {
                        beg = end + separatorLength;
                    }
                } else {
                    str = inputDocument.substring(beg);
                    indexBegin = beg;
                    substrings.add(str);
                    cnt++;
                    end = len;
                }
                if ((str == null) || (indexBegin < 0) || ((cnt % 2) != 0)) {
                    continue;
                }

                /*
                 * register "str" from "indexBegin!"
                 */
                final String placeHolderName = str;
                final Token tokenBegin = new Token("$$", "$$", indexBegin - 2);
                tokensTemp.add(tokenBegin);
                final int indexEnd = indexBegin + placeHolderName.length();
                tokensTemp.add(new Token("$$", "$$", indexEnd));

                /*
                 * compute place holder key
                 */
                List parts = new ArrayList();
                String[] ss = StringUtils.split(placeHolderName);
                for (int j = 0; j < ss.length; j++) {
                    String part = ss[j];
                    if (StringUtils.isBlank(part)) {
                        // skip empty parts
                        continue;
                    }
                    final String oldPart = part;
                    part = StringUtils.remove(part, '{');
                    part = StringUtils.remove(part, '}');
                    if (part.startsWith("\\")) {
                        // skip rtf code parts
                        continue;
                    }
                    /*
                     * replace \'hh codes e.g. StringUtils.replace(part, "\\'e0", "à");
                     * 
                     * \'hh A hexadecimal value, based on the specified character set (may be used to identify 8-bit values).
                     */
                    String newPart = "";
                    char[] partChars = part.toCharArray();
                    int partCharsLength = partChars.length;
                    for (int j2 = 0; j2 < partCharsLength;) {
                        char pc = partChars[j2++];
                        if ((pc == '\\') && (j2 < (partCharsLength + 2)) && (partChars[j2] == '\'')) {
                            // case of \'hh
                            j2++;
                            char c0 = partChars[j2++];
                            char c1 = partChars[j2++];
                            try {
                                int charCode = Integer.parseInt(("" + c0 + c1).toUpperCase(), 16);
                                newPart += ("" + ((char) charCode));
                            } catch (NumberFormatException e) {
                            }
                        } else {
                            newPart += pc;
                        }
                    }
                    part = StringUtils.substringBefore(newPart, "\\");

                    /*
                     * add removing end codes
                     */
                    final int index = inputDocument.indexOf(oldPart, indexBegin);
                    if (index < 0 || index > indexEnd) {
                        throw new RuntimeException("Invalid index [0.." + indexEnd + "] " + index);
                    }
                    tokensTemp.add(new Token(oldPart, part, index));
                    parts.add(part);
                }
                final String placeHolderKey = StringUtils.join(parts, "");

                tokenBegin.setKey(placeHolderKey);

                if (!placeHolderKeysTemp.contains(placeHolderKey)) {
                    placeHolderKeysTemp.add(placeHolderKey);
                }

            }

            placeHolderKeys = Collections.unmodifiableList(placeHolderKeysTemp);
            placeHolderKeysAsArray = (String[]) placeHolderKeysTemp.toArray(new String[0]);

            Collections.sort(tokensTemp, new Comparator() {

                public int compare(Object arg0, Object arg1) {
                    Token t0 = (Token) arg0;
                    Token t1 = (Token) arg1;
                    return t1.offset - t0.offset;
                }
            });

            tokens = Collections.unmodifiableList(tokensTemp);

        }

        static final class Token {

            final String fullPart;

            final String subPart;

            final int offset;

            final int length;

            private String key;

            private Token(String fullPart, String subPart, int offset) {
                this.fullPart = fullPart;
                this.subPart = subPart;
                this.offset = offset;
                this.length = subPart.length();
            }

            private void setKey(String key) {
                this.key = key;
            }

            public String getKey() {
                return key;
            }

            public String toString() {
                return Token.class.getName() + " @" + offset + " [" + key + "]" + " = " + fullPart + " : " + subPart;
            }
        }

    }
}