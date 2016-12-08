package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.json.JSONObject;

public class FacebookMessage {

    private String id;
    private GregorianCalendar dateOfCreation;
    private String senderId;
    private String senderName;
    private String content;

    public FacebookMessage(JSONObject jsonObject) {

        this.id = jsonObject.optString("id");
        this.dateOfCreation = DateHelper.getCalendarFromString(jsonObject.optString("created_time"),
                DateHelper.FACEBOOK_DATE_TIME_FORMAT);

        JSONObject sender = jsonObject.optJSONObject("from");
        if (sender != null) {
            this.senderId = sender.optString("id");
            this.senderName = sender.optString("name");
        }
        this.content = jsonObject.optString("message");
    }

    public String getId() {
        return id;
    }

    public GregorianCalendar getDateOfCreation() {
        return dateOfCreation;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

}
