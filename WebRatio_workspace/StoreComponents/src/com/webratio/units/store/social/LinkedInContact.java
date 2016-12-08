package com.webratio.units.store.social;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractContact;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class LinkedInContact extends AbstractContact {

    protected LinkedInContact(Element contactElement, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = contactElement.valueOf("*[local-name()='id']");
            name = contactElement.valueOf("*[local-name()='first-name']") + " "
                    + contactElement.valueOf("*[local-name()='last-name']");
            location = contactElement.valueOf("*[local-name()='location']");
            String profileImageUrl = contactElement.valueOf("*[local-name()='picture-url']");
            if (!StringUtils.isBlank(profileImageUrl)) {
                profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());
            }
        } catch (Exception e) {
            throw new RTXException("Unable to read contact informations", e);
        }
    }

}
