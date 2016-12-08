package com.webratio.units.store.social;

import java.util.GregorianCalendar;
import java.util.List;

@SuppressWarnings("rawtypes")
public class DoodlePoll {

    private String id;
    private String adminKey;
    private GregorianCalendar lastChange;
    private DoodlePollType type;
    private boolean hidden;
    private DoodleLevel levels;
    private DoodleState state;
    private String title;
    private String location;
    private String description;
    private DoodleInitiator initiator;
    private List options;
    private List participants;
    private List comments;

    /**
     * @param title
     * @param description
     * @param location
     * @param initiator
     * @param options
     */
    /* public PollImpl(String title, String description,String location, Initiator initiator,List<Option> options) */
    public DoodlePoll(String id, String adminKey, GregorianCalendar lastChange, DoodlePollType type, boolean hidden,
            DoodleLevel levels, DoodleState state, String title, String location, String description, DoodleInitiator initiator,
            List options, List participants, List comments) {
        this.id = id;
        this.adminKey = adminKey;
        this.lastChange = lastChange;
        this.type = type;
        this.hidden = hidden;
        this.levels = levels;
        this.state = state;
        this.title = title;
        this.location = location;
        this.description = description;
        this.initiator = initiator;
        this.options = options;
        this.participants = participants;
        this.comments = comments;
    }

    // to PUT
    public DoodlePoll(DoodlePollType type, boolean hidden, DoodleLevel levels, String title, String location, String description,
            DoodleInitiator initiator, List options) {
        this.type = type;
        this.hidden = hidden;
        this.levels = levels;
        this.title = title;
        this.location = location;
        this.description = description;
        this.initiator = initiator;
        this.options = options;
    }

    // to MODIFY
    public DoodlePoll(String id, String adminKey, DoodlePollType type, boolean hidden, DoodleLevel levels, String title,
            String location, String description, DoodleInitiator initiator, List options) {
        this.id = id;
        this.adminKey = adminKey;
        this.type = type;
        this.hidden = hidden;
        this.levels = levels;
        this.title = title;
        this.location = location;
        this.description = description;
        this.initiator = initiator;
        this.options = options;
    }

    // to GET
    public DoodlePoll(String id, GregorianCalendar lastChange, DoodlePollType type, boolean hidden, DoodleLevel levels,
            DoodleState state, String title, String location, String description, DoodleInitiator initiator, List options,
            List participants, List comments) {
        this.lastChange = lastChange;
        this.type = type;
        this.hidden = hidden;
        this.levels = levels;
        this.state = state;
        this.title = title;
        this.location = location;
        this.description = description;
        this.initiator = initiator;
        this.options = options;
        this.participants = participants;
        this.comments = comments;
        this.id = id;

    }

    // to CLOSE

    public DoodlePoll(String id, String adminKey, DoodlePoll poll, DoodleState state, List options) {
        this.id = id;
        this.adminKey = adminKey;
        this.type = poll.getType();
        this.hidden = poll.isHidden();
        this.levels = poll.getLevels();
        this.state = state;
        this.title = poll.getTitle();
        this.location = poll.getLocation();
        this.description = poll.getDescription();
        this.initiator = poll.getInitiator();
        this.options = options;
    }

    public DoodlePoll(String id, String adminKey) {
        this.id = id;
        this.adminKey = adminKey;
    }

    public DoodlePoll(String id, String adminKey, String title) {
        this.id = id;
        this.adminKey = adminKey;
        this.title = title;
    }

    public DoodlePoll(String id, String adminKey, String title, String state) {
        this.id = id;
        this.adminKey = adminKey;
        this.title = title;
        this.state = DoodleState.fromValue(state);
    }

    public String getId() {
        return id;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public GregorianCalendar getLastChange() {
        return lastChange;
    }

    public DoodlePollType getType() {
        return type;
    }

    public void setType(DoodlePollType type) {

        this.type = type;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public DoodleLevel getLevels() {
        return levels;
    }

    public void setLevels(DoodleLevel levels) {
        this.levels = levels;
    }

    public DoodleState getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getParticipants() {
        return participants;
    }

    public List getComments() {
        return comments;
    }

    public DoodleInitiator getInitiator() {
        return this.initiator;
    }

    public List getOptions() {

        return this.options;
    }

    public boolean isClosed() {
        return this.getState().equals(DoodleState.CLOSED);
    }

}