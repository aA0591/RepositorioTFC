package com.webratio.units.store.social;

import org.dom4j.Element;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractUser;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class LinkedInUser extends AbstractUser {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String headline;
    private String location;
    private String publicProfileUrl;

    protected LinkedInUser(Element element, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = element.valueOf("id");
            firstName = element.valueOf("first-name");
            lastName = element.valueOf("last-name");
            userName = firstName + " " + lastName;
            email = element.valueOf("email-address");
            headline = element.valueOf("headline");
            location = element.valueOf("location/name");
            publicProfileUrl = element.valueOf("site-standard-profile-request/url");
            profileImage = BLOBHelper.getRTXBLOBData(element.valueOf("picture-url"), socialNetwork.getManager());
        } catch (Exception e) {
            throw new RTXException("Unable to read user informations", e);
        }
    }

    public String getLocation() {
        return location;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHeadline() {
        return headline;
    }
}
