package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.json.JSONObject;

public class TwitterMessage {

    private String text;
    private String id;
    private GregorianCalendar createdAt;
    private String recipientId;
    private String recipientName;
    private String senderId;
    private String senderName;

    public TwitterMessage(JSONObject jsonObject) {

        text = jsonObject.optString("text");
        id = jsonObject.optString("id_str");

        createdAt = DateHelper.getCalendarFromString(jsonObject.optString("created_at"), DateHelper.TWITTER_DATE_TIME_FORMAT);

        recipientId = jsonObject.optString("recipient_id");
        recipientName = jsonObject.optString("recipient_screen_name");

        senderId = jsonObject.optString("sender_id");
        senderName = jsonObject.optString("sender_screen_name");

    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public GregorianCalendar getCreatedAt() {
        return createdAt;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

}
