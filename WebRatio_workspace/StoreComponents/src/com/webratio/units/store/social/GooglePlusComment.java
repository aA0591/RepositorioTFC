package com.webratio.units.store.social;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class GooglePlusComment {

    private String id;//
    private String creatorName;//
    private String creatorId;//
    private String content;//
    private Date published;//
    private Date updated;//

    public GooglePlusComment(JSONObject jsonObject) throws ParseException {

        SimpleDateFormat rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        this.id = jsonObject.optString("id");

        String publishedString = jsonObject.optString("published");
        String updatedString = jsonObject.optString("updated");

        this.published = rfc3339.parse(publishedString);
        this.updated = rfc3339.parse(updatedString);

        JSONObject activityObject = jsonObject.getJSONObject("object");
        this.content = activityObject.optString("content");

        JSONObject creator = jsonObject.getJSONObject("actor");
        this.creatorName = creator.optString("displayName");
        this.creatorId = creator.optString("id");

    }

    public String getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getContent() {
        return content;
    }

    public Date getPublished() {
        return published;
    }

    public Date getUpdated() {
        return updated;
    }

}
