package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractUser;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class FacebookUser extends AbstractUser {

    private static final long serialVersionUID = 1L;

    private String location;
    private String[] companies;
    private String firstName;
    private String lastName;
    private String publicProfileUrl;

    protected FacebookUser(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = jsonObject.getString("id");
            userName = jsonObject.getString("name");
            firstName = jsonObject.optString("first_name");
            lastName = jsonObject.optString("last_name");
            email = jsonObject.optString("email");
            publicProfileUrl = jsonObject.optString("link");
            String profileImageUrl = "https://graph.facebook.com/" + id + "/picture";
            profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());
            largeProfileImage = BLOBHelper.getRTXBLOBData(profileImageUrl + "?type=large", socialNetwork.getManager());
            JSONObject jsonLocation = jsonObject.optJSONObject("location");
            if (jsonLocation != null) {
                location = jsonLocation.optString("name");
            }
            List tempCompanies = new ArrayList();
            JSONArray jobList = jsonObject.optJSONArray("work");
            if (jobList != null) {
                for (int i = 0; i < jobList.length(); i++) {
                    JSONObject employer = jobList.getJSONObject(i).optJSONObject("employer");
                    if (employer != null) {
                        String company = employer.optString("name");
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
