package com.webratio.units.store.social;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import org.dom4j.Element;
import org.json.JSONObject;

import twitter4j.Status;

import com.webratio.rtx.RTXException;

@SuppressWarnings("rawtypes")
public class TwitterTweet {
    private String statusId;
    private String userId;
    private String userName;
    private String text;
    private GregorianCalendar createdAt;
    private Boolean retweeted;

    private int retweetCount;
    private String source;
    private Boolean favorited;
    private TwitterTweet retweetedStatus;

    public TwitterTweet(JSONObject jsonObject) {
        this(jsonObject, Boolean.FALSE);
    }

    public TwitterTweet(JSONObject jsonObject, Boolean retweeted) {

        this.text = jsonObject.optString("text");
        this.statusId = jsonObject.optString("id_str");
        this.retweetCount = jsonObject.optInt("retweet_count");
        this.retweeted = Boolean.valueOf(jsonObject.getBoolean("retweeted"));
        this.source = jsonObject.optString("source");
        this.favorited = Boolean.valueOf(jsonObject.getBoolean("favorited"));

        // Gets the embeded retweet if it has been retweeted
        if (retweeted.equals(Boolean.TRUE)) {
            retweetedStatus = new TwitterTweet(jsonObject.getJSONObject("retweeted_status"), Boolean.FALSE);
        }

        createdAt = DateHelper.getCalendarFromString(jsonObject.optString("created_at"), DateHelper.TWITTER_DATE_TIME_FORMAT);

        JSONObject user = jsonObject.getJSONObject("user");
        userId = user.optString("id_str");
        userName = user.optString("name");
    }

    public TwitterTweet(Status tweet) {
        this.text = tweet.getText();
        this.statusId = new Long(tweet.getId()).toString();
        this.retweetCount = new Long(tweet.getRetweetCount()).intValue();
        this.retweeted = Boolean.valueOf(tweet.isRetweetedByMe());
        this.source = tweet.getSource();
        this.favorited = Boolean.valueOf(tweet.isFavorited());

        if (retweeted.booleanValue()) {
            this.retweetedStatus = new TwitterTweet(tweet.getRetweetedStatus());
        }
    }

    protected TwitterTweet(Element element) throws RTXException {

        String name = "";
        this.createdAt = (GregorianCalendar) Calendar.getInstance();
        SimpleDateFormat datetwitterFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);

        name = element.getName();
        try {
            if (name.equals("status")) {
                Iterator it2 = element.elementIterator();
                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    name = element2.getName();
                    if (name.equals("created_at"))
                        this.createdAt.setTime(datetwitterFormat.parse(element2.getText()));
                    if (name.equals("id"))
                        this.statusId = element2.getText();
                    if (name.equals("text"))
                        this.text = element2.getText();
                    if (name.equals("user")) {
                        Iterator it3 = element2.elementIterator();
                        while (it3.hasNext()) {
                            Element element3 = (Element) it3.next();
                            name = element3.getName();
                            if (name.equals("id"))
                                this.userId = element3.getText();
                            if (name.equals("screen_name"))
                                this.userName = element3.getText();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RTXException("Unable to read contact informations", e);
        }
    }

    public TwitterTweet(String statusId, String userId, String user, String text, GregorianCalendar createAt) {

        this.statusId = statusId;
        this.userId = userId;
        this.userName = user;
        this.text = text;
        this.createdAt = createAt;
    }

    public String getStatusId() {
        return statusId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public GregorianCalendar getCreatedAt() {
        return createdAt;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public String getSource() {
        return source;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public TwitterTweet getRetweetedStatus() {
        return retweetedStatus;
    }
}
