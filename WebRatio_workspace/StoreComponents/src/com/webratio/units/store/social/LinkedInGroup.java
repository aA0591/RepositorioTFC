package com.webratio.units.store.social;

import org.dom4j.Element;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class LinkedInGroup {

    private String id;
    private String name;
    private RTXBLOBData logo;

    public LinkedInGroup(Element groupElement) {
        this.id = groupElement.valueOf("id");
        this.name = groupElement.valueOf("name");

    }

    public LinkedInGroup(Element groupElement, ISocialNetworkService socialNetwork) throws RTXException {
        this(groupElement);
        String logoUrl = groupElement.valueOf("small-logo-url");
        this.logo = BLOBHelper.getRTXBLOBData(logoUrl, socialNetwork.getManager());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RTXBLOBData getLogo() {
        return logo;
    }

}
