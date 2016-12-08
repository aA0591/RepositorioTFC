package com.webratio.units.store.social;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/user.xsd
 * 
 * <pre>
 * <xsd:complexType name="UserType">
 * 	<xsd:sequence>
 * 	<xsd:element name="userId" type="xsd:string"/>
 * 	<xsd:element name="name" minOccurs="0" type="xsd:string"/>
 *  	<xsd:element name="eMailAddress" minOccurs="0">...</xsd:element>
 * 	<xsd:element name="polls">
 * 	<xsd:complexType>
 * 		<xsd:sequence>
 * 			<xsd:element name="poll" minOccurs="0" maxOccurs="unbounded">
 * 				<xsd:complexType>
 * 					<xsd:sequence>
 * 						<xsd:element name="pollId" type="tns:PollIdType"/>
 * 						<xsd:element name="adminKey" minOccurs="0" type="tns:AdminKeyType"/>
 * 						<xsd:element name="latestChange" type="xsd:dateTime"/>
 * 						<xsd:element name="state" type="tns:StatesType"/>
 * 						<xsd:element name="title" type="xsd:string"/>
 * 						<xsd:element name="nrOfParticipants" type="xsd:integer"/>
 * 					</xsd:sequence>
 * 				</xsd:complexType>
 * 			</xsd:element>
 * 		</xsd:sequence>
 * 	</xsd:complexType>
 * 	</xsd:element>
 * 		<xsd:element name="myDoodleIcsFeedUri" minOccurs="0" maxOccurs="1" type="xsd:anyURI"/>
 * 		<xsd:element name="calendars">...</xsd:element>
 * 		<xsd:element name="features" type="tns:FeaturesType"/>
 * 	</xsd:sequence>
 * </xsd:complexType>
 */
@SuppressWarnings("rawtypes")
public class DoodleUser {

    private String name;
    private String email;
    private String userId;
    private List polls;

    public DoodleUser(String name, String email, String userId, List polls) {
        super();
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.polls = polls;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List getPolls() {
        return polls;
    }

    public DoodlePoll getById(String id) {
        Iterator it = polls.iterator();
        while (it.hasNext()) {
            DoodlePoll doodlePoll = (DoodlePoll) it.next();
            if (doodlePoll.getId().equals(id))
                return doodlePoll;
        }
        return null;
    }

}
