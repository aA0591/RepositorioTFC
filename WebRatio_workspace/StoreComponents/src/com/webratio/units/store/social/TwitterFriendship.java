package com.webratio.units.store.social;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TwitterFriendship {

    private String userId;
    private String friendId;

    private String friendName;
    private List connections;

    public TwitterFriendship(JSONObject jsonObject, String userId) {
        this.friendName = jsonObject.optString("name");
        this.friendId = jsonObject.optString("id_str");
        this.userId = userId;

        this.connections = new ArrayList();
        JSONArray jsonConnections = jsonObject.getJSONArray("connections");
        for (int i = 0; i < jsonConnections.length(); i++) {
            this.connections.add(TwitterConnection.fromValue(jsonConnections.getString(i)));
        }

    }

    public String getUserId() {
        return userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public List getConnections() {
        return connections;
    }

}
