package com.webratio.units.store.social;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.blob.BLOBHelper;
import com.webratio.units.store.commons.application.ISocialNetworkService;

//TODO
public class FacebookEvent {
    private String id;
    private String name;
    private String ownerId;
    private String ownerName;
    private String description;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private String location;
    private String pictureUrl;
    private RTXBLOBData picture;
    private String url;
    private String rsvpStatus;
    private final static String eventUrl = "http://www.facebook.com/events/${eventId}";

    public static final String GOING = FacebookConnectionType.ATTENDING.getValue();
    public static final String MAYBE = FacebookConnectionType.MAYBE.getValue();
    public static final String DECLINE = FacebookConnectionType.DECLINED.getValue();

    public FacebookEvent(JSONObject jsonObject) throws RTXException {
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.optString("name");

            this.description = jsonObject.optString("description");
            this.startTime = DateHelper
                    .getCalendarFromString(jsonObject.optString("start_time"), DateHelper.FACEBOOK_DATE_TIME_FORMAT);
            this.endTime = DateHelper.getCalendarFromString(jsonObject.optString("end_time"), DateHelper.FACEBOOK_DATE_TIME_FORMAT);
            this.location = jsonObject.optString("location");
            JSONObject owner = jsonObject.optJSONObject("owner");
            if (owner != null) {
                this.ownerId = owner.optString("id");
                this.ownerName = owner.optString("name");
            }
            this.pictureUrl = FacebookURLHelper.createUrl(id, FacebookConnectionType.PICTURE);
            this.url = StringUtils.replace(eventUrl, "${userId}", this.id);
            this.rsvpStatus = jsonObject.optString("rsvp_status");
        } catch (NoSuchElementException e) {
            throw new RTXException(e);
        }
    }

    public FacebookEvent(JSONObject jsonObject, ISocialNetworkService socialNetwork) throws RTXException {
        this(jsonObject);

        if (pictureUrl != null) {
            this.picture = BLOBHelper.getRTXBLOBData(pictureUrl, socialNetwork.getManager());
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getDescription() {
        return description;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public RTXBLOBData getPicture() {
        return picture;
    }

    public String getRsvpStatus() {
        return rsvpStatus;
    }

    public static String getEventurl() {
        return eventUrl;
    }

}
