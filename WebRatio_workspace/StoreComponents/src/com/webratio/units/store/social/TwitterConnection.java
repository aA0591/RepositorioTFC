package com.webratio.units.store.social;

public class TwitterConnection {

    private String value;

    public static final TwitterConnection FOLLOWED_BY = new TwitterConnection("followed_by");
    public static final TwitterConnection FOLLOWING = new TwitterConnection("following");
    public static final TwitterConnection FOLLOWING_REQUESTED = new TwitterConnection("following_requested");
    public static final TwitterConnection NONE = new TwitterConnection("none");

    private TwitterConnection(String value) {
        this.value = value;
    }

    public String getTitle() {
        if (this.equals(FOLLOWED_BY))
            return "Followed by user";
        else if (this.equals(FOLLOWING))
            return "Following user";
        else if (this.equals(FOLLOWING_REQUESTED))
            return "Pending follow request";
        else
            return "No connection";

    }

    public String value() {
        return value;
    }

    public static TwitterConnection fromValue(String value) {
        if (value != null) {
            if (value.equals("followed_by"))
                return FOLLOWED_BY;
            else if (value.equals("following"))
                return FOLLOWING;
            else if (value.equals("following_requested"))
                return FOLLOWING_REQUESTED;
            else
                return NONE;
        } else {
            return NONE;
        }

    }

}
