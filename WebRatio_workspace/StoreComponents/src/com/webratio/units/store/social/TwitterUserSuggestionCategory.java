package com.webratio.units.store.social;

import org.json.JSONObject;

public class TwitterUserSuggestionCategory {

    private String name;
    private String slug;
    private int size;

    public TwitterUserSuggestionCategory(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.slug = jsonObject.optString("slug");
        this.size = jsonObject.optInt("size");

    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getSize() {
        return size;
    }

}
