package com.webratio.units.store.social;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractContact;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class TwitterContact extends AbstractContact {

    protected String profileImageURL;

    protected TwitterContact(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = jsonObject.optString("id_str");
            name = jsonObject.optString("name");
            nickName = jsonObject.optString("screen_name");
            location = jsonObject.optString("location");
            profileImageURL = jsonObject.optString("profile_image_url");
            if (!StringUtils.isBlank(profileImageURL)) {
                profileImage = BLOBHelper.getRTXBLOBData(profileImageURL, socialNetwork.getManager());
            }
        } catch (Exception e) {
            throw new RTXException("Unable to read contact informations", e);
        }
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

}
