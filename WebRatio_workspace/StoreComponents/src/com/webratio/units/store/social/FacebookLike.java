package com.webratio.units.store.social;

import java.text.ParseException;

import org.json.JSONObject;

public class FacebookLike {

    private String id;//
    private String name;//

    public FacebookLike(JSONObject jsonObject) throws ParseException {

        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setCreatorName(String name) {
        this.name = name;
    }
}
