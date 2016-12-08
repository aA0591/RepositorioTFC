package com.webratio.units.store.social;

import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.units.store.commons.application.AbstractUser;
import com.webratio.units.store.commons.application.IApplication;

public class GoogleDocsUser extends AbstractUser {
    public GoogleDocsUser(JSONObject jsonObject, IApplication application) throws RTXException {
        super(application);
        try {
            id = jsonObject.getString("id");
            userName = jsonObject.getString("name");
        } catch (Exception e) {
            throw new RTXException("Unable to read user informations", e);
        }

    }

}
