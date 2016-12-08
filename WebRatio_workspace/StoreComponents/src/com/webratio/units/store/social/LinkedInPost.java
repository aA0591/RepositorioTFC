package com.webratio.units.store.social;

import org.dom4j.Element;

public class LinkedInPost {

    private String title;
    private String message;
    private String id;
    private String creatorName;
    private int numOfLikes;

    public LinkedInPost(Element postElement) {
        this.id = postElement.valueOf("id");
        this.title = postElement.valueOf("title");
        this.message = postElement.valueOf("summary");
        this.creatorName = postElement.valueOf("creator/first-name") + " " + postElement.valueOf("creator/last-name");
        this.numOfLikes = Integer.parseInt(postElement.valueOf("likes/@total"));

    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

}
