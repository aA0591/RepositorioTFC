package com.webratio.units.store.social;

/**
 * { "id": "220439", "name": "Bret Taylor", "first_name": "Bret", "last_name": "Taylor", "link": "http://www.facebook.com/btaylor",
 * "username": "btaylor", "gender": "male", "locale": "en_US" }
 */
public class FacebookFriend {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String link;
    private String userName;
    private String gender;
    private String locale;

    public FacebookFriend(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public FacebookFriend(String id, String name, String firstName, String lastName, String link, String userName, String gender,
            String locale) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.link = link;
        this.userName = userName;
        this.gender = gender;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLink() {
        return link;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }

    public String getUserName() {
        return userName;
    }

}
