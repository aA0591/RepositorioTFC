package com.webratio.units.store.social;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class GooglePlusPost {

    private String id;//
    private String creatorName;//
    private String creatorId;//
    private String type;//
    private String content;//
    private String htmlContent;//
    private int commentsCount;//
    private int likesCount;//
    private String title;//
    private Date published;
    private Date updated;

    public GooglePlusPost(JSONObject jsonObject) throws ParseException {

        SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        this.title = jsonObject.optString("title");
        this.type = jsonObject.optString("verb");
        this.id = jsonObject.optString("id");

        String publishedString = jsonObject.optString("published");
        String updatedString = jsonObject.optString("updated");

        this.published = rfc3339.parse(publishedString);
        this.updated = rfc3339.parse(updatedString);

        JSONObject activityObject = jsonObject.getJSONObject("object");
        this.content = activityObject.optString("originalContent");
        this.htmlContent = activityObject.optString("content");

        JSONObject plusoners = activityObject.getJSONObject("plusoners");
        this.likesCount = plusoners.getInt("totalItems");

        JSONObject comments = activityObject.getJSONObject("replies");
        this.commentsCount = comments.getInt("totalItems");

        JSONObject creator = jsonObject.getJSONObject("actor");
        this.creatorName = creator.optString("displayName");
        this.creatorId = creator.optString("id");

    }

    public String getTitle() {
        return title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getId() {
        return id;
    }

    public Date getPublished() {
        return published;
    }

    public Date getUpdated() {
        return updated;
    }
}
