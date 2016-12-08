package com.webratio.units.store.social;

public class FacebookConnectionType {

    private String value;

    public static final FacebookConnectionType LIKES = new FacebookConnectionType("likes");
    public static final FacebookConnectionType COMMENTS = new FacebookConnectionType("comments");
    public static final FacebookConnectionType FEED = new FacebookConnectionType("feed");
    public static final FacebookConnectionType ATTENDING = new FacebookConnectionType("attending");
    public static final FacebookConnectionType MAYBE = new FacebookConnectionType("maybe");
    public static final FacebookConnectionType DECLINED = new FacebookConnectionType("declined");
    public static final FacebookConnectionType INVITED = new FacebookConnectionType("invited");
    public static final FacebookConnectionType NOREPLY = new FacebookConnectionType("noreply");
    public static final FacebookConnectionType HOME = new FacebookConnectionType("home");
    public static final FacebookConnectionType VIDEOS = new FacebookConnectionType("videos");
    public static final FacebookConnectionType ALBUMS = new FacebookConnectionType("albums");
    public static final FacebookConnectionType PHOTOS = new FacebookConnectionType("photos");
    public static final FacebookConnectionType EVENTS = new FacebookConnectionType("events");
    public static final FacebookConnectionType GROUPS = new FacebookConnectionType("groups");
    public static final FacebookConnectionType LOCATIONS = new FacebookConnectionType("locations");
    public static final FacebookConnectionType FRIENDS = new FacebookConnectionType("friends");
    public static final FacebookConnectionType NOTES = new FacebookConnectionType("notes");
    public static final FacebookConnectionType TAGS = new FacebookConnectionType("tags");
    public static final FacebookConnectionType CHECKINS = new FacebookConnectionType("checkins");
    public static final FacebookConnectionType INBOX = new FacebookConnectionType("inbox");
    public static final FacebookConnectionType MEMBERS = new FacebookConnectionType("members");
    public static final FacebookConnectionType MUTUALFRIENDS = new FacebookConnectionType("mutualfriends");
    public static final FacebookConnectionType PICTURE = new FacebookConnectionType("picture");
    public static final FacebookConnectionType STATUSES = new FacebookConnectionType("statuses");

    private FacebookConnectionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static FacebookConnectionType fromValue(String value) {
        if (value != null) {
            if (value.equals("likes"))
                return LIKES;
            else if (value.equals("comments"))
                return COMMENTS;
            else if (value.equals("feed"))
                return FEED;
            else if (value.equals("attending"))
                return ATTENDING;
            else if (value.equals("maybe"))
                return MAYBE;
            else if (value.equals("declined"))
                return DECLINED;
            else if (value.equals("invited"))
                return INVITED;
            else if (value.equals("locations"))
                return LOCATIONS;
            else if (value.equals("friends"))
                return FRIENDS;
            else if (value.equals("tags"))
                return TAGS;
            else if (value.equals("members"))
                return MEMBERS;
            else if (value.equals("mutualfriends"))
                return MUTUALFRIENDS;
            else if (value.equals("albums"))
                return ALBUMS;
            else if (value.equals("photos"))
                return PHOTOS;
            else if (value.equals("groups"))
                return GROUPS;
            else if (value.equals("events"))
                return EVENTS;
            else if (value.equals("videos"))
                return VIDEOS;
            else if (value.equals("home"))
                return HOME;
            else if (value.equals("noreply"))
                return NOREPLY;
            else if (value.equals("checkins"))
                return CHECKINS;
            else if (value.equals("updates"))
                return INBOX;
            else if (value.equals("notes"))
                return NOTES;
            else if (value.equals("picture"))
                return PICTURE;
            else if (value.equals("statuses"))
                return STATUSES;
            else
                return null;
        } else {
            return null;
        }
    }

}
