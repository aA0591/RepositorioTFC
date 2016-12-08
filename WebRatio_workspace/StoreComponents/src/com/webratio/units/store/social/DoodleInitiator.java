package com.webratio.units.store.social;

/**
 * <p>
 * Java class for PollImp.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/poll.xsd
 * 
 * <pre>
 * <xsd:complexType name="InitiatorType">
 * 	<xsd:sequence>
 * 		<xsd:element name="name" type="xsd:string"/>
 * 		<xsd:element name="userId" type="xsd:string" minOccurs="0"/>
 * 		<xsd:element name="eMailAddress" type="xsd:string" minOccurs="0"/>
 * 	</xsd:sequence>
 * </xsd:complexType>
 * </pre>
 * 
 */
public class DoodleInitiator {

    private String name;
    private String userId;
    private String eMailAddress;

    public DoodleInitiator(String name) {
        this.name = name;
    }

    public DoodleInitiator(String name, String userId, String eMailAddress) {
        this.name = name;
        this.userId = userId;
        this.eMailAddress = eMailAddress;
    }

    public DoodleInitiator(String name, String eMailAddress) {
        this.name = name;
        this.eMailAddress = eMailAddress;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

}
