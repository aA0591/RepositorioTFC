package com.webratio.units.store.social;

import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.AbstractContact;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class FacebookContact extends AbstractContact {
    private String gender;
    private String firstName;
    private String lastName;
    private String link;
    
	FacebookContact(JSONObject jsonContact, ISocialNetworkService socialNetwork) throws RTXException {
        super(socialNetwork);
        try {
            id = jsonContact.optString("id");
            name = nickName = jsonContact.optString("name");
            link = jsonContact.optString("link");
            JSONObject jsonLocation = jsonContact.optJSONObject("location");
            if (jsonLocation != null) {
                location = jsonLocation.optString("name");
            }
            gender = jsonContact.optString("gender");
            firstName = jsonContact.optString("first_name");
            lastName = jsonContact.optString("last_name");

            String profileImageUrl = FacebookURLHelper.createUrl(id, FacebookConnectionType.PICTURE);
            profileImage = BLOBHelper.getRTXBLOBData(profileImageUrl, socialNetwork.getManager());

        } catch (Exception e) {
            throw new RTXException("Unable to read contact information", e);
        }
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
