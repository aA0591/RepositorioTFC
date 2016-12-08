package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class FacebookThread {

    private String id;
    private GregorianCalendar dateOfCreation;
    private String senderId;
    private String senderName;
    private String recipientId;
    private String recipientName;

    public FacebookThread(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.dateOfCreation = DateHelper.getCalendarFromString(jsonObject.optString("created_time"),
                DateHelper.FACEBOOK_DATE_TIME_FORMAT);
        JSONArray to = jsonObject.getJSONObject("to").getJSONArray("data");
        JSONObject sender = to.getJSONObject(0);
        if (sender != null) {
            this.senderId = sender.optString("id");
            this.senderName = sender.optString("name");
        }
        if(to.length() > 1) {
            JSONObject recipient = to.getJSONObject(1);
            if (recipient != null) {
                this.recipientId = recipient.optString("id");
                this.recipientName = recipient.optString("name");
            }
        }
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

    public String getRecipientId() {
        return recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }
}
