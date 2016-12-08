package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FacebookPost {
    private String id;
    private String message;
    private List likes;
    private String userId;
    private String userName;
    private String fullId;
    private String url;
    private Integer shares;
    private String type;
    private List comments;
    private Integer commentsCount;
    private Integer likesCount;
    private GregorianCalendar createdTime;

    public FacebookPost(String id) {
        this.id = id;
    }

    public FacebookPost(JSONObject jsonObject) throws RTXException {
        try {
            this.id = jsonObject.getString("id");
            this.message = jsonObject.optString("message");
            this.url = FacebookURLHelper.createUrl(id);
            this.createdTime = DateHelper.getCalendarFromString(jsonObject.optString("created_time"),
                    DateHelper.FACEBOOK_DATE_TIME_FORMAT);

            JSONObject from = jsonObject.getJSONObject("from");
            if (from != null) {
                this.userName = from.optString("name");
                this.userId = from.optString("id");
            }

            JSONObject jsonShares = jsonObject.optJSONObject("shares");
            if (jsonShares != null) {
                this.shares = new Integer(jsonShares.optInt("count"));
            }
            this.type = jsonObject.optString("type");

            JSONObject jsonLikes = jsonObject.optJSONObject("likes");
            List likes = new ArrayList();
            if (jsonLikes != null) {
                JSONArray data = jsonLikes.optJSONArray("data");
                this.likesCount = new Integer(data.length());
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        FacebookLike like = new FacebookLike(data.getJSONObject(i));
                        likes.add(like);
                    }
                }
            }
            this.likes = likes;

            JSONObject jsonComments = jsonObject.optJSONObject("comments");
            List comments = new ArrayList();
            if (jsonComments != null) {
                JSONArray data = jsonComments.optJSONArray("data");
                this.commentsCount = new Integer(data.length());
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        FacebookComment comment = new FacebookComment(data.getJSONObject(i));
                        comments.add(comment);
                    }
                }
            }
            this.comments = comments;
        } catch (Exception e) {
            throw new RTXException("Unable to read post information", e);
        }
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

    public List getLikes() {
        return likes;
    }

    public void setLikes(List likes) {
        this.likes = likes;
    }

    public List getComments() {
        return comments;
    }

    public void setComments(List comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public GregorianCalendar getCreatedTime() {
        return createdTime;
    }

}
