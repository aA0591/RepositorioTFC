package com.webratio.units.store.social;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXConstants;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.AbstractOAuthableApplicationService;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.application.IUser;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.HttpResponseReader;
import com.webratio.units.store.commons.auth.IAuthManager;
import com.webratio.units.store.commons.auth.InvalidAccessTokenException;
import com.webratio.units.store.commons.auth.OAuth2Manager;
import com.webratio.units.store.commons.auth.OAuth2StatelessManager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

/**
 * The social network service implementation for Facebook.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FacebookService extends AbstractOAuthableApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.FACEBOOK_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.FACEBOOK_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL = "https://www.facebook.com/dialog/oauth";
    private static final String ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";
    private static final String SIGN_OUT_URL = "https://graph.facebook.com/me/permissions";

    /* API URLs */
    private static final String SEARCH_CONTACTS_URL = "https://graph.facebook.com/v2.3/search[q=${keywords}|type=user]";
    private static final String[] CONTACT_FIELDS = { "id", "name", "picture" };
    private static final String CONTACT_DETAILS_FIELDS = "id,name,link,location,first_name,last_name,gender,picture";

    /* OAuth API keys */
    private final String consumerKey;
    private final String consumerSecret;

    /* Authorizations */
    private final String scopeUrlPart;

    private IUser user;

    public IUser getUser() {
        return user;
    }

    public FacebookService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);

        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        String authorizationsStr = StringUtils.defaultString((String) serviceDataProviderInfo.get("authorizations"));
        Set authorizations = new HashSet(Arrays.asList(StringUtils.split(authorizationsStr, "|")));
        authorizations.remove("");
        if (authorizations.isEmpty()) {
            this.scopeUrlPart = "";
        } else {
            // In the following line the needed permissions are configured
            this.scopeUrlPart = "&scope=" + StringUtils.join(authorizations, ",");
        }
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));
        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for the 'Facebook' social network data provider")
                    .addHint("Set the API Key for the 'Facebook' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'Facebook' social network data provider")
                    .addHint("Set the Secret Key for the 'Facebook' social network data provider");
        }
    }

    protected IAuthManager locateOAuthManager(Map localContext, Map sessionContext) throws RTXException {
        String accessToken = BeanHelper.asString(localContext.get(localContext.get("unitId") + ".accessToken"));

        IAuthManager authMgr;

        if (!((accessToken == null) || ("".equals(accessToken)))) {
            authMgr = new OAuth2StatelessManager(accessToken);
        } else {
            authMgr = (IAuthManager) sessionContext.get(OAUTH_MANAGER_KEY);
            if (authMgr == null) {
                authMgr = new OAuth2Manager(REQ_TOKEN_URL, ACCESS_TOKEN_URL, consumerKey, consumerSecret, (ISocialNetworkService) this);
            }
        }
        sessionContext.put(OAUTH_MANAGER_KEY, authMgr);
        return authMgr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "Facebook";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getHomePage()
     */
    public String getHomePage() {
        return "http://www.facebook.com";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#init (com.webratio.units.store.commons.auth.AccessToken,
     * java.util.Map, java.util.Map)
     */
    public void init(AccessToken accessToken, Map localContext, Map sessionContext) throws RTXException {
        locateOAuthManager(localContext, sessionContext).setAccessToken(accessToken);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# computeAuthorizationUrl(java.lang.String, java.util.Map,
     * java.util.Map)
     */
    public String computeAuthorizationUrl(String callbackUrl, Map localContext, Map sessionContext) throws RTXException {
        try {
            if (callbackUrl.indexOf("?") > 0) {
                callbackUrl += "&";
            } else {
                callbackUrl += "?";
            }
            callbackUrl += "sn=" + getId();
            return locateOAuthManager(localContext, sessionContext).getAuthorizationUrl(callbackUrl) + scopeUrlPart;
        } catch (Exception e) {
            throw new RTXException("Unable to compute the authorization url", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# getAccessToken(java.util.Map, java.util.Map)
     */
    public AccessToken getAccessToken(Map localContext, Map sessionContext) throws RTXException {
        try {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            HttpServletRequest request = (HttpServletRequest) localContext.get(RTXConstants.HTTP_SERVLET_REQUEST_KEY);
            if (getId().equals(request.getParameter("sn"))) {
                if (!authMgr.authorize(localContext, sessionContext)) {
                    return null;
                }
            }
            return authMgr.getAccessToken();
        } catch (Exception e) {
            throw new RTXException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#logout (java.util.Map, java.util.Map)
     */
    public void logout(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        try {
            if (isAuthorized(localContext, sessionContext)) {
                new ServiceConsumer(SIGN_OUT_URL, authMgr).doDelete();
            }
        } finally {
            authMgr.setAccessToken(null);
            sessionContext.remove(CURRENT_USER_KEY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#isAuthorized (java.util.Map, java.util.Map)
     */
    public boolean isAuthorized(Map localContext, Map sessionContext) throws RTXException {
        return locateOAuthManager(localContext, sessionContext).isAuthorized();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getUser(java .util.Map, java.util.Map)
     */
    public IUser getUser(Map localContext, Map sessionContext) throws RTXException {
        IUser user = (IUser) sessionContext.get(CURRENT_USER_KEY);
        if (user == null) {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            if (authMgr.isAuthorized()) {
                ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl("me"), authMgr);
                HttpResponse response = serviceConsumer.doGet();
                if (response != null) {
                    user = new FacebookUser(HttpResponseReader.getJSonObject(response), this);
                    sessionContext.put(CURRENT_USER_KEY, user);
                }
            }
        }

        this.user = user;

        return user;
    }

    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        List contacts = new ArrayList();

        String searchUrl = SEARCH_CONTACTS_URL;
        searchUrl = StringUtils.replace(searchUrl, "${keywords}", keywords);
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        JSONArray data = getCollectionData(searchUrl, null, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                contacts.add(new FacebookContact(data.getJSONObject(i), this));
            }
        }

        return contacts;
    }

    public List searchContacts(String keywords, int limit, int offset, Map localContext, Map sessionContext) throws RTXException {
        List contacts = new ArrayList();
        Map parameters = new HashMap();
        parameters.put("limit", new Integer(limit));
        parameters.put("offset", new Integer(offset));
        String searchUrl = SEARCH_CONTACTS_URL;
        searchUrl = StringUtils.replace(searchUrl, "${KEYWORDS}", keywords);
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        System.out.println(searchUrl);
        JSONArray data = getCollectionData(searchUrl, parameters, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                contacts.add(new FacebookContact(data.getJSONObject(i), this));
            }
        }

        return contacts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# readAccessToken(org.apache.http.HttpResponse)
     */
    public AccessToken readAccessToken(HttpResponse response) {
        if (response == null) {
            return null;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            getLog().error("Invalid empty response");
            return null;
        }
        AccessToken accessToken = null;
        try {
            String accessTokenString = HttpResponseReader.getContentAsString(response).trim();
            String expiresParam = "&expires=";
            int expires = -1;
            int expiresPosition = accessTokenString.lastIndexOf(expiresParam);
            if (expiresPosition > 0) {
                expires = NumberUtils.toInt(accessTokenString.substring(expiresPosition + expiresParam.length()).trim());
                accessTokenString = accessTokenString.substring(0, expiresPosition);
            }
            accessTokenString = StringUtils.substringAfter(accessTokenString, "access_token=");
            accessToken = new AccessToken(this, accessTokenString);
            accessToken.setExpires(expires);
        } catch (Exception e) {
            logError("Unable to parse access token response", e);
        }
        return accessToken;
    }

    /**
     * Post to the current user wall
     * 
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost postToCurrentUserWall(String message, Map localContext, Map sessionContext) throws RTXException {
        // Use the identifier "me" to refer to the current user
        return postToWall("me", message, localContext, sessionContext);
    }

    /**
     * Post a note
     * 
     * @param subject
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookNote postNote(String subject, String message, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl("me", FacebookConnectionType.NOTES), authMgr);
        Map payload = new HashMap();
        payload.put("subject", subject);
        payload.put("message", message);
        HttpResponse response = serviceConsumer.doPost(payload);
        FacebookNote note = new FacebookNote(HttpResponseReader.getJSonObject(response));
        return note;
    }

    /**
     * Post on a friend Wall
     * 
     * @param friendId
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost postToFriend(String friendId, String message, Map localContext, Map sessionContext) throws RTXException {
        return postToWall(friendId, message, localContext, sessionContext);
    }

    /**
     * Add a comment to the Facebook Object (Album, Photo, Post, etc.)
     * 
     * @param message
     * @param objectId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookComment addComment(String message, String objectId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl(objectId, FacebookConnectionType.COMMENTS),
                authMgr);
        Map payload = new HashMap();
        payload.put("message", message);
        HttpResponse response = serviceConsumer.doPost(payload);
        FacebookComment comment;
        try {
            comment = new FacebookComment(HttpResponseReader.getJSonObject(response));
        } catch (ParseException e) {
            throw new RTXException(e);
        }
        return comment;
    }

    /**
     * Get information about the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookContact getCurrentUserInfo(Map localContext, Map sessionContext) throws RTXException {
        return getUserInfo("me", localContext, sessionContext);
    }

    /**
     * Get information about the identified user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookContact getUserInfo(String userId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(userId, null, localContext, sessionContext);

        if (jsonObject != null) {
            return new FacebookContact(jsonObject, this);
        }
        return null;

    }

    /**
     * Create an event
     * 
     * @param eventName
     * @param start
     * @param end
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     * @throws ParseException
     */
    public FacebookEvent createEvent(String eventName, Timestamp start, Timestamp end, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl("me", FacebookConnectionType.EVENTS), authMgr);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        Map payload = new HashMap();
        payload.put("name", eventName);
        payload.put("start_time", df.format(start));
        payload.put("end_time", df.format(end));
        HttpResponse response = serviceConsumer.doPost(payload);
        FacebookEvent event = new FacebookEvent(HttpResponseReader.getJSonObject(response));

        return event;
    }

    /**
     * Invite a list of users to a Identified event
     * 
     * @param eventId
     * @param userIds
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void inviteToEvent(String eventId, String[] userIds, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl(eventId, FacebookConnectionType.INVITED),
                authMgr);
        Map payload = new HashMap();
        payload.put("users", StringUtils.join(userIds, ","));

        serviceConsumer.doPost(payload);
    }

    /**
     * Get the Groups of a identified user
     * 
     * @param userId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getGroups(String userId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        List groupsList = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (StringUtils.isEmpty(userId)) {
            userId = "me";
        }

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));

        JSONArray data = getCollectionData(userId, FacebookConnectionType.GROUPS, payload, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                FacebookGroup post;

                post = new FacebookGroup(data.getJSONObject(i));

                groupsList.add(post);
            }
        }
        return groupsList;
    }

    /**
     * Upload a photo
     * 
     * @param userId
     * @param photoBlob
     * @param filename
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String uploadPhoto(String userId, RTXBLOBData photoBlob, String filename, String message, Map localContext,
            Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (StringUtils.isEmpty(userId)) {
            userId = "me";
        }

        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl(userId, FacebookConnectionType.PHOTOS),
                authMgr);
        Map payload = new HashMap();
        payload.put("message", message);
        HttpResponse response = serviceConsumer.send(photoBlob, payload, HttpPost.METHOD_NAME, "source");

        JSONObject jsonPhoto = HttpResponseReader.getJSonObject(response);

        return jsonPhoto.optString("id");
    }

    /**
     * Add tag to a photo
     * 
     * @param photoId
     * @param tag
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void addTag(String photoId, String tag, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl(photoId, FacebookConnectionType.TAGS),
                authMgr);
        Map payload = new HashMap();
        payload.put("tag_text", tag);
        serviceConsumer.doPost(payload);
    }

    /**
     * Get the list of Comments for a Facebook Object (Photo, Album, Post, etc.)
     * 
     * @param objectId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getComments(String objectId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        List commentsList = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));

        JSONArray data = getCollectionData(objectId, FacebookConnectionType.COMMENTS, payload, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                FacebookComment comment;
                try {
                    comment = new FacebookComment(data.getJSONObject(i));
                } catch (NoSuchElementException e) {
                    throw new RTXException(e);
                } catch (ParseException e) {
                    throw new RTXException(e);
                }
                commentsList.add(comment);
            }
        }
        return commentsList;
    }

    /**
     * Get the list of Messages for a Facebook Thread
     * 
     * @param objectId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getMessages(String threadId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        List messagesList = new ArrayList();

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));

        JSONObject thread = getElementById(threadId, payload, localContext, sessionContext);
        JSONArray data = null;
        if (thread != null) {
            JSONObject comments = thread.optJSONObject("comments");
            if (comments != null) {
                data = comments.getJSONArray("data");
            }
        }
        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                FacebookMessage message;
                try {
                    message = new FacebookMessage(data.getJSONObject(i));
                } catch (NoSuchElementException e) {
                    throw new RTXException(e);
                }
                messagesList.add(message);
            }
        }
        return messagesList;
    }

    /**
     * Get the wall posts of the current user
     * 
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserWallPosts(int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getPostsFeed("me", offset, limit, localContext, sessionContext);
    }

    /**
     * Get the wall posts of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserWallPosts(Map localContext, Map sessionContext) throws RTXException {
        return getPostsFeed("me", localContext, sessionContext);
    }

    /**
     * Get a list of Users friends with the identified user
     * 
     * @param userId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFriends(String userId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        List friendsList = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (StringUtils.isEmpty(userId)) {
            userId = "me";
        }

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));
        payload.put("fields", CONTACT_DETAILS_FIELDS);

        JSONArray data = getCollectionData(FacebookURLHelper.createUrl(userId, FacebookConnectionType.FRIENDS), payload, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                FacebookContact contact = new FacebookContact(data.getJSONObject(i), this);
                friendsList.add(contact);
            }
        }

        return friendsList;

    }

    /**
     * Get a Facebook Post by Id
     * 
     * @param postId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost getPost(String postId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(postId, null, localContext, sessionContext);
        if (jsonObject != null) {
            return new FacebookPost(jsonObject);
        }

        return null;
    }

    /**
     * Like a Facebook object (Album, Photo, Comment, etc.)
     * 
     * @param objectId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public Boolean like(String objectId, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String url = FacebookURLHelper.createUrl(objectId, FacebookConnectionType.LIKES);

        ServiceConsumer consumer = getServiceConsumer(url, authMgr);

        HttpResponse response = consumer.doPost();
        Boolean result;

        try {
            result = Boolean.valueOf(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new RTXException("Unable to parse response from server", e);
        }

        return result;

    }

    /**
     * Unlike a object (Comment, Photo, Album, etc.)
     * 
     * @param objectId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public Boolean unlike(String objectId, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String url = FacebookURLHelper.createUrl(objectId, FacebookConnectionType.LIKES);

        ServiceConsumer consumer = getServiceConsumer(url, authMgr);

        HttpResponse response = consumer.doDelete();

        Boolean result;

        try {
            result = Boolean.valueOf(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new RTXException("Unable to parse response from server", e);
        }

        return result;
    }

    /**
     * Get the list of albums of a determined Facebook user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserAlbums(String userId, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        List albums = new ArrayList();

        String[] fields = { "id", "name", "from", "cover_photo", "link" };

        JSONArray data = getCollectionData(FacebookURLHelper.createUrl(userId, FacebookConnectionType.ALBUMS, fields), null, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                albums.add(new FacebookAlbum(data.getJSONObject(i), this));
            }
        }

        return albums;
    }

    /**
     * Get the list of albums of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserAlbums(Map localContext, Map sessionContext) throws RTXException {
        return getUserAlbums("me", localContext, sessionContext);
    }

    /**
     * Get an Album by its Id
     * 
     * @param albumId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookAlbum getAlbumById(String albumId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(albumId, null, localContext, sessionContext);
        if (jsonObject != null)
            return new FacebookAlbum(jsonObject, this);
        else
            return null;
    }

    /**
     * Get the list of photos of a Facebook Album
     * 
     * @param albumId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getAlbumPhotos(String albumId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List photos = new ArrayList();

        String[] fields = { "id", "name", "picture", "link" };

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));

        JSONArray data = getCollectionData(FacebookURLHelper.createUrl(albumId, FacebookConnectionType.PHOTOS, fields), payload,
                authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                photos.add(new FacebookPhoto(data.getJSONObject(i), this));
            }
        }

        return photos;
    }

    /**
     * Get a photo by Id
     * 
     * @param photoId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPhoto getPhotoById(String photoId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(photoId, null, localContext, sessionContext);

        if (jsonObject != null)
            return new FacebookPhoto(jsonObject, this);
        else
            return null;

    }

    /**
     * Get events of a determined Facebook user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserEvents(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        List events = new ArrayList();

        String[] fields = { "owner", "picture", "start_time", "end_time", "location", "name" };

        JSONArray data = getCollectionData(FacebookURLHelper.createUrl(userId, FacebookConnectionType.EVENTS, fields), null, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                events.add(new FacebookEvent(data.getJSONObject(i), this));
            }
        }
        return events;
    }

    /**
     * Get the events of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserEvents(Map localContext, Map sessionContext) throws RTXException {
        return getUserEvents("me", localContext, sessionContext);
    }

    /**
     * Get an event by Id
     * 
     * @param eventId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookEvent getEventById(String eventId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(eventId, null, localContext, sessionContext);

        if (jsonObject != null)
            return new FacebookEvent(jsonObject, this);
        else
            return null;

    }

    public List getEventInvited(String eventId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getEventUsers(eventId, offset, limit, FacebookConnectionType.INVITED, localContext, sessionContext);
    }

    public List getEventMaybe(String eventId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getEventUsers(eventId, offset, limit, FacebookConnectionType.MAYBE, localContext, sessionContext);
    }

    public List getEventAttending(String eventId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getEventUsers(eventId, offset, limit, FacebookConnectionType.ATTENDING, localContext, sessionContext);
    }

    public List getEventDeclined(String eventId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getEventUsers(eventId, offset, limit, FacebookConnectionType.DECLINED, localContext, sessionContext);
    }

    public List getEventNonRepliers(String eventId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getEventUsers(eventId, offset, limit, FacebookConnectionType.NOREPLY, localContext, sessionContext);
    }

    /**
     * Respond to an event (rsvp_status)
     * 
     * @param eventId
     * @param eventResponse
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public Boolean respondToEvent(String eventId, String eventResponse, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        FacebookConnectionType connectionType = FacebookConnectionType.fromValue(eventResponse);
        ServiceConsumer consumer = getServiceConsumer(FacebookURLHelper.createUrl(eventId, connectionType), authMgr);
        HttpResponse response = consumer.doPost();
        try {
            Boolean result = Boolean.valueOf(EntityUtils.toString(response.getEntity()));
            return result;
        } catch (IOException e) {
            throw new RTXException("Unable to parse response from server", e);
        }
    }

    /**
     * Post to a Facebook Event wall
     * 
     * @param eventId
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost postToEventWall(String eventId, String message, Map localContext, Map sessionContext) throws RTXException {
        return postToWall(eventId, message, localContext, sessionContext);
    }

    public List getCurrentUserMessageThreads(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List messages = new ArrayList();

        JSONArray data = getCollectionData("me", FacebookConnectionType.INBOX, null, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                messages.add(new FacebookThread(data.getJSONObject(i)));
            }
        }

        return messages;
    }

    /**
     * Get a group by Id
     * 
     * @param groupId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookGroup getGroupById(String groupId, Map localContext, Map sessionContext) throws RTXException {

        JSONObject jsonObject = getElementById(groupId, null, localContext, sessionContext);
        return new FacebookGroup(jsonObject);

    }

    /**
     * Post to a group wall
     * 
     * @param groupId
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost postToGroupWall(String groupId, String message, Map localContext, Map sessionContext) throws RTXException {
        return postToWall(groupId, message, localContext, sessionContext);
    }

    /**
     * Get the list of members of a group
     * 
     * @param groupId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getGroupMembers(String groupId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List members = new ArrayList();

        Map parameters = new HashMap();
        parameters.put("offset", new Integer(offset));
        parameters.put("limit", new Integer(limit));

        JSONArray data = getCollectionData(groupId, FacebookConnectionType.MEMBERS, parameters, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                members.add(new FacebookContact(data.getJSONObject(i), this));
            }
        }

        return members;

    }

    /**
     * Get mutual friends between the current user and the target user
     * 
     * @param targetUserId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getMutualFriends(String targetUserId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        return getMutualFriends("me", targetUserId, offset, limit, localContext, sessionContext);
    }

    /**
     * Get a list of the mutual friends between two specified users
     * 
     * @param userId
     * @param targetUserId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getMutualFriends(String userId, String targetUserId, int offset, int limit, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List mutualFriends = new ArrayList();

        Map parameters = new HashMap();
        parameters.put("offset", new Integer(offset));
        parameters.put("limit", new Integer(limit));

        JSONArray data = getCollectionData(
                FacebookURLHelper.createUrl(userId, targetUserId, FacebookConnectionType.MUTUALFRIENDS, CONTACT_FIELDS), parameters,
                authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                mutualFriends.add(new FacebookContact(data.getJSONObject(i), this));
            }
        }
        return mutualFriends;
    }

    /**
     * Get the posts in a Facebook Object wall (User, Album, Group, etc.)
     * 
     * @param objectId
     * @param offset
     * @param limit
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getPostsFeed(String objectId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {

        Map payload = new HashMap();
        payload.put("limit", new Integer(limit));
        payload.put("offset", new Integer(offset));

        return getPostsFeed(objectId, payload, localContext, sessionContext);
    }

    /**
     * Get the posts in a Facebook Object wall (User, Album, Group, etc.)
     * 
     * @param objectId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getPostsFeed(String objectId, Map localContext, Map sessionContext) throws RTXException {
        return getPostsFeed(objectId, null, localContext, sessionContext);
    }

    /**
     * Get the posts in a Facebook Object wall (User, Album, Group, etc.)
     * 
     * @param objectId
     * @param parameters
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getPostsFeed(String objectId, Map parameters, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List posts = new ArrayList();

        JSONArray data = getCollectionData(objectId, FacebookConnectionType.FEED, parameters, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                posts.add(new FacebookPost(data.getJSONObject(i)));
            }
        }

        return posts;
    }

    /**
     * Get the statuses in a Facebook Object wall
     * 
     * @param objectId
     * @param parameters
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserStatuses(String objectId, int offset, int limit, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List posts = new ArrayList();
        Map parameters = new HashMap();
        parameters.put("limit", new Integer(limit));
        parameters.put("offset", new Integer(offset));

        JSONArray data = getCollectionData(objectId, FacebookConnectionType.STATUSES, parameters, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                posts.add(new FacebookPost(data.getJSONObject(i)));
            }
        }

        return posts;
    }

    /**
     * Post to a Facebook object wall
     * 
     * @param objectId
     * @param message
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public FacebookPost postToWall(String objectId, String message, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer serviceConsumer = getServiceConsumer(FacebookURLHelper.createUrl(objectId, FacebookConnectionType.FEED),
                authMgr);
        Map payload = new HashMap();
        payload.put("message", message);
        HttpResponse response = serviceConsumer.doPost(payload);

        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);

        return new FacebookPost(jsonObject.optString("id"));
    }

    /**
     * Get a Facebook event Users according to response to the event
     * 
     * @param eventId
     * @param offset
     * @param limit
     * @param connection
     *            Response to event (attending, maybe, declined, invited,noreply)
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    private List getEventUsers(String eventId, int offset, int limit, FacebookConnectionType connection, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List eventUsers = new ArrayList();

        Map parameters = new HashMap();
        parameters.put("offset", new Integer(offset));
        parameters.put("limit", new Integer(limit));
        JSONArray data = getCollectionData(FacebookURLHelper.createUrl(eventId, connection, CONTACT_FIELDS), parameters, authMgr);

        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                eventUsers.add(new FacebookContact(data.getJSONObject(i), this));
            }
        }

        return eventUsers;
    }

    /**
     * Retrieve a Facebook Object in JSON format by its Id
     * 
     * @param elementId
     * @param parameters
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    private JSONObject getElementById(String elementId, Map parameters, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        String url = FacebookURLHelper.createUrl(elementId);

        ServiceConsumer consumer = getServiceConsumer(url, authMgr);
        HttpResponse response;

        if (parameters != null)
            response = consumer.doGet(parameters);
        else
            response = consumer.doGet();

        return HttpResponseReader.getJSonObject(response);

    }

    /**
     * Get a specified collection from a Facebook object, like posts, albums, photos, etc.
     * 
     * @param objectId
     * @param collection
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private JSONArray getCollectionData(String objectId, FacebookConnectionType collection, Map parameters, IAuthManager authMgr)
            throws RTXException {
        String url = FacebookURLHelper.createUrl(objectId, collection);
        return getCollectionData(url, parameters, authMgr);
    }

    private JSONArray getCollectionData(String url, Map parameters, IAuthManager authMgr) throws RTXException {

        HttpResponse response;
        ServiceConsumer consumer = getServiceConsumer(url, authMgr);

        if (parameters != null)
            response = consumer.doGet(parameters);
        else
            response = consumer.doGet();

        JSONObject result = HttpResponseReader.getJSonObject(response);
        if (result != null)
            return result.getJSONArray("data");

        return null;

    }

    private ServiceConsumer getServiceConsumer(String endpointURL, IAuthManager authManager) throws RTXException {
        return new ServiceConsumer(endpointURL, authManager) {

            protected void checkError(HttpResponse response) throws RTXException {
                if (response == null) {
                    throw new RTXException("Null response from service");
                }
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_BAD_REQUEST) {
                    JSONObject jsonResponse = HttpResponseReader.getJSonObject(response);
                    if (jsonResponse.has("error")) {
                        JSONObject error = jsonResponse.getJSONObject("error");
                        if (error.has("code")) {
                            int code = error.getInt("code");
                            if (code == 2500 || code == 190 || code == 102 || code == 458 || code == 463 || code == 467) {
                                throw new InvalidAccessTokenException(error.getString("message"));
                            }
                        }
                    }
                }
                super.checkError(response);
            }

        };
    }
}
