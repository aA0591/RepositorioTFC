package com.webratio.units.store.social;

/**
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/poll.xsd
 * 
 * <pre>
 * <xsd:simpleType name="StatesType">
 * 	<xsd:restriction base="xsd:string">
 * 		<xsd:enumeration value="OPEN"/>
 * 		<xsd:enumeration value="CLOSED"/>
 * 		<xsd:enumeration value="DELETED"/>
 * 	</xsd:restriction>
 * </xsd:simpleType>
 * </pre>
 * 
 */
public class DoodleState {

    private String value;

    public static final DoodleState OPEN = new DoodleState("OPEN");
    public static final DoodleState CLOSED = new DoodleState("CLOSED");
    public static final DoodleState DELETED = new DoodleState("DELETED");

    private DoodleState(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static DoodleState fromValue(String value) {
        if (value != null) {
            if (value.equals("OPEN"))
                return OPEN;
            else if (value.equals("CLOSED"))
                return CLOSED;
            else if (value.equals("DELETED"))
                return DELETED;
            else
                return null;
        } else {
            return null;
        }

    }

}