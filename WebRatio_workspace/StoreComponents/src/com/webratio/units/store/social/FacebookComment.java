package com.webratio.units.store.social;

import java.text.ParseException;
import java.util.GregorianCalendar;

import org.json.JSONObject;

public class FacebookComment {

    private String id;//
    private String creatorName;//
    private String creatorId;//
    private String message;//
    private GregorianCalendar createdTime;//
    private int likes;//

    public FacebookComment(JSONObject jsonObject) throws ParseException {

        this.id = jsonObject.optString("id");

        this.message = jsonObject.optString("message");
        this.likes = jsonObject.optInt("likes");

        JSONObject creator = jsonObject.optJSONObject("from");
        if (creator != null) {
            this.creatorName = creator.optString("name");
            this.creatorId = creator.optString("id");
        }

        this.createdTime = DateHelper
                .getCalendarFromString(jsonObject.optString("created_time"), DateHelper.FACEBOOK_DATE_TIME_FORMAT);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GregorianCalendar getCreatedTime() {
        return createdTime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
