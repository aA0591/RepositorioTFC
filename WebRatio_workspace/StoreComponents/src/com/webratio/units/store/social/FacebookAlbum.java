package com.webratio.units.store.social;

import java.util.GregorianCalendar;

import org.json.JSONObject;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

public class FacebookAlbum {

    private String id;
    private String ownerId;
    private String ownerName;
    private String name;
    private String description;
    private String location;
    private String link;
    private int numberOfPhotos;
    private String coverPhotoId;
    private String coverPhotoUrl;
    private RTXBLOBData coverPhoto;
    private GregorianCalendar lastUpdate;

    public FacebookAlbum(JSONObject jsonObject) {

        this.id = jsonObject.optString("id");

        JSONObject owner = jsonObject.getJSONObject("from");
        if (owner != null) {
            this.ownerName = owner.optString("name");
            this.ownerId = owner.optString("id");
        }

        this.name = jsonObject.optString("name");
        this.description = jsonObject.optString("description");
        this.location = jsonObject.optString("location");
        this.link = jsonObject.optString("link");
        String count = jsonObject.optString("count");
        if (count != null && !count.equals(""))
            this.numberOfPhotos = new Integer(jsonObject.optString("count")).intValue();
        this.coverPhotoId = jsonObject.optString("cover_photo");
        this.coverPhotoUrl = FacebookURLHelper.createUrl(id, FacebookConnectionType.PICTURE);
        this.lastUpdate = DateHelper.getCalendarFromString(jsonObject.optString("updated_time"), DateHelper.FACEBOOK_DATE_TIME_FORMAT);

    }

    public FacebookAlbum(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        this(jsonObject);

        this.coverPhoto = BLOBHelper.getRTXBLOBData(coverPhotoUrl, socialNetwork.getManager());

    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }

    public int getNumberOfPhotos() {
        return numberOfPhotos;
    }

    public String getCoverPhotoId() {
        return coverPhotoId;
    }

    public GregorianCalendar getLastUpdate() {
        return lastUpdate;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public RTXBLOBData getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(RTXBLOBData coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

}
