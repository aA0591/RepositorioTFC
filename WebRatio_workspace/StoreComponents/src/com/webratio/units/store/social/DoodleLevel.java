package com.webratio.units.store.social;

/**
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * url: http://doodle.com/xsd1/poll.xsd
 * 
 * <pre>
 * <xsd:simpleType name="LevelsType">
 * 	<xsd:restriction base="xsd:integer">
 * 		<xsd:minInclusive value="2"/>
 * 		<xsd:maxInclusive value="3"/>
 * 	</xsd:restriction>
 * </xsd:simpleType>
 * </pre>
 * 
 */

public class DoodleLevel {

    private int level;

    public static final DoodleLevel TWO = new DoodleLevel(2);
    public static final DoodleLevel THREE = new DoodleLevel(3);

    private DoodleLevel(int level) {
        this.level = level;
    }

    public static DoodleLevel fromValue(Integer level) {
        if (level.equals(new Integer(2)))
            return TWO;
        else if (level.equals(new Integer(3)))
            return THREE;
        else
            return null;

    }

    public int getLevel() {
        return this.level;
    }

}
