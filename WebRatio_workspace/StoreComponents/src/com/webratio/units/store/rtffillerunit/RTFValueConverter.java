package com.webratio.units.store.rtffillerunit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.xml.XMLUtils;

/**
 * This utility class provides to convert a given value (expressed in a subset of XHTML) into a valid (supported) RTF formatted text.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
final class RTFValueConverter {

    private static final String[] CELL_BORDERS = new String[] {

    "\\clbrdrt ",// TOP

            "\\clbrdrl ",// LEFT

            "\\clbrdrb ",// BOTTOM

            "\\clbrdrr "// RIGHT

    };

    private static final String LN = "\n";

    private static final Set COLUMN_TAGS;

    static {
        Set columnTags = new HashSet();
        columnTags.add("th");
        columnTags.add("td");
        COLUMN_TAGS = Collections.unmodifiableSet(columnTags);
    }

    private RTFValueConverter() {

    }

    static String process(String input) throws DocumentException, RTXException {
        input = StringUtils.defaultString(input);
        if (StringUtils.isBlank(input)) {
            return input;
        }

        input = StringEscapeUtils.unescapeHtml(input);
        input = StringUtils.replace(input, "\\", "\\\\");
        input = StringUtils.replace(input, "{", "\\{");
        input = StringUtils.replace(input, "}", "\\}");

        Map converterMap = new HashMap();
        char[] cc = input.toCharArray();
        for (int i = 0; i < cc.length; i++) {
            char c = cc[i];
            int v = CharacterHelper.codePointAt(cc, i);
            if (v >= 0 && v <= 127) {
                continue;
            }
            if (v > Short.MAX_VALUE || v < Short.MIN_VALUE) {
                throw new RTXException("Unsupported char code " + v);
            }
            short s = (short) v;
            converterMap.put(new Character(c), "{\\u" + s + "?}");
        }

        for (Iterator i = converterMap.keySet().iterator(); i.hasNext();) {
            Character ch = (Character) i.next();
            String sh = (String) converterMap.get(ch);
            input = StringUtils.replace(input, ch.toString(), sh);
        }

        Document doc = XMLUtils.parse("<foo>" + StringUtils.trim(input) + "</foo>");
        return processElement(doc.getRootElement(), null);
    }

    private static String processElement(final Element element, Object data) {
        final StringBuffer sb = new StringBuffer();

        /*
         * compute tag
         */
        String tag = element.getQualifiedName();
        if ("em".equalsIgnoreCase(tag)) {
            tag = "i";
        } else if ("strong".equalsIgnoreCase(tag)) {
            tag = "b";
        } else if ("p".equalsIgnoreCase(tag)) {
            Element parent = element.getParent();
            if (parent != null && "foo".equalsIgnoreCase(parent.getQualifiedName()) && (parent.nodeCount() == 1)) {
                tag = "";// no paragraph tag
            }
        }

        /*
         * open
         */
        Object childData = null;
        if ("i".equalsIgnoreCase(tag) || "b".equalsIgnoreCase(tag)) {
            sb.append("{\\" + tag.toLowerCase() + " {");
        } else if ("u".equalsIgnoreCase(tag)) {
            sb.append("{\\ul {");
        } else if ("strike".equalsIgnoreCase(tag)) {
            sb.append("{\\strike {");
        } else if ("sub".equalsIgnoreCase(tag)) {
            sb.append("{\\sub {");
        } else if ("sup".equalsIgnoreCase(tag)) {
            sb.append("{\\super {");
        } else if ("p".equalsIgnoreCase(tag)) {
            sb.append("{\\par {");
        } else if ("table".equalsIgnoreCase(tag)) {
            sb.append("{\\par {");
            childData = new TableData(element);
        } else if ("tr".equalsIgnoreCase(tag)) {
            sb.append("\\trowd " + LN);
            final int columnsCount = getElementsByQualifiedName(element, COLUMN_TAGS).size();
            if (columnsCount > 0) {
                final TableData tableData = (TableData) data;
                final int w = (tableData.width / columnsCount);
                for (int c = 0; c < columnsCount; c++) {
                    if (tableData.border > 0) {
                        for (int i = 0; i < CELL_BORDERS.length; i++) {
                            sb.append(CELL_BORDERS[i] + "\\brdrw" + tableData.border + "\\brdrs ");
                        }
                    }
                    sb.append("\\cellx" + ((c + 1) * w) + LN);
                }
            }
            childData = data;// redirect
        } else if ("td".equalsIgnoreCase(tag)) {
            final TableData tableData = (TableData) data;
            if (tableData.cellMargin > 0) {
                sb.append("\\trgaph" + tableData.cellMargin + " ");
            }
            sb.append("\\pard\\intbl{");// open block
        } else if ("th".equalsIgnoreCase(tag)) {
            sb.append("\\pard\\intbl{" + "\\b {");// open foo-bold
        } else if ("ol".equalsIgnoreCase(tag)) {
            // ordered list
            childData = new OrderedListData(1);
            sb.append("{");
        } else if ("ul".equalsIgnoreCase(tag)) {
            // unordered list
            childData = new UnorderedListData();
            sb.append("{");
        } else if ("li".equalsIgnoreCase(tag)) {
            IListData listData = (IListData) data;
            sb.append(listData.getBeginToken());
        } else if ("tbody".equalsIgnoreCase(tag) || "thead".equalsIgnoreCase(tag)) {
            childData = data;// redirect
        }

        /*
         * children and contents
         */
        final List groups = new ArrayList();
        TokensGroup tokensGroup = null;
        for (Iterator iter = element.nodeIterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if (node instanceof Text) {
                final String text = node.getText();
                if (StringUtils.isEmpty(text)) {
                    continue;
                }
                if (tokensGroup == null) {
                    tokensGroup = new TokensGroup("");
                    groups.add(tokensGroup);
                }
                tokensGroup.add(text);
            } else if (node instanceof Element) {
                final String text = processElement((Element) node, childData);
                if (StringUtils.isEmpty(text)) {
                    continue;
                }
                tokensGroup = null;
                groups.add(new TokensGroup(text));
            }
        }
        final int size = groups.size();
        for (int i = 0; i < size; i++) {
            String token = ((TokensGroup) groups.get(i)).getValue();
            if ((i != size) && (i != 0) && StringUtils.isBlank(token) && !StringUtils.isEmpty(token)) {
                token = " ";
            } else {
                if (i != size) {
                    token = StringUtils.stripStart(token, null);
                }
                if (i != 0) {
                    token = StringUtils.stripEnd(token, null);
                }
            }
            sb.append(token);
        }

        /*
         * close
         */
        if ("i".equalsIgnoreCase(tag) || "b".equalsIgnoreCase(tag)) {
            sb.append("}}");
        } else if ("u".equalsIgnoreCase(tag)) {
            sb.append("}\\ulnone }");
        } else if ("strike".equalsIgnoreCase(tag)) {
            sb.append("}\\strike0 }");
        } else if ("sub".equalsIgnoreCase(tag)) {
            sb.append("}\\nosupersub }");
        } else if ("sup".equalsIgnoreCase(tag)) {
            sb.append("}\\nosupersub }");
        } else if ("p".equalsIgnoreCase(tag)) {
            sb.append("}\\pard}");
        } else if ("table".equalsIgnoreCase(tag)) {
            sb.append("}\\pard}");
        } else if ("tr".equalsIgnoreCase(tag)) {
            sb.append("\\row ");
        } else if ("td".equalsIgnoreCase(tag)) {
            sb.append("}\\cell ");
        } else if ("th".equalsIgnoreCase(tag)) {
            sb.append("}");// close foo-bold
            sb.append("}\\cell ");// as TD
        } else if ("ol".equalsIgnoreCase(tag) || "ul".equalsIgnoreCase(tag)) {
            sb.append("}");
        } else if ("li".equalsIgnoreCase(tag) && (data instanceof IListData)) {
            IListData listData = (IListData) data;
            sb.append(listData.getEndToken());
            listData.increment();
        }

        return sb.toString();
    }

    private static List getElementsByQualifiedName(Element element, Set qualifiedNames) {
        List list = new ArrayList();
        if (qualifiedNames != null && !qualifiedNames.isEmpty()) {
            for (Iterator iter = element.nodeIterator(); iter.hasNext();) {
                Node node = (Node) iter.next();
                if (node instanceof Element) {
                    Element child = (Element) node;
                    final String childQualifiedName = child.getQualifiedName();
                    for (Iterator q = qualifiedNames.iterator(); q.hasNext();) {
                        String qualifiedName = (String) q.next();
                        if (childQualifiedName.equalsIgnoreCase(qualifiedName)) {
                            list.add(child);
                        }
                    }
                }
            }
        }
        return list;
    }

    private static String getAttributeValue(Element element, String attributeName) {
        return getAttributeValue(element, attributeName, true);
    }

    private static String getAttributeValue(Element element, String attributeName, boolean caseSensitive) {
        for (Iterator iter = element.attributeIterator(); iter.hasNext();) {
            final Attribute attribute = (Attribute) iter.next();
            final String qualifiedName = attribute.getQualifiedName();
            if (caseSensitive) {
                if (qualifiedName.equals(attributeName)) {
                    return attribute.getValue();
                }
            } else {
                if (qualifiedName.equalsIgnoreCase(attributeName)) {
                    return attribute.getValue();
                }
            }
        }
        if (!caseSensitive) {
            return "";
        }
        return getAttributeValue(element, attributeName, false);
    }

    private static interface IListData {

        public String getBeginToken();

        public String getEndToken();

        public void increment();
    }

    private static abstract class AbstractListData implements IListData {

        private final String beginToken;

        private final String endToken;

        public AbstractListData(String beginToken, String endToken) {
            this.beginToken = StringUtils.defaultString(beginToken);
            this.endToken = StringUtils.defaultString(endToken);
        }

        public String getBeginToken() {
            return beginToken;
        }

        public String getEndToken() {
            return endToken;
        }
    }

    private static final class OrderedListData extends AbstractListData {

        int index;

        public OrderedListData(int index) {
            super("{\\par {\\tab ", "}");
            this.index = index;
        }

        public void increment() {
            this.index++;
        }

        public String getBeginToken() {
            return super.getBeginToken() + index + ".  }";
        }

    }

    private static final class UnorderedListData extends AbstractListData {

        public UnorderedListData() {
            super("{\\par {\\tab\\bullet  }", "}");
        }

        public void increment() {
        }

    }

    private static final class TokensGroup {

        private final List tokens = new ArrayList();

        public TokensGroup(String token) {
            this.add(token);
        }

        public void add(String token) {
            tokens.add(StringUtils.defaultString(token));
        }

        public String getValue() {
            return StringUtils.join(tokens, "");
        }

    }

    private static final class TableData {

        final int width;

        final int border;

        final int cellMargin;

        public TableData(Element element) {

            /*
             * width
             */
            int tableWidth = -1;
            try {
                String widthStr = getAttributeValue(element, "width").trim();
                if (StringUtils.isBlank(widthStr)) {
                    // e.g. style="width: 600px;"
                    String styleStr = getAttributeValue(element, "style").trim().toLowerCase();
                    if (styleStr.startsWith("width:") && styleStr.length() > 6) {
                        styleStr = styleStr.substring(6).trim();
                        styleStr = StringUtils.removeEnd(styleStr, ";");
                        styleStr = StringUtils.removeEnd(styleStr, "px");
                        widthStr = styleStr;
                    }
                }
                tableWidth = Integer.parseInt(widthStr);
            } catch (Throwable e) {
                tableWidth = -1;
            }
            this.width = (tableWidth < 0 ? 10000 : tableWidth * 15);

            /*
             * border
             */
            int tableBorder = -1;
            try {
                tableBorder = Integer.parseInt(getAttributeValue(element, "border").trim());
            } catch (Throwable e) {
                tableBorder = -1;
            }
            this.border = Math.max(tableBorder, 0) * 18;

            /*
             * border
             */
            int cellpadding = 0;
            try {
                cellpadding = Integer.parseInt(getAttributeValue(element, "cellpadding").trim());
            } catch (Throwable e) {
            }
            int cellspacing = 0;
            try {
                cellspacing = Integer.parseInt(getAttributeValue(element, "cellspacing").trim());
            } catch (Throwable e) {
            }
            this.cellMargin = Math.max(cellpadding + cellspacing, 0) * 18;
        }

    }

    private static final class CharacterHelper {

        private static final char MIN_HIGH_SURROGATE = '\uD800';

        private static final char MAX_HIGH_SURROGATE = '\uDBFF';

        private static final char MIN_LOW_SURROGATE = '\uDC00';

        private static final char MAX_LOW_SURROGATE = '\uDFFF';

        private static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;

        static int codePointAt(char[] a, int index) {
            return codePointAtImpl(a, index, a.length);
        }

        private static int codePointAtImpl(char[] a, int index, int limit) {
            char c1 = a[index++];
            if (isHighSurrogate(c1)) {
                if (index < limit) {
                    char c2 = a[index];
                    if (isLowSurrogate(c2)) {
                        return toCodePoint(c1, c2);
                    }
                }
            }
            return c1;
        }

        private static boolean isHighSurrogate(char ch) {
            return ch >= MIN_HIGH_SURROGATE && ch <= MAX_HIGH_SURROGATE;
        }

        private static boolean isLowSurrogate(char ch) {
            return ch >= MIN_LOW_SURROGATE && ch <= MAX_LOW_SURROGATE;
        }

        private static int toCodePoint(char high, char low) {
            return ((high - MIN_HIGH_SURROGATE) << 10) + (low - MIN_LOW_SURROGATE) + MIN_SUPPLEMENTARY_CODE_POINT;
        }
    }

}
