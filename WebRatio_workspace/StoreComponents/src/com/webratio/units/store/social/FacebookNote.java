package com.webratio.units.store.social;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;

public class FacebookNote {
    private String id;
    private String userId;
    private String fullId;
    private String url;
    private String subject;
    private String message;
    private final static String noteUrl = "https://www.facebook.com/notes/${userId}/note/${noteId}";

    public FacebookNote(JSONObject jsonObject) throws RTXException {
        String fullId = jsonObject.optString("id");
        this.id = StringUtils.substringAfter(fullId, "_");
        this.userId = StringUtils.substringBefore(fullId, "_");
        this.fullId = fullId;
        String url = StringUtils.replace(noteUrl, "${userId}", this.userId);
        url = StringUtils.replace(url, "${noteId}", this.id);
        this.url = url;
        this.subject = jsonObject.optString("subject");
        this.message = jsonObject.optString("message");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
