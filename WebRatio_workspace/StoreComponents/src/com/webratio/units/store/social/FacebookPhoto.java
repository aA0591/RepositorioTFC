package com.webratio.units.store.social;

import org.json.JSONObject;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class FacebookPhoto {
    private String id;
    private String posterId;
    private String posterName;
    private String name;
    private String facebookLink;
    private String url;
    private RTXBLOBData source;
    private String thumbnailUrl;
    private RTXBLOBData thumbnail;
    private String link;

    public FacebookPhoto(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        this.id = jsonObject.optString("id");
        this.url = jsonObject.optString("source");
        if (url != null && socialNetwork != null)
            source = BLOBHelper.getRTXBLOBData(url, socialNetwork.getManager());
        this.link = jsonObject.optString("link");
        this.thumbnailUrl = jsonObject.optString("picture");
        if (thumbnailUrl != null && socialNetwork != null)
            thumbnail = BLOBHelper.getRTXBLOBData(thumbnailUrl, socialNetwork.getManager());
        this.facebookLink = jsonObject.optString("link");
        JSONObject poster = jsonObject.getJSONObject("from");
        if (poster != null) {
            this.posterId = poster.optString("id");
            this.posterName = poster.optString("name");
        }
        this.name = jsonObject.optString("name");

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public String getName() {
        return name;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLink() {
        return link;
    }

    public RTXBLOBData getSource() {
        return source;
    }

    public RTXBLOBData getThumbnail() {
        return thumbnail;
    }

}
