package com.webratio.units.store.social;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import org.json.JSONObject;

public class FacebookGroup {

    private String id;//
    private String name;//
    private String description;
    private String ownerId;
    private String ownerName;
    private String link;
    private GregorianCalendar lastChanged;

    public FacebookGroup(JSONObject jsonObject) {

        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.description = jsonObject.optString("description");

        try {
            JSONObject owner = jsonObject.getJSONObject("owner");
            if (owner != null) {
                ownerId = owner.optString("id");
                ownerName = owner.optString("name");
            }
        } catch (NoSuchElementException e) {

        }

        this.link = jsonObject.optString("link");
        this.lastChanged = DateHelper
                .getCalendarFromString(jsonObject.optString("updated_time"), DateHelper.FACEBOOK_DATE_TIME_FORMAT);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getLink() {
        return link;
    }

    public GregorianCalendar getLastChanged() {
        return lastChanged;
    }

}
