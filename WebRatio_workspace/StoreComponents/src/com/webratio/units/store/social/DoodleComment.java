package com.webratio.units.store.social;

import java.util.GregorianCalendar;

/**
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/comment.xsd
 * 
 * <pre>
 * <xsd:complexType name="CommentType">
 * 	<xsd:sequence>
 * 		<xsd:element name="id" type="xsd:long" minOccurs="0"/>
 * 		<xsd:element name="who" type="xsd:string"/>
 * 		<xsd:element name="userId" type="xsd:string" minOccurs="0"/>
 * 		<xsd:element name="when" type="xsd:dateTime" minOccurs="0"/>
 * 		<xsd:element name="what" type="xsd:string"/>
 * 	</xsd:sequence>
 * </xsd:complexType>
 * </pre>
 * 
 */
public class DoodleComment {

    private long id;
    private String who;
    private String userId;
    private GregorianCalendar when;
    private String what;

    public DoodleComment(String who, String what) {
        this.who = who;
        this.what = what;
    }

    public DoodleComment(long id, String who, String userId, GregorianCalendar when, String what) {
        this.id = id;
        this.who = who;
        this.userId = userId;
        this.when = when;
        this.what = what;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWho() {
        return who;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GregorianCalendar getWhen() {
        return when;
    }

    public void setWhen(GregorianCalendar when) {
        this.when = when;
    }

    public String getWhat() {
        return what;
    }

}
