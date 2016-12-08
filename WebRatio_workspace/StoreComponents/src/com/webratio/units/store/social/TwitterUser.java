package com.webratio.units.store.social;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractUser;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.auth.IAuthManager;

public class TwitterUser extends AbstractUser {

    private static final long serialVersionUID = 1L;

    private String location;
    private String firstName;
    private String lastName;
    private String publicProfileUrl;

    protected TwitterUser(JSONObject jsonObject, ISocialNetworkService socialNetwork, IAuthManager authMgr) throws RTXException {
        super(socialNetwork);
        try {
            id = jsonObject.getString("id");
            userName = jsonObject.getString("screen_name");
            String composedName = jsonObject.getString("name");
            publicProfileUrl = "https://twitter.com/" + userName;
            if (composedName.indexOf(" ") > -1) {
                String parts[] = composedName.split(" ");
                firstName = parts[0];
                lastName = parts[1];
                int i = 2;
                while (i < parts.length) {
                    lastName = lastName + parts[i];
                    i++;
                }
            }
            String profileImageUrl = jsonObject.optString("profile_image_url_https");
            if (!StringUtils.isBlank(profileImageUrl)) {
                profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());
                profileImageUrl = profileImageUrl.replaceAll("_normal", "");
                largeProfileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());
            }
            location = jsonObject.optString("location");
        } catch (Exception e) {
            throw new RTXException("Unable to read user informations", e);
        }
    }

    public String getLocation() {
        return location;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

}
