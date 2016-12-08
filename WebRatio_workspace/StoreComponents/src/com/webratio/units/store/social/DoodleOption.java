package com.webratio.units.store.social;

import java.util.GregorianCalendar;

/**
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/poll.xsd
 * 
 * <pre>
 * <xsd:complexType name="OptionsType">
 * 	<xsd:sequence>
 * 		<xsd:element name="option" maxOccurs="unbounded">
 * 			<xsd:complexType>
 * 				<xsd:simpleContent>
 * 					<xsd:extension base="xsd:string">
 * 						<xsd:attribute name="date" type="xsd:date"/>
 * 						<xsd:attribute name="dateTime" type="xsd:dateTime"/>
 * 						<xsd:attribute name="startDateTime" type="xsd:dateTime"/>
 * 						<xsd:attribute name="endDateTime" type="xsd:dateTime"/>
 * 						<xsd:attribute name="final" type="xsd:boolean"/>
 * 					</xsd:extension>
 * 				</xsd:simpleContent>
 * 			</xsd:complexType>
 * 		</xsd:element>
 * 	</xsd:sequence>
 * </xsd:complexType>
 * </pre>
 * 
 */

public class DoodleOption {

    private String value;
    private GregorianCalendar date;
    private GregorianCalendar dateTime;
    private GregorianCalendar startDateTime;
    private GregorianCalendar endDateTime;
    private boolean dateTimeV;
    private boolean finalV;

    public boolean isDateTimeV() {
        return dateTimeV;
    }

    /**
     * Constructor to type TEXT: Example: <option>Text</option>
     * 
     * @param value
     *            , is Text
     */
    public DoodleOption(String value) {
        this.value = value;
    }

    /**
     * Constructor to type DATE: Example: <option startDateTime="2011-06-10T08:15:59" endDateTime="2011-06-11T09:20:59"/>
     * 
     * @param startDateTime
     *            , Start date and time
     * @param endDateTime
     *            , End date and time
     */
    public DoodleOption(GregorianCalendar startDateTime, GregorianCalendar endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Constructor to type DATE: Case examples: 1.- <option dateTime="2023-05-20T10:22:59"/>; dateTimeV = true; 2.- <option
     * date="2023-05-20">Text</option>; dateTimeV = false; 3.- <option date="2023-05-20"/> dateTime = false;
     * 
     * 
     * @param dateTime
     *            , GregorianCalendar
     * @param dateTimeV
     *            , boolean to determine if date/time or date
     * @param value
     *            , Text only for case 2
     * 
     *            Note: Case 3 is for a date with empty text Bad Request: <option date="2023-05-20">Text</option>
     */

    public DoodleOption(GregorianCalendar dateTime, boolean dateTimeV, String value) {
        this.dateTimeV = dateTimeV;
        if (dateTimeV) {
            this.dateTime = dateTime;
        } else {
            this.date = dateTime;
            this.value = value;
        }
    }

    public String getValue() {
        return value;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public GregorianCalendar getDateTime() {
        return dateTime;
    }

    public GregorianCalendar getStartDateTime() {
        return startDateTime;
    }

    public GregorianCalendar getEndDateTime() {
        return endDateTime;
    }

    public boolean isFinal_() {
        return finalV;
    }

    public void setFinal_(boolean finalV) {
        this.finalV = finalV;
    }

}
