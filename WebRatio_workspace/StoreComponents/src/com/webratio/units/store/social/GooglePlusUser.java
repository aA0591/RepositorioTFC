package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractApplicationService;
import com.webratio.units.store.commons.application.AbstractUser;
import com.webratio.units.store.commons.application.IApplication;

public class GooglePlusUser extends AbstractUser {

    private static final long serialVersionUID = 1L;

    private String location;
    private String[] companies;
    private String firstName;
    private String lastName;
    private String publicProfileUrl;
    private String imagePixel = "200"; // pixel profile image

    public GooglePlusUser(JSONObject jsonObject, IApplication application, String email) throws RTXException {
        super(application);
        try {
            id = jsonObject.getString("id");
            userName = jsonObject.getString("displayName");
            publicProfileUrl = jsonObject.getString("url");
            JSONObject name = jsonObject.optJSONObject("name");
            if (name != null) {
                firstName = name.optString("givenName");
                lastName = name.optString("familyName");
            }
            JSONObject imageObj = jsonObject.getJSONObject("image");
            if (imageObj != null) {
                String profileImageUrl = imageObj.getString("url");
                if (!StringUtils.isBlank(profileImageUrl)) {
                    profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, ((AbstractApplicationService) application).getManager());
                    profileImageUrl = profileImageUrl.replaceAll("sz=50", "sz=" + imagePixel);
                    largeProfileImage = BLOBHelper.getRTXBLOBData(profileImageUrl,
                            ((AbstractApplicationService) application).getManager());
                }
            }
            this.email = email;
            JSONArray locationsList = jsonObject.optJSONArray("placesLived");
            location = null;
            if (locationsList != null) {
                for (int i = 0; i < locationsList.length(); i++) {
                    if (locationsList.getJSONObject(i).optBoolean("primary")) {
                        location = locationsList.getJSONObject(i).getString("value");
                    }
                }
            }
            List tempCompanies = new ArrayList();
            JSONArray organizations = jsonObject.optJSONArray("organizations");
            if (organizations != null) {
                for (int i = 0; i < organizations.length(); i++) {
                    if ("work".equals(organizations.getJSONObject(i).getString("type"))) {
                        String company = organizations.getJSONObject(i).optString("name");
                        if (!StringUtils.isBlank(company)) {
                            tempCompanies.add(company);
                        }
                    }
                }
            }
            companies = (String[]) tempCompanies.toArray(new String[0]);
        } catch (Exception e) {
            throw new RTXException("Unable to read user informations", e);
        }
    }

    public String getLocation() {
        return location;
    }

    public String[] getCompanies() {
        return companies;
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
