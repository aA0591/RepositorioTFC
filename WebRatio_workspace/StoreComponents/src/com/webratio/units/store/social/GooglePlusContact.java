package com.webratio.units.store.social;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractContact;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class GooglePlusContact extends AbstractContact {

    protected GooglePlusContact(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = jsonObject.optString("id");
            name = nickName = jsonObject.optString("displayName");
            JSONObject image = jsonObject.optJSONObject("image");
            if (image != null) {
                String profileImageUrl = image.optString("url");
                if (!StringUtils.isBlank(profileImageUrl)) {
                    profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());
                }
            }
        } catch (Exception e) {
            throw new RTXException("Unable to read contact informations", e);
        }
    }

}
