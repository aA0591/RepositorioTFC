package com.webratio.units.store.social;

import java.util.List;

@SuppressWarnings("rawtypes")
public class GoogleCalendarList {

    String nextPageToken;
    List items;

    public GoogleCalendarList(String nextPageToken, List items) {
        super();
        this.nextPageToken = nextPageToken;
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List getItems() {
        return items;
    }

}
