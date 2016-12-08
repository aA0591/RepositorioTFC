package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/participant.xsd
 * 
 * <pre>
 * <xsd:complexType name="ParticipantType">
 * 	<xsd:sequence>
 * 		<xsd:element name="id" type="xsd:long" minOccurs="0"/>
 * 		<xsd:element name="participantKey" type="xsd:string" minOccurs="0"/>
 * 		<xsd:element name="name" type="xsd:string"/>
 * 		<xsd:element name="userId" type="xsd:string" minOccurs="0"/>
 * 		<xsd:element name="preferences">
 * 	<xsd:complexType
 * <xsd:sequence>
 * <xsd:element name="option" maxOccurs="unbounded">
 * 	<xsd:simpleType>
 * 		<xsd:restriction base="xsd:integer">
 * 		<xsd:minInclusive value="0"/>
 * 		</xsd:restriction>
 * 	</xsd:simpleType>
 * </xsd:element>
 * </xsd:sequence>
 * </xsd:complexType>
 * </xsd:element>
 * </xsd:sequence>
 * </xsd:complexType>
 * </pre>
 * 
 */
@SuppressWarnings("rawtypes")
public class DoodleParticipant {

    private String id;
    private String participantKey;
    private String name;
    private String userId;
    private List preferences;

    public DoodleParticipant(String id, String participantKey, String name, String userId, List preferences) {
        this.id = id;
        this.participantKey = participantKey;
        this.name = name;
        this.userId = userId;
        this.preferences = preferences;
    }

    public DoodleParticipant(String name, List preferences) {
        this.name = name;
        this.preferences = preferences;
    }

    public String getId() {
        return id;
    }

    public String getParticipantKey() {
        return participantKey;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public List getPreferences() {
        if (preferences == null) {
            preferences = new ArrayList();
        }
        return preferences;
    }

}
