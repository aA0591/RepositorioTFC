package com.webratio.units.store.social;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.AbstractApplicationService;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.application.IUser;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.HttpResponseReader;
import com.webratio.units.store.commons.auth.IAuthManager;
import com.webratio.units.store.commons.auth.OAuth2Manager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GoogleCalendarService extends AbstractApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.GOOGLECALENDAR_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.GOOGLECALENDAR_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL = "https://accounts.google.com/o/oauth2/auth[response_type=code|scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar]";
    private static final String ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token[grant_type=authorization_code]";

    /* API URLs */
    private static final String BASE_URL = "https://www.googleapis.com/calendar/v3/";
    private static final String USER_PROFILE_URL = "https://www.googleapis.com/oauth2/v1/userinfo[access_token=${ACCESS_TOKEN}]";
    private static final String CALENDAR_LIST_URL = BASE_URL + "users/me/calendarList";
    private static final String CALENDARS_URL = BASE_URL + "calendars";
    private static final String EVENTS_URL = CALENDARS_URL + "/${calendarId}/events";

    /* OAuth API keys */
    private final String consumerKey;
    private final String consumerSecret;

    public GoogleCalendarService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);
        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));

        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for 'GoogleCalendar' the social network data provider")
                    .addHint("Set the API Key for the 'GoogleCalendar' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'GoogleCalendar' social network data provider")
                    .addHint("Set the Secret Key for the 'GoogleCalendar' social network data provider");
        }
    }

    public String computeAuthorizationUrl(String callbackUrl, Map localContext, Map sessionContext) throws RTXException {
        try {
            return locateOAuthManager(localContext, sessionContext).getAuthorizationUrl(callbackUrl);
        } catch (Exception e) {
            throw new RTXException("Unable to compute the authorization url", e);
        }
    }

    public AccessToken getAccessToken(Map localContext, Map sessionContext) throws RTXException {
        try {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            if (!authMgr.authorize(localContext, sessionContext)) {
                return null;
            }
            return authMgr.getAccessToken();

        } catch (Exception e) {
            throw new RTXException(e);
        }
    }

    public boolean isAuthorized(Map localContext, Map sessionContext) throws RTXException {
        return locateOAuthManager(localContext, sessionContext).isAuthorized();
    }

    public void init(AccessToken accessToken, Map localContext, Map sessionContext) throws RTXException {
        locateOAuthManager(localContext, sessionContext).setAccessToken(accessToken);

    }

    public void logout(Map localContext, Map sessionContext) throws RTXException {
        locateOAuthManager(localContext, sessionContext).setAccessToken(null);
        sessionContext.remove(CURRENT_USER_KEY);

    }

    public AccessToken readAccessToken(HttpResponse response) {
        if (response == null) {
            return null;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            getLog().error("Invalid empty response");
            return null;
        }
        String contentType = entity.getContentType().getValue();
        if (contentType == null || contentType.indexOf("application/json") == -1) {
            getLog().error("Invalid response content type '" + contentType + "'");
            return null;
        }
        AccessToken accessToken = null;
        try {
            JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
            accessToken = new AccessToken(this, jsonObject.getString("access_token"));
            accessToken.setExpires(jsonObject.getInt("expires_in"));
        } catch (Exception e) {
            logError("Unable to parse access token response", e);
        }
        return accessToken;
    }

    public String getHomePage() {
        return "http://www.calendar.google.com";

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "GoogleCalendar";
    }

    public IUser getUser(Map localContext, Map sessionContext) throws RTXException {
        IUser user = (IUser) sessionContext.get(CURRENT_USER_KEY);
        if (user == null) {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            if (authMgr.isAuthorized()) {
                String accessToken = authMgr.getAccessToken().getValue();
                String userProfileUrl = StringUtils.replace(USER_PROFILE_URL, "${ACCESS_TOKEN}", accessToken);
                ServiceConsumer serviceConsumer = new ServiceConsumer(userProfileUrl, authMgr);
                HttpResponse response = serviceConsumer.doGet();
                if (response != null) {
                    // user = new
                    // GoogleDocsUser(HttpResponseReader.getJSonObject(response),
                    // this);
                    sessionContext.put(CURRENT_USER_KEY, user);
                }
            }
        }
        return user;
    }

    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        // TODO Auto-generated method stub
        return null;
    }

    private IAuthManager locateOAuthManager(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = (IAuthManager) sessionContext.get(OAUTH_MANAGER_KEY);
        if (authMgr == null) {
            authMgr = new OAuth2Manager(REQ_TOKEN_URL, ACCESS_TOKEN_URL, consumerKey, consumerSecret, this);
            sessionContext.put(OAUTH_MANAGER_KEY, authMgr);
        }
        return authMgr;
    }

    /**
     * Get the calendars of the current user
     * 
     * @param pageToken
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleCalendarList getUserCalendars(String pageToken, Integer maxResults, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List calendars = new ArrayList();

        if (authMgr.isAuthorized()) {

            Map parameters = new HashMap();
            if (pageToken != null)
                parameters.put("pageToken", pageToken);
            if (maxResults != null)
                parameters.put("maxResults", maxResults);

            ServiceConsumer consumer = new ServiceConsumer(CALENDAR_LIST_URL, authMgr);
            HttpResponse response;
            response = consumer.doGet(parameters);

            JSONObject result = HttpResponseReader.getJSonObject(response);
            String nextPageToken = result.optString("nextPageToken");
            JSONArray data = result.getJSONArray("items");

            if (data != null && data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    calendars.add(new GoogleCalendar(data.getJSONObject(i)));
                }
            }

            return new GoogleCalendarList(nextPageToken, calendars);

        } else {
            throw new RTXException("Not yet authorized!");
        }

    }

    /**
     * Get the events on the specified calendar
     * 
     * @param calendarId
     * @param pageToken
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleCalendarList getCalendarEvents(String calendarId, String pageToken, Integer maxResults, Map localContext,
            Map sessionContext) throws RTXException {
        return getCalendarEvents(calendarId, pageToken, maxResults, null, null, localContext, sessionContext);
    }

    /**
     * Get the events on the specified calendar
     * 
     * @param calendarId
     * @param pageToken
     * @param maxResults
     * @param timemin
     * @param timemax
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleCalendarList getCalendarEvents(String calendarId, String pageToken, Integer maxResults, Date timemin, Date timemax,
            Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List resultEvents = new ArrayList();

        if (authMgr.isAuthorized()) {
            String url = StringUtils.replace(EVENTS_URL, "${calendarId}", calendarId);
            Map parameters = new HashMap();
            if (pageToken != null)
                parameters.put("pageToken", pageToken);
            if (maxResults != null)
                parameters.put("maxResults", maxResults);
            if (timemin != null)
                parameters.put("timeMin", timemin);
            if (timemax != null)
                parameters.put("timeMax", timemax);

            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = consumer.doGet();

            JSONObject result = HttpResponseReader.getJSonObject(response);
            String nextPageToken = result.optString("nextPageToken");

            JSONArray data = result.getJSONArray("items");
            try {
                if (data != null && data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        resultEvents.add(new GoogleCalendarEvent(data.getJSONObject(i)));

                    }
                }
            } catch (ParseException e) {
                throw new RTXException("Unable to parse JSON", e);
            }

            return new GoogleCalendarList(nextPageToken, resultEvents);
        }

        return null;

    }

    /**
     * Create an event on the specified calendar
     * 
     * @param calendarId
     * @param summary
     * @param description
     * @param location
     * @param remindersList
     * @param attendeesEmails
     * @param startDateTime
     * @param startDate
     * @param startTimeZone
     * @param endDateTime
     * @param endDate
     * @param endTimeZone
     * @param organizerName
     * @param organizerId
     * @param allDay
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String createEvent(String calendarId, String summary, String description, String location, List remindersList,
            List attendeesEmails, Date startDateTime, Date startDate, String startTimeZone, Date endDateTime, Date endDate,
            String endTimeZone, String organizerName, String organizerId, Boolean allDay, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {

            String url = StringUtils.replace(EVENTS_URL, "${calendarId}", calendarId);
            GoogleCalendarEvent event = new GoogleCalendarEvent(summary, location, description, organizerName, organizerId, allDay,
                    startDateTime, startDate, startTimeZone, endDateTime, endDate, endTimeZone,
                    getAttendeesFromEmails(attendeesEmails), remindersList);

            JSONObject document = event.getJSONObject();

            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = consumer.send(document, HttpPost.METHOD_NAME);

            JSONObject result = HttpResponseReader.getJSonObject(response);
            GoogleCalendarEvent resultEvent;
            try {
                resultEvent = new GoogleCalendarEvent(result);
            } catch (ParseException e) {
                throw new RTXException("Unable to parse JSON", e);
            }
            return resultEvent.getId();
        } else {
            throw new RTXException("Not yet authorized!");
        }
    }

    /**
     * Deletes a specified event, in a specified calendar
     * 
     * @param eventId
     * @param calendarId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void deleteEvent(String eventId, String calendarId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {
            String url = StringUtils.replace(EVENTS_URL, "${calendarId}", calendarId);
            url += "/" + eventId;
            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            consumer.doDelete();

        } else {
            throw new RTXException("Not yet authorized!");
        }
    }

    /**
     * Get a list of the attendants to the specified event
     * 
     * @param eventId
     * @param calendarId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getEventAttendees(String eventId, String calendarId, Map localContext, Map sessionContext) throws RTXException {
        GoogleCalendarEvent event = getEventById(calendarId, eventId, localContext, sessionContext);
        return event.getAttendees();
    }

    /**
     * Create a new Calendar
     * 
     * @param summary
     * @param description
     * @param location
     * @param timeZone
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String createCalendar(String summary, String description, String location, String timeZone, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {
            ServiceConsumer consumer = new ServiceConsumer(CALENDARS_URL, authMgr);
            GoogleCalendar calendar = new GoogleCalendar(summary, location, timeZone, description);
            JSONObject document = calendar.getJSONObject();

            HttpResponse response = consumer.send(document, HttpPost.METHOD_NAME);
            JSONObject result = HttpResponseReader.getJSonObject(response);
            GoogleCalendar resultCalendar = new GoogleCalendar(result);
            return resultCalendar.getId();

        } else {
            throw new RTXException("Not yet authorized!");
        }

    }

    /**
     * Delete the specified calendar
     * 
     * @param calendarId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void deleteCalendar(String calendarId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized()) {
            String url = CALENDARS_URL + "/" + calendarId;
            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            consumer.doDelete();

        } else {
            throw new RTXException("Not yet authorized!");
        }

    }

    /**
     * Get a calendar details
     * 
     * @param calendarId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleCalendar getCalendarById(String calendarId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized()) {
            String url = CALENDARS_URL + "/" + calendarId;
            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = consumer.doGet();
            JSONObject result = HttpResponseReader.getJSonObject(response);

            return new GoogleCalendar(result);

        } else {
            throw new RTXException("Not yet authorized!");
        }
    }

    /**
     * Update the specified calendar
     * 
     * @param calendarId
     * @param calendar
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String updateCalendar(String calendarId, GoogleCalendar calendar, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {
            String url = CALENDARS_URL + "/" + calendarId;

            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            JSONObject document = calendar.getJSONObject();

            HttpResponse response = consumer.send(document, HttpPut.METHOD_NAME);
            JSONObject result = HttpResponseReader.getJSonObject(response);
            GoogleCalendar resultCal = new GoogleCalendar(result);

            return resultCal.getId();

        } else {
            throw new RTXException("Not yet authorized!");
        }
    }

    /**
     * Get an Event details
     * 
     * @param calendarId
     * @param eventId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleCalendarEvent getEventById(String calendarId, String eventId, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized()) {
            String url = EVENTS_URL;
            url = StringUtils.replace(url, "${calendarId}", calendarId);
            url += "/" + eventId;

            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = consumer.doGet();

            JSONObject result = HttpResponseReader.getJSonObject(response);
            try {
                return new GoogleCalendarEvent(result);
            } catch (ParseException e) {
                throw new RTXException("Unable to parse JSON", e);
            }

        } else {

            throw new RTXException("Not yet authorized!");
        }
    }

    /**
     * Update the specified event
     * 
     * @param calendarId
     * @param eventId
     * @param etag
     * @param event
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String updateEvent(String calendarId, String eventId, String etag, GoogleCalendarEvent event, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized()) {
            String url = StringUtils.replace(EVENTS_URL, "${calendarId}", calendarId);
            url += "/" + eventId;

            JSONObject document = event.getJSONObject();
            Map headers = null;
            if (etag != null) {
                headers = new HashMap();
                headers.put("If-Match", etag);
            }
            ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = consumer.send(document, HttpPut.METHOD_NAME, headers);
            JSONObject result = HttpResponseReader.getJSonObject(response);
            String newEtag = result.optString("etag");

            return newEtag;

        } else {
            throw new RTXException("Not yet authorized!");
        }

    }

    /**
     * Make a GET request for the specified URL and retrieve JSONArray object, specified in the 'items' value of the JSON response
     * 
     * @param url
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private JSONArray getItemsList(String url, Map parameters, IAuthManager authMgr) throws RTXException {
        ServiceConsumer consumer = new ServiceConsumer(url, authMgr);
        HttpResponse response;
        if (parameters != null)
            response = consumer.doGet(parameters);
        else
            response = consumer.doGet();

        JSONObject result = HttpResponseReader.getJSonObject(response);

        return result.getJSONArray("items");

    }

    /**
     * Return a List of objects GoogleCalendarEventAttendee from a list of emails
     * 
     * @param attendeesEmails
     * @return
     */
    private List getAttendeesFromEmails(List attendeesEmails) {
        List attendees = new ArrayList();
        for (int i = 0; i < attendeesEmails.size(); i++) {
            String email = attendeesEmails.get(i).toString();
            attendees.add(new GoogleCalendarEventAttendee(email));
        }

        return attendees;
    }

}
