package com.webratio.units.store.social;

import java.util.List;

@SuppressWarnings("rawtypes")
public class TwitterContactList {

    private List contacts;
    private String nextCursor;
    private String previousCursor;

    public TwitterContactList(List contacts, String nextCursor, String previousCursor) {
        super();
        this.contacts = contacts;
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }

    public TwitterContact getContact(int index) {
        return (TwitterContact) contacts.get(index);
    }

    public String getPreviousCursor() {
        return previousCursor;
    }

    public List getContacts() {
        return contacts;
    }

    public String getNextCursor() {
        return nextCursor;
    }

}
