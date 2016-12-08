package com.webratio.units.store.social;

import org.dom4j.Element;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class LinkedInCompany {

    private String name;
    private String id;
    private String website;
    private RTXBLOBData logo;
    private String description;

    private String type;

    public LinkedInCompany(Element element, ISocialNetworkService socialNetwork) throws RTXException {

        name = element.valueOf("name");
        id = element.valueOf("id");
        website = element.valueOf("website-url");
        String logoUrl = element.valueOf("logo-url");
        logo = BLOBHelper.getRTXBLOBData(logoUrl, socialNetwork.getManager());
        description = element.valueOf("description");
        type = element.valueOf("company-type/name");

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getWebsite() {
        return website;
    }

    public RTXBLOBData getLogo() {
        return logo;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

}
