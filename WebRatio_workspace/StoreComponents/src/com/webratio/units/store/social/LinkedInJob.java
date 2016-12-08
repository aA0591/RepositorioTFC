package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.dom4j.Element;

public class LinkedInJob {

    private String id;
    private Boolean active;
    private GregorianCalendar postingDate;
    private String companyId;
    private String companyName;
    private String position;
    private String positionLocation;
    private String skillsAndExperience;
    private String description;
    private String salary;
    private String siteJobUrl;

    public LinkedInJob(Element root) {
        id = root.valueOf("id");
        active = Boolean.valueOf(root.valueOf("active"));
        companyId = root.valueOf("company/id");
        companyName = root.valueOf("company/name");
        position = root.valueOf("position/title");
        positionLocation = root.valueOf("position/location/name");
        skillsAndExperience = root.valueOf("skills-and-experiences");
        description = root.valueOf("description");
        salary = root.valueOf("salary");
        siteJobUrl = root.valueOf("site-job-url");
        postingDate = new GregorianCalendar();
        if (root.element("posting-date") != null) {
            int month = new Integer(root.valueOf("posting-date/month")).intValue();
            int day = new Integer(root.valueOf("posting-date/day")).intValue();
            int year = new Integer(root.valueOf("posting-date/year")).intValue();
            postingDate.set(year, month, day);
        }

    }

    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public GregorianCalendar getPostingDate() {
        return postingDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPosition() {
        return position;
    }

    public String getPositionLocation() {
        return positionLocation;
    }

    public String getSkillsAndExperience() {
        return skillsAndExperience;
    }

    public String getDescription() {
        return description;
    }

    public String getSalary() {
        return salary;
    }

    public String getSiteJobUrl() {
        return siteJobUrl;
    }

}
