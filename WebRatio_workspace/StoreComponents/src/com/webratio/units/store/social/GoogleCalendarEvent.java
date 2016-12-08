package com.webratio.units.store.social;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GoogleCalendarEvent {

    private String id;
    private String status;
    private String link;
    private GregorianCalendar created;
    private String summary;
    private String location;
    private String description;
    private String organizerName;
    private String organizerId;
    private Boolean allDay;

    private Date startDate;
    private Date startDateTime;
    private String startTimeZone;
    private Date endDate;
    private Date endDateTime;
    private String endTimeZone;

    private List attendees;
    private List reminders;

    public GoogleCalendarEvent(String id, String summary, String location, String description, String organizerName,
            String organizerId, Boolean allDay, Date startDateTime, Date startDate, String startTimeZone, Date endDateTime,
            Date endDate, String endTimeZone, List attendees, List reminders) {
        super();
        this.id = id;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.organizerName = organizerName;
        this.organizerId = organizerId;
        this.startDateTime = startDateTime;
        this.startDate = startDate;
        this.startTimeZone = startTimeZone;
        this.endDateTime = endDateTime;
        this.endDate = endDate;
        this.endTimeZone = endTimeZone;
        this.attendees = attendees;
        this.reminders = reminders;
        this.allDay = allDay;
    }

    public GoogleCalendarEvent(String summary, String location, String description, String organizerName, String organizerId,
            Boolean allDay, Date startDateTime, Date startDate, String startTimeZone, Date endDateTime, Date endDate,
            String endTimeZone, List attendees, List reminders) {
        super();
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.organizerName = organizerName;
        this.organizerId = organizerId;
        this.startDateTime = startDateTime;
        this.startDate = startDate;
        this.startTimeZone = startTimeZone;
        this.endDateTime = endDateTime;
        this.endTimeZone = endTimeZone;
        this.endDate = endDate;
        this.attendees = attendees;
        this.reminders = reminders;
        this.allDay = allDay;
    }

    public GoogleCalendarEvent(JSONObject event) throws ParseException {

        SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateHelper.GOOGLE_CALENDAR_DATE_TIME_FORMAT);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        this.id = event.optString("id");
        this.status = event.optString("status");
        this.link = event.optString("htmlLink");
        this.created = DateHelper.getCalendarFromString(event.optString("created"), DateHelper.GOOGLE_CALENDAR_DATE_TIME_FORMAT);
        this.summary = event.optString("summary");
        this.location = event.optString("location");
        this.description = event.optString("description");

        try {
            JSONObject organizer = event.getJSONObject("organizer");
            if (organizer != null) {
                this.organizerName = organizer.optString("displayName");
                this.organizerId = organizer.optString("id");
            }
        } catch (NoSuchElementException e) {
            this.organizerId = null;
            this.organizerName = null;
        }

        JSONObject start = event.getJSONObject("start");
        if (start != null) {
            if (start.optString("dateTime") != null && !start.optString("dateTime").equals(""))
                this.startDateTime = DateHelper.getDateFromRFC3339String(start.optString("dateTime"));
            this.allDay = Boolean.FALSE;
            if (start.optString("date") != null && !start.optString("date").equals("")) {
                this.startDate = sdfDate.parse(start.optString("date"));
                this.allDay = Boolean.TRUE;
            }
            this.startTimeZone = start.optString("timeZone");
        }

        JSONObject end = event.getJSONObject("end");
        if (end != null) {
            if (end.optString("dateTime") != null && !end.optString("dateTime").equals(""))
                this.endDateTime = DateHelper.getDateFromRFC3339String(end.optString("dateTime"));
            this.allDay = Boolean.FALSE;
            if (end.optString("date") != null && !end.optString("date").equals("")) {
                this.endDate = sdfDate.parse(end.optString("date"));
                this.allDay = Boolean.TRUE;
            }
            this.endTimeZone = end.optString("timeZone");
        }

        try {
            JSONArray attendees = event.getJSONArray("attendees");
            this.attendees = new ArrayList();
            if (attendees != null && attendees.length() > 0) {
                for (int i = 0; i < attendees.length(); i++) {
                    this.attendees.add(new GoogleCalendarEventAttendee(attendees.getJSONObject(i)));
                }
            }
        } catch (NoSuchElementException e) {
            this.attendees = null;
        }

        try {
            JSONObject reminders = event.getJSONObject("reminders");
            this.reminders = new ArrayList();
            JSONArray overrides = reminders.getJSONArray("overrides");
            if (overrides != null && overrides.length() > 0) {
                for (int i = 0; i < overrides.length(); i++) {
                    this.reminders.add(new GoogleCalendarEventReminder(overrides.getJSONObject(i)));
                }
            }
        } catch (NoSuchElementException e) {
            this.reminders = null;
        }

    }

    public JSONObject getJSONObject() {

        JSONObject result = new JSONObject();

        if (this.summary != null && !this.summary.equals(""))
            result.put("summary", this.summary);
        if (this.location != null && !this.location.equals(""))
            result.put("location", this.location);
        if (this.description != null && !this.description.equals(""))
            result.put("description", this.description);

        if (this.id != null)
            result.put("id", this.id);

        if (this.organizerName != null && !this.organizerName.equals("")) {
            JSONObject organizer = new JSONObject();
            if (this.organizerId != null && !this.organizerId.equals(""))
                organizer.put("id", this.organizerId);
            organizer.put("displayName", this.organizerName);
            result.put("organizer", organizer);
        }

        SimpleDateFormat sdfDateTime = new SimpleDateFormat(DateHelper.GOOGLE_CALENDAR_DATE_TIME_FORMAT);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        if (this.startDateTime != null || (this.startTimeZone != null && !this.startTimeZone.equals(""))) {
            JSONObject start = new JSONObject();
            if (this.startDateTime != null && !this.allDay.booleanValue())
                start.put("dateTime", sdfDateTime.format(this.startDateTime));
            if (this.startDate != null && this.allDay.booleanValue())
                start.put("date", sdfDate.format(this.startDate));
            if (this.startTimeZone != null && !this.startTimeZone.equals(""))
                start.put("timeZone", this.startTimeZone);
            result.put("start", start);
        }

        if (this.endDateTime != null || (this.endTimeZone != null && !this.endTimeZone.equals(""))) {
            JSONObject end = new JSONObject();
            if (this.endDateTime != null && !this.allDay.booleanValue())
                end.put("dateTime", sdfDateTime.format(this.endDateTime));
            if (this.endDate != null && this.allDay.booleanValue())
                end.put("date", sdfDate.format(this.endDate));
            if (this.endTimeZone != null && !this.endTimeZone.equals(""))
                end.put("timeZone", this.endTimeZone);
            result.put("end", end);
        }

        if (this.attendees != null) {
            JSONArray jsonAttendees = new JSONArray();
            if (this.attendees != null && this.attendees.size() > 0) {
                for (int i = 0; i < this.attendees.size(); i++) {
                    GoogleCalendarEventAttendee att = (GoogleCalendarEventAttendee) this.attendees.get(i);
                    JSONObject attendee = new JSONObject();
                    attendee.put("email", att.getEmail());
                    attendee.put("id", att.getId());
                    attendee.put("displayName", att.getDisplayName());

                    jsonAttendees.put(attendee);
                }
            }
            result.put("attendees", jsonAttendees);
        }

        if (this.reminders != null) {
            JSONObject jsonReminders = new JSONObject();
            JSONArray jsonOverrides = new JSONArray();
            if (this.reminders != null && this.reminders.size() > 0) {
                jsonReminders.put("useDefault", Boolean.FALSE);
                for (int i = 0; i < this.reminders.size(); i++) {
                    GoogleCalendarEventReminder rem = (GoogleCalendarEventReminder) this.reminders.get(i);
                    JSONObject override = new JSONObject();
                    override.put("method", rem.getMethod());
                    override.put("minutes", rem.getMinutes());

                    jsonOverrides.put(override);
                }
            }
            jsonReminders.put("overrides", jsonOverrides);
            result.put("reminders", jsonReminders);
        }

        return result;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public GregorianCalendar getCreated() {
        return created;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public Date getStartDateTime() {
        return this.startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;

    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List getAttendees() {
        return attendees;
    }

    public List getReminders() {
        return reminders;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public String getStartTimeZone() {
        return startTimeZone;
    }

    public String getEndTimeZone() {
        return endTimeZone;
    }

    public Boolean getAllDay() {
        return allDay;
    }

}
