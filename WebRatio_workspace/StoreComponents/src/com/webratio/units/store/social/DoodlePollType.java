package com.webratio.units.store.social;

/**
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/poll.xsd
 * 
 * <pre>
 * <xsd:simpleType name="PollTypeType">
 * 	<xsd:restriction base="xsd:string">
 * 		<xsd:enumeration value="DATE"/>
 * 		<xsd:enumeration value="TEXT"/>
 * 	</xsd:restriction>
 * </xsd:simpleType>
 * </pre>
 * 
 */

public class DoodlePollType {

    private String value;

    public static final DoodlePollType DATE = new DoodlePollType("DATE");
    public static final DoodlePollType TEXT = new DoodlePollType("TEXT");
    public static final DoodlePollType DATETIME = new DoodlePollType("DATETIME");

    private DoodlePollType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static DoodlePollType fromValue(String value) {
        if (value != null) {
            if (value.equals("DATE"))
                return DATE;
            else if (value.equals("TEXT"))
                return TEXT;
            else if (value.equals("DATETIME"))
                return DATETIME;
            else
                return null;
        } else {
            return null;
        }
    }

}