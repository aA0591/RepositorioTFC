package com.webratio.units.store.social;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.webratio.rtx.RTXBLOBData;
import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.AbstractApplicationService;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.application.IUser;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.HttpResponseReader;
import com.webratio.units.store.commons.auth.IAuthManager;
import com.webratio.units.store.commons.auth.OAuth1Manager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

/**
 * The social network service implementation for Twitter.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TwitterService extends AbstractApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.TWITTER_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.TWITTER_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String AUTHORIZE_TOKEN_URL = "https://api.twitter.com/oauth/authenticate";
    private static final String SIGN_OUT_URL = "https://api.twitter.com/oauth/invalidateToken";

    /* API URLs */
    private static final String USER_PROFILE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String SEARCH_CONTACTS_URL = "https://api.twitter.com/1.1/users/search.json";
    private static final String GET_FOLLOWINGS_IDS_URL = "https://api.twitter.com/1.1/friends/ids.json";
    private static final String GET_FOLLOWINGS_URL = "https://api.twitter.com/1.1/friends/list.json";
    private static final String GET_FOLLOWERS_IDS_URL = "https://api.twitter.com/1.1/followers/ids.json";
    private static final String GET_FOLLOWERS_URL = "https://api.twitter.com/1.1/followers/list.json";
    private static final String GET_TWEETS_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
    private static final String POST_TWEET_URL = "https://api.twitter.com/1.1/statuses/update.json";
    private static final String SEND_MESSAGE_URL = "https://api.twitter.com/1.1/direct_messages/new.json";
    private static final String USER_LOOKUP_URL = "https://api.twitter.com/1.1/users/lookup.json";
    private static final String GET_MENTIONS_OF_CURRENT_USER_URL = "https://api.twitter.com/1.1/statuses/mentions_timeline.json";
    private static final String GET_HOME_OF_CURRENT_USER_URL = "https://api.twitter.com/1.1/statuses/home_timeline.json";
    private static final String RETWEET_URL = "https://api.twitter.com/1.1/statuses/retweet/{TWEET_ID}.json";
    private static final String POST_TWEET_WITH_MEDIA_URL = "https://api.twitter.com/1.1/statuses/update_with_media.json";
    private static final String GET_TWEET_BY_ID_URL = "https://api.twitter.com/1.1/statuses/show.json";
    private static final String SEARCH_TWEETS_URL = "https://api.twitter.com/1.1/search/tweets.json";
    private static final String FOLLOW_USER_URL = "https://api.twitter.com/1.1/friendships/create.json";
    private static final String UNFOLLOW_USER_URL = "https://api.twitter.com/1.1/friendships/destroy.json";
    private static final String FRIENDSHIP_LOOKUP_URL = "http://api.twitter.com/1.1/friendships/lookup.json";
    private static final String GET_OEMBEDED_TWEET_URL = "https://api.twitter.com/1.1/statuses/oembed.json";
    private static final String GET_MESSAGES_SENT_TO_CURRENT_USER_URL = "https://api.twitter.com/1.1/direct_messages.json";
    private static final String GET_MESSAGES_SENT_BY_CURRENT_USER_URL = "https://api.twitter.com/1.1/direct_messages/sent.json";
    private static final String GET_MESSAGE_BY_ID_URL = "https://api.twitter.com/1.1/direct_messages/show.json";
    private static final String GET_USER_LISTS_URL = "http://api.twitter.com/1.1/lists/list.json";
    private static final String GET_LIST_SUBSCRIBERS_URL = "https://api.twitter.com/1.1/lists/subscribers.json";
    private static final String GET_LIST_MEMBERS_URL = "https://api.twitter.com/1.1/lists/members.json";
    private static final String ADD_MEMBER_TO_LIST_URL = "https://api.twitter.com/1.1/lists/members/create.json";
    private static final String ADD_MEMBERS_TO_LIST_URL = "https://api.twitter.com/1.1/lists/members/create_all.json";
    private static final String REMOVE_MEMBER_FROM_LIST_URL = "https://api.twitter.com/1.1/lists/members/destroy.json";
    private static final String REMOVE_MEMBERS_FROM_LIST_URL = "https://api.twitter.com/1.1/lists/members/destroy_all.json";
    private static final String SUBSCRIBE_TO_LIST_URL = "https://api.twitter.com/1.1/lists/subscribers/create.json";
    private static final String UNSUBSCRIBE_FROM_LIST_URL = "https://api.twitter.com/1.1/lists/subscribers/destroy.json";
    private static final String GET_USER_SUGGESTION_CATEGORIES_URL = "http://api.twitter.com/1.1/users/suggestions.json";
    private static final String GET_USER_SUGGESTION_BY_CATEGORY_URL = "http://api.twitter.com/1.1/users/suggestions/{SLUG}.json";
    private static final String GET_BLOCKED_USERS_URL = "https://api.twitter.com/1.1/blocks/list.json";
    private static final String BLOCK_USER_URL = "https://api.twitter.com/1.1/blocks/create.json";
    private static final String UNBLOCK_USER_URL = "https://api.twitter.com/1.1/blocks/destroy.json";
    private static final String GET_FAVORITE_TWEETS_URL = "https://api.twitter.com/1.1/favorites/list.json";
    private static final String FAVORITE_STATUS_URL = "https://api.twitter.com/1.1/favorites/create.json";
    private static final String UNFAVORITE_STATUS_URL = "https://api.twitter.com/1.1/favorites/destroy.json";

    /* OAuth API keys */
    private final String consumerKey;
    private final String consumerSecret;
    private static Integer lastRateLimit = new Integer(100);

    /* auxiliar constants */
    private static final String REMAINING_RATE_LIMIT_HEADER = "X-Rate-Limit-Remaining";
    private static final String REMAINING_WINDOW_RATE_RESET = "X-Rate-Limit-Reset";

    private String currentUserId;

    private ConfigurationBuilder cb;
    private String oAuthTokenSecret;
    private String oAuthToken;

    public TwitterService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);
        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));
        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for the 'Twitter' social network data provider")
                    .addHint("Set the API Key for the 'Twitter' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'Twitter' social network data provider ")
                    .addHint("Set the Secret Key for the 'Twitter' social network data provider");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "Twitter";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getHomePage()
     */
    public String getHomePage() {
        return "http://www.twitter.com";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# computeAuthorizationUrl(java.lang.String, java.util.Map,
     * java.util.Map)
     */
    public String computeAuthorizationUrl(String callbackUrl, Map localContext, Map sessionContext) throws RTXException {
        try {
            return locateOAuthManager(localContext, sessionContext).getAuthorizationUrl(callbackUrl);
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
            if (!authMgr.authorize(localContext, sessionContext)) {
                return null;
            }
            if (sessionContext.get(CURRENT_USER_KEY) != null) {
                currentUserId = sessionContext.get(CURRENT_USER_KEY).toString();
            } else {
                ServiceConsumer consumer = new TwitterServiceConsumer(USER_PROFILE_URL, authMgr);
                HttpResponse response = consumer.doGet();
                currentUserId = HttpResponseReader.getJSonObject(response).getString("id");
            }
            String[] tokens = StringUtils.split(authMgr.getAccessToken().getValue(), "|");
            if (tokens != null && tokens.length > 0) {
                oAuthToken = tokens[0];
                oAuthTokenSecret = tokens[1];
            }
            cb = new ConfigurationBuilder().setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(oAuthToken).setOAuthAccessTokenSecret(oAuthTokenSecret);
            return authMgr.getAccessToken();
        } catch (Exception e) {
            throw new RTXException(e);
        }
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
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#logout (java.util.Map, java.util.Map)
     */
    public void logout(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        try {
            if (isAuthorized(localContext, sessionContext)) {
                new TwitterServiceConsumer(SIGN_OUT_URL, authMgr).doGet();
            }
        } finally {
            authMgr.setAccessToken(null);
            sessionContext.remove(CURRENT_USER_KEY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.ISocialNetworkService# searchContacts(java.lang.String, java.util.Map,
     * java.util.Map)
     */
    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        return searchUsers(keywords, null, null, localContext, sessionContext);
    }

    public List searchUsers(String keywords, Integer count, Integer page, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List contacts = new ArrayList();
        try {
            keywords = URLEncoder.encode(keywords, "UTF-8");
            Map parameters = new HashMap();
            parameters.put("q", keywords);
            if (count != null) {
                parameters.put("count", count);
            }
            if (page != null) {
                parameters.put("page", page);
            }
            ServiceConsumer serviceConsumer = new TwitterServiceConsumer(SEARCH_CONTACTS_URL, authMgr);
            HttpResponse response = serviceConsumer.doGet(parameters);
            JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
            if (jsonArray != null) {
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonContact = jsonArray.getJSONObject(i);
                    contacts.add(new TwitterContact(jsonContact, this));
                }
            }
        } catch (Exception e) {
            throw new RTXException("Unable to retrieve contact(s) information", e);
        }
        return contacts;
    }

    public List getUserInfo(String[] userIds, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (userIds.length > 100) {
            throw new RTXException("Array of user ids should be not bigger then 100");
        }
        List users = new ArrayList();
        String effectiveUrl = USER_LOOKUP_URL;
        ServiceConsumer serviceConsumer = new TwitterServiceConsumer(effectiveUrl, authMgr);
        Map payload = new HashMap();
        payload.put("user_id", StringUtils.join(userIds, ","));
        HttpResponse response = serviceConsumer.doGet(payload);
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        if (jsonArray != null) {
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonContact = jsonArray.getJSONObject(i);
                users.add(new TwitterContact(jsonContact, this));
            }
        }
        return users;

    }

    /**
     * Get a list of followings with next and previous cursor, for pagination Fixed in 20 results per call
     * 
     * @param userId
     * @param cursor
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowingsById(String userId, String cursor, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (userId != null) {
            parameters.put("user_id", userId);
        }
        if (cursor != null) {
            parameters.put("cursor", cursor);
        }
        return getContacts(GET_FOLLOWINGS_URL, parameters, authMgr);
    }

    /**
     * Get a list of followings with next and previous cursor, for pagination
     * 
     * @param userId
     * @param cursor
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowingsById(String userId, String cursor, Integer maxResults, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (userId != null) {
            parameters.put("user_id", userId);
        }
        if (cursor != null) {
            parameters.put("cursor", cursor);
        }
        return getContacts(GET_FOLLOWINGS_URL, maxResults, parameters, authMgr);
    }

    /**
     * Get a list of followers with next and previous cursor, for pagination Fixed in 20 results per call
     * 
     * @param userId
     * @param cursor
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowersById(String userId, String cursor, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (userId != null) {
            parameters.put("user_id", userId);
        }
        if (cursor != null) {
            parameters.put("cursor", cursor);
        }
        return getContacts(GET_FOLLOWERS_URL, parameters, authMgr);
    }

    /**
     * Get a list of followers with next and previous cursor, for pagination
     * 
     * @param userId
     * @param cursor
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowersById(String userId, String cursor, Integer maxResults, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (userId != null) {
            parameters.put("user_id", userId);
        }
        if (cursor != null) {
            parameters.put("cursor", cursor);
        }
        return getContacts(GET_FOLLOWERS_URL, maxResults, parameters, authMgr);
    }

    /**
     * Get a list of followings for the current user with next and previous cursor, for pagination Fixed 20 results per call
     * 
     * @param cursor
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowingsOfCurrentUser(String cursor, Map localContext, Map sessionContext)
            throws RTXException {
        return getCursoredFollowingsById(null, cursor, localContext, sessionContext);
    }

    /**
     * Get a list of followings for the current user with next and previous cursor, for pagination
     * 
     * @param cursor
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowingsOfCurrentUser(String cursor, Integer maxResults, Map localContext,
            Map sessionContext) throws RTXException {
        return getCursoredFollowingsById(null, cursor, maxResults, localContext, sessionContext);
    }

    /**
     * Get a list of followers for the current user with next and previous cursor, for pagination Fixed 20 results per call
     * 
     * @param cursor
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowersOfCurrentUser(String cursor, Map localContext, Map sessionContext)
            throws RTXException {
        return getCursoredFollowersById(null, cursor, localContext, sessionContext);
    }

    /**
     * Get a list of followers for the current user with next and previous cursor, for pagination
     * 
     * @param cursor
     * @param maxResults
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContactList getCursoredFollowersOfCurrentUser(String cursor, Integer maxResults, Map localContext, Map sessionContext)
            throws RTXException {
        return getCursoredFollowersById(null, cursor, maxResults, localContext, sessionContext);
    }

    /**
     * Get the tweets twitted by the current user
     * 
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getTweetsOfCurrentUser(String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", currentUserId);
        if (maxId != null)
            parameters.put("max_id", maxId);
        if (count != null)
            parameters.put("count", count);

        return getTweets(GET_TWEETS_URL, parameters, authMgr);
    }

    /**
     * Get the tweets twitted by the specified user
     * 
     * @param userId
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getTweetsById(String userId, String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", userId);
        if (maxId != null)
            parameters.put("max_id", maxId);
        if (count != null)
            parameters.put("count", count);

        return getTweets(GET_TWEETS_URL, parameters, authMgr);
    }

    /**
     * Get the tweets twitted by the specified user
     * 
     * @param username
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getTweetsByUsername(String username, String maxId, Integer count, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (maxId != null) {
            parameters.put("max_id", maxId);
        }
        if (count != null) {
            parameters.put("count", count);
        }
        parameters.put("screen_name", username);
        return getTweets(GET_TWEETS_URL, parameters, authMgr);
    }

    /**
     * Get tweets about the current user
     * 
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getTweetsAboutCurrentUser(String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (maxId != null) {
            parameters.put("max_id", maxId);
        }
        if (count != null) {
            parameters.put("count", count);
        }
        return getTweets(GET_MENTIONS_OF_CURRENT_USER_URL, parameters, authMgr);
    }

    /**
     * Get the tweets of the Current User home timeline
     * 
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getHomeTimelineOfCurrentUser(String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (maxId != null) {
            parameters.put("max_id", maxId);
        }
        if (count != null) {
            parameters.put("count", count);
        }
        return getTweets(GET_HOME_OF_CURRENT_USER_URL, parameters, authMgr);
    }

    /**
     * Get a tweet details
     * 
     * @param id
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet getTweetById(String id, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("id", id);
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_TWEET_BY_ID_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        return new TwitterTweet(HttpResponseReader.getJSonObject(response));
    }

    /**
     * Search for tweets by keywords
     * 
     * @param query
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List searchTweets(String query, String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        List tweets = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("q", query);
        if (maxId != null) {
            parameters.put("max_id", maxId);
        }
        if (count != null) {
            parameters.put("count", count);
        }
        ServiceConsumer consumer = new TwitterServiceConsumer(SEARCH_TWEETS_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONObject jsonResponse = HttpResponseReader.getJSonObject(response);
        JSONArray jsonStatuses = jsonResponse.getJSONArray("statuses");
        for (int i = 0; i < jsonStatuses.length(); i++) {
            tweets.add(new TwitterTweet(jsonStatuses.getJSONObject(i)));
        }
        return tweets;
    }

    /**
     * Post a tweet on behalf of the current user
     * 
     * @param text
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String postTweet(String text, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("status", text);
        ServiceConsumer consumer = new TwitterServiceConsumer(POST_TWEET_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        TwitterTweet tweet = new TwitterTweet(HttpResponseReader.getJSonObject(response));
        return tweet.getStatusId();
    }

    /**
     * Post a tweet with a picture, using the Twitter4j API
     * 
     * @param text
     * @param image
     * @param filename
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet postTweetWithMediaByAPI(String text, RTXBLOBData image, String filename, Map localContext, Map sessionContext)
            throws RTXException {
        Twitter twitter;
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (cb != null) {
            twitter = new TwitterFactory(cb.build()).getInstance();
        } else {
            twitter = new TwitterFactory().getInstance();
        }
        Status status;
        StatusUpdate statusUpdate = new StatusUpdate(text);
        statusUpdate.setMedia(filename, image.openFileInputStream());
        try {
            status = twitter.updateStatus(statusUpdate);
        } catch (TwitterException e) {
            throw new RTXException(e);
        }
        return new TwitterTweet(status);
    }

    /**
     * Post a tweet with a picture, using REST API (not working)
     * 
     * @param text
     * @param image
     * @param filename
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet postTweetWithMedia(String text, RTXBLOBData image, String filename, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("status", text);
        parameters.put("media[]", image.openFileInputStream());
        ServiceConsumer consumer = new TwitterServiceConsumer(POST_TWEET_WITH_MEDIA_URL, authMgr);
        HttpResponse response = consumer.send(image, parameters, HttpPost.METHOD_NAME, "media[]");
        return new TwitterTweet(HttpResponseReader.getJSonObject(response));
    }

    /**
     * Retweet a specified tweet
     * 
     * @param text
     * @param tweetId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet retweetById(String text, String tweetId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("text", text);
        String url = StringUtils.replace(RETWEET_URL, "{TWEET_ID}", tweetId);
        ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        TwitterTweet tweet = new TwitterTweet(HttpResponseReader.getJSonObject(response), Boolean.TRUE);
        return tweet;
    }

    /**
     * Send a message to a specified User
     * 
     * @param text
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterMessage sendMessageToId(String text, String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("text", text);
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(SEND_MESSAGE_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        return new TwitterMessage(HttpResponseReader.getJSonObject(response));
    }

    /**
     * Send a message to a specified User
     * 
     * @param text
     * @param username
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterMessage sendMessageToUsername(String text, String username, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("text", text);
        parameters.put("screen_name", username);
        ServiceConsumer consumer = new TwitterServiceConsumer(SEND_MESSAGE_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        return new TwitterMessage(HttpResponseReader.getJSonObject(response));
    }

    /**
     * Get a user details
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserById(String[] userIds, Map localContext, Map sessionContext) throws RTXException {
        List users = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", StringUtils.join(userIds, ","));
        ServiceConsumer consumer = new TwitterServiceConsumer(USER_LOOKUP_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                users.add(new TwitterContact(jsonObject, this));
            }
        }
        return users;
    }

    /**
     * Follow a specified user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContact followUser(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(FOLLOW_USER_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        return new TwitterContact(HttpResponseReader.getJSonObject(response), null);
    }

    /**
     * Unfollow a specified user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContact unfollowUser(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(UNFOLLOW_USER_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        return new TwitterContact(HttpResponseReader.getJSonObject(response), null);
    }

    /**
     * Get the list of relationships between the current user and the specified user. (following,followed_by,following_requested,none)
     * 
     * @param friendIds
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterFriendship friendshipLookup(String friendIds, Map localContext, Map sessionContext) throws RTXException {
        List friendships = new ArrayList();
        TwitterFriendship friendship = null;
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", friendIds);
        ServiceConsumer consumer = new TwitterServiceConsumer(FRIENDSHIP_LOOKUP_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        if (jsonArray != null && jsonArray.length() > 0) {
            friendship = new TwitterFriendship(jsonArray.getJSONObject(0), currentUserId);
        }
        return friendship;
    }

    /**
     * Get an embedded Html representation of a tweet
     * 
     * @param tweetId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String getOEmbededTweet(String tweetId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("id", tweetId);
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_OEMBEDED_TWEET_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        if (jsonObject != null) {
            return jsonObject.optString("html");
        }
        return null;

    }

    /**
     * Get a list of messages sent to the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getMessagesSentToCurrentUser(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        return getMessages(GET_MESSAGES_SENT_TO_CURRENT_USER_URL, null, authMgr);
    }

    /**
     * Get a list of messages sent by the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getMessagesSentByCurrentUser(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        return getMessages(GET_MESSAGES_SENT_BY_CURRENT_USER_URL, null, authMgr);
    }

    /**
     * Get a message details by Id
     * 
     * @param messageId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterMessage getMessageById(String messageId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("id", messageId);
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_MESSAGE_BY_ID_URL, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        if (jsonObject != null) {
            return new TwitterMessage(jsonObject);
        }
        return null;
    }

    /**
     * Get the lists of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserLists(Map localContext, Map sessionContext) throws RTXException {
        return getUserLists(null, localContext, sessionContext);
    }

    /**
     * Get the lists of the specified user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserLists(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        List userLists = new ArrayList();
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_USER_LISTS_URL, authMgr);
        HttpResponse response;
        if (userId != null) {
            parameters.put("user_id", userId);
            response = consumer.doGet(parameters);
        } else {
            response = consumer.doGet();
        }
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            userLists.add(new TwitterList(jsonObject));
        }
        return userLists;
    }

    /**
     * Get the users that subscribed to the specified List
     * 
     * @param listId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getListSubscribers(String listId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        return getTwitterListUsers(GET_LIST_SUBSCRIBERS_URL, parameters, authMgr);

    }

    /**
     * Get the users that are members of the Specified list
     * 
     * @param listId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getListMembers(String listId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        return getTwitterListUsers(GET_LIST_MEMBERS_URL, parameters, authMgr);
    }

    /**
     * Add the specified users as members of the specified list
     * 
     * @param listId
     * @param userIds
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void addMembersToList(String listId, String[] userIds, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String allUserIds = StringUtils.join(userIds, ",");
        Map parameters = new HashMap();
        parameters.put("user_id", allUserIds);
        parameters.put("list_id", listId);
        ServiceConsumer consumer = new TwitterServiceConsumer(ADD_MEMBERS_TO_LIST_URL, authMgr);
        consumer.doPost(parameters);
    }

    /**
     * Add the specified user to the specified list
     * 
     * @param listId
     * @param userId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void addMemberToList(String listId, String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(ADD_MEMBER_TO_LIST_URL, authMgr);
        consumer.doPost(parameters);
    }

    /**
     * Remove a user from a list
     * 
     * @param listId
     * @param userId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void removeMemberFromList(String listId, String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(REMOVE_MEMBER_FROM_LIST_URL, authMgr);
        consumer.doPost(parameters);
    }

    /**
     * Remove the specified users of a List
     * 
     * @param listId
     * @param userIds
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void removeMembersFromList(String listId, String[] userIds, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String allUserIds = StringUtils.join(userIds, ",");
        Map parameters = new HashMap();
        parameters.put("user_id", allUserIds);
        parameters.put("list_id", listId);
        ServiceConsumer consumer = new TwitterServiceConsumer(REMOVE_MEMBERS_FROM_LIST_URL, authMgr);
        consumer.doPost(parameters);
    }

    /**
     * Subscribe the current user to the specified list
     * 
     * @param listId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterList subscribeToList(String listId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        ServiceConsumer consumer = new TwitterServiceConsumer(SUBSCRIBE_TO_LIST_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        return new TwitterList(jsonObject);
    }

    /**
     * Unsubscribe the current user from the specified list
     * 
     * @param listId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void unsubscribeFromList(String listId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("list_id", listId);
        ServiceConsumer consumer = new TwitterServiceConsumer(UNSUBSCRIBE_FROM_LIST_URL, authMgr);
        consumer.doPost(parameters);
    }

    /**
     * Get the Categories of Suggestions by Twitter
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserSuggestionCategories(Map localContext, Map sessionContext) throws RTXException {
        List categories = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_USER_SUGGESTION_CATEGORIES_URL, authMgr);
        HttpResponse response = consumer.doGet();
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                categories.add(new TwitterUserSuggestionCategory(jsonArray.getJSONObject(i)));
            }
        }
        return categories;
    }

    /**
     * Get the Users suggested by Twitter in a specified category
     * 
     * @param category
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserSuggestionByCategory(String category, Map localContext, Map sessionContext) throws RTXException {
        List users = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String url = StringUtils.replace(GET_USER_SUGGESTION_BY_CATEGORY_URL, "{SLUG}", category);
        ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
        HttpResponse response = consumer.doGet();
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        JSONArray jsonUsers = jsonObject.getJSONArray("users");
        for (int i = 0; i < jsonUsers.length(); i++) {
            users.add(new TwitterContact(jsonUsers.getJSONObject(i), this));
        }
        return users;
    }

    /**
     * Get the list of users blocked by the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getBlockedUsers(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List blockedUsers = new ArrayList();
        ServiceConsumer consumer = new TwitterServiceConsumer(GET_BLOCKED_USERS_URL, authMgr);
        HttpResponse response = consumer.doGet();
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        JSONArray users = jsonObject.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            blockedUsers.add(new TwitterContact(users.getJSONObject(i), this));
        }
        return blockedUsers;
    }

    /**
     * Block the specified user on behalf of the current user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContact blockUser(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(BLOCK_USER_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        return new TwitterContact(jsonObject, null);
    }

    /**
     * Unblock a user
     * 
     * @param userId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterContact unBlockUser(String userId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("user_id", userId);
        ServiceConsumer consumer = new TwitterServiceConsumer(UNBLOCK_USER_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        return new TwitterContact(jsonObject, null);
    }

    /**
     * Get tweets favourited by the current user
     * 
     * @param maxId
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getCurrentUserFavoriteTweets(String maxId, Integer count, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        if (maxId != null) {
            parameters.put("max_id", maxId);
        }
        if (count != null) {
            parameters.put("count", count);
        }
        return getTweets(GET_FAVORITE_TWEETS_URL, parameters, authMgr);
    }

    /**
     * Favourite a tweet on behalf of the current user
     * 
     * @param tweetId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet favoriteStatus(String tweetId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("id", tweetId);
        ServiceConsumer consumer = new TwitterServiceConsumer(FAVORITE_STATUS_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        return new TwitterTweet(jsonObject);
    }

    /**
     * Unfavourite a status
     * 
     * @param tweetId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public TwitterTweet unfavoriteStatus(String tweetId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        Map parameters = new HashMap();
        parameters.put("id", tweetId);
        ServiceConsumer consumer = new TwitterServiceConsumer(UNFAVORITE_STATUS_URL, authMgr);
        HttpResponse response = consumer.doPost(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        return new TwitterTweet(jsonObject);
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
                ServiceConsumer serviceConsumer = new TwitterServiceConsumer(USER_PROFILE_URL, authMgr);
                HttpResponse response = serviceConsumer.doGet();
                if (response != null) {
                    user = new TwitterUser(HttpResponseReader.getJSonObject(response), this, authMgr);
                    sessionContext.put(CURRENT_USER_KEY, user);
                }
            }
        }
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# readAccessToken(org.apache.http.HttpResponse)
     */
    public AccessToken readAccessToken(HttpResponse response) {
        throw new UnsupportedOperationException("Should not happen (access management delegated to signout lib)");
    }

    private IAuthManager locateOAuthManager(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = (IAuthManager) sessionContext.get(OAUTH_MANAGER_KEY);
        if (authMgr == null) {
            authMgr = new OAuth1Manager(REQ_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_TOKEN_URL, consumerKey, consumerSecret, this);
            sessionContext.put(OAUTH_MANAGER_KEY, authMgr);
        }
        return authMgr;
    }

    /**
     * Return a list of connections and the next and previous cursor for pagination Fixed on 20 results per call
     * 
     * @param url
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private TwitterContactList getContacts(String url, Map parameters, IAuthManager authMgr) throws RTXException {
        Integer remainingRateLimit = lastRateLimit;
        if (remainingRateLimit.intValue() == 0) {
            throw new RateLimitExceededException();
        }
        List contacts = new ArrayList();
        try {
            ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
            HttpResponse response = consumer.doGet(parameters);
            JSONObject jsonResponse = HttpResponseReader.getJSonObject(response);
            String nextCursor = jsonResponse.optString("next_cursor_str");
            String previousCursor = jsonResponse.optString("previous_cursor_str");
            JSONArray jsonContacts = jsonResponse.getJSONArray("users");
            for (int i = 0; i < jsonContacts.length(); i++) {
                contacts.add(new TwitterContact(jsonContacts.getJSONObject(i), this));
            }
            if (response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER) != null) {
                lastRateLimit = new Integer(response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER).getValue());
            }
            return new TwitterContactList(contacts, nextCursor, previousCursor);
        } catch (Exception e) {
            int index = ExceptionUtils.indexOfThrowable(e, RateLimitExceededException.class);
            if (index > -1) {
                throw (RateLimitExceededException) ExceptionUtils.getThrowables(e)[index];
            }
            logError("Unable to retrieve contacts", e);
            return new TwitterContactList(contacts, "0", "-1");
        }
    }

    /**
     * Makes the necessary numbers of calls to retrieve the required number of results
     * 
     * @param url
     * @param maxResults
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private TwitterContactList getContacts(String url, Integer maxResults, Map parameters, IAuthManager authMgr) throws RTXException {
        Integer remainingRateLimit = lastRateLimit;
        if (remainingRateLimit.intValue() == 0) {
            throw new RateLimitExceededException();
        }
        List contacts = new ArrayList();
        try {
            ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
            String nextCursor, previousCursor = "-1";
            if (parameters.containsKey("cursor")) {
                nextCursor = parameters.get("cursor").toString();
            } else {
                nextCursor = "-1";
            }
            Integer count = new Integer(0);
            HttpResponse response;
            JSONObject jsonResponse;
            JSONArray jsonContacts;
            // If we need just one call
            if (maxResults.intValue() <= 20 && remainingRateLimit.intValue() != 0) {
                response = consumer.doGet(parameters);
                jsonResponse = HttpResponseReader.getJSonObject(response);
                if (response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER) != null) {
                    remainingRateLimit = new Integer(response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER).getValue());
                }
                nextCursor = jsonResponse.optString("next_cursor_str");
                previousCursor = jsonResponse.optString("previous_cursor_str");
                jsonContacts = jsonResponse.getJSONArray("users");
                int size = (maxResults.intValue() == 20) ? 20 : maxResults.intValue();
                if (jsonContacts.length() < size) {
                    size = jsonContacts.length();
                }
                for (int i = 0; i < size; i++) {
                    contacts.add(new TwitterContact(jsonContacts.getJSONObject(i), this));
                }
                return new TwitterContactList(contacts, nextCursor, previousCursor);
            } else {
                // Get the number of calls to me made (apart from the last one)
                Integer numCalls = new Integer(maxResults.intValue() / 20);
                // Get the number of elements to get in the last call
                Integer remainder = new Integer(maxResults.intValue() % 20);
                Integer callsMade = new Integer(0);

                // make the first n-1 calls
                while (!nextCursor.equals("0") && callsMade.intValue() < numCalls.intValue() && remainingRateLimit.intValue() != 0) {
                    parameters.put("cursor", nextCursor);
                    response = consumer.doGet(parameters);
                    callsMade = new Integer(callsMade.intValue() + 1);
                    jsonResponse = HttpResponseReader.getJSonObject(response);
                    nextCursor = jsonResponse.optString("next_cursor_str");
                    previousCursor = jsonResponse.optString("previous_cursor_str");
                    jsonContacts = jsonResponse.getJSONArray("users");
                    for (int i = 0; i < jsonContacts.length(); i++) {
                        contacts.add(new TwitterContact(jsonContacts.getJSONObject(i), this));
                    }
                    if (response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER) != null) {
                        remainingRateLimit = new Integer(response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER).getValue());
                    }
                }

                // make the last call with the remainders
                if (remainder.intValue() != 0 && !nextCursor.equals("0") && remainingRateLimit.intValue() != 0) {
                    parameters.put("curor", nextCursor);
                    response = consumer.doGet(parameters);
                    jsonResponse = HttpResponseReader.getJSonObject(response);
                    nextCursor = jsonResponse.optString("next_cursor_str");
                    previousCursor = jsonResponse.optString("previous_cursor_str");
                    jsonContacts = jsonResponse.getJSONArray("users");
                    int size = (remainder.intValue() < jsonContacts.length()) ? remainder.intValue() : jsonContacts.length();
                    for (int i = 0; i < size; i++) {
                        contacts.add(new TwitterContact(jsonResponse, this));
                    }
                }
                lastRateLimit = remainingRateLimit;
                return new TwitterContactList(contacts, nextCursor, previousCursor);
            }
        } catch (Exception e) {
            int index = ExceptionUtils.indexOfThrowable(e, RateLimitExceededException.class);
            if (index > -1) {
                throw (RateLimitExceededException) ExceptionUtils.getThrowables(e)[index];
            }
            logError("Unable to retrieve contacts", e);
            return new TwitterContactList(contacts, "0", "-1");
        }
    }

    /**
     * Make a get request to the specified URL and return a list of Twitter object Ids
     * 
     * @param url
     * @param parameters
     * @param maxResults
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private List getIds(String url, Map parameters, int maxResults, IAuthManager authMgr) throws RTXException {
        Integer remainingRateLimit = null;
        Integer remainingWindowRateReset = null;
        if (parameters == null) {
            parameters = new HashMap();
        }
        ServiceConsumer serviceConsumer = new TwitterServiceConsumer(url, authMgr);
        List ids = new ArrayList();
        int count = 0;
        // cursor used to page through results
        int cursor = -1;
        while (count < maxResults && cursor != 0) {
            // Check if the application has exceeded the Allowed number of Requests
            if (remainingRateLimit != null && remainingRateLimit.intValue() == 0) {
                throw new RateLimitExceededException();
            }
            parameters.put("cursor", new Integer(cursor));
            HttpResponse response = serviceConsumer.doGet(parameters);
            JSONObject jsonResponse = HttpResponseReader.getJSonObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("ids");
            cursor = jsonResponse.optInt("next_cursor");
            for (int i = 0; i < jsonArray.length(); i++) {
                ids.add(jsonArray.getString(i));
            }
            count += jsonArray.length();
            if (response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER) != null) {
                remainingRateLimit = new Integer(response.getFirstHeader(REMAINING_RATE_LIMIT_HEADER).getValue());
            }
            if (response.getFirstHeader(REMAINING_WINDOW_RATE_RESET) != null) {
                remainingWindowRateReset = new Integer(response.getFirstHeader(REMAINING_WINDOW_RATE_RESET).getValue());
            }
        }
        return ids;

    }

    /**
     * Make a GET request to URL and get a list of tweets
     * 
     * @param url
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private List getTweets(String url, Map parameters, IAuthManager authMgr) throws RTXException {
        ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
        List tweets = new ArrayList();
        HttpResponse response;
        if (parameters == null) {
            response = consumer.doGet();
        } else {
            response = consumer.doGet(parameters);
        }
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(new TwitterTweet(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    /**
     * Make a GET request to URL and get a list of messages
     * 
     * @param url
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private List getMessages(String url, Map parameters, IAuthManager authMgr) throws RTXException {
        List messages = new ArrayList();
        HttpResponse response;
        ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
        if (parameters == null) {
            response = consumer.doGet();
        } else {
            response = consumer.doGet(parameters);
        }
        JSONArray jsonArray = HttpResponseReader.getJSonArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            messages.add(new TwitterMessage(jsonObject));
        }
        return messages;
    }

    /**
     * Make a GET request to the specified URL and return a List of TwitterContact objects
     * 
     * @param url
     * @param parameters
     * @param authMgr
     * @return
     * @throws RTXException
     */
    private List getTwitterListUsers(String url, Map parameters, IAuthManager authMgr) throws RTXException {
        List users = new ArrayList();
        ServiceConsumer consumer = new TwitterServiceConsumer(url, authMgr);
        HttpResponse response = consumer.doGet(parameters);
        JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
        JSONArray jsonUsers = jsonObject.getJSONArray("users");
        for (int i = 0; i < jsonUsers.length(); i++) {
            JSONObject user = jsonUsers.getJSONObject(i);
            users.add(new TwitterContact(user, this));
        }
        return users;
    }

    private static class TwitterServiceConsumer extends ServiceConsumer {

        public TwitterServiceConsumer(String endpointURL, IAuthManager authManager) throws RTXException {
            super(endpointURL, authManager);
        }

        protected void checkError(HttpResponse response) throws RTXException {
            try {
                super.checkError(response);
            } catch (RTXException e) {
                if (response.getStatusLine().getStatusCode() == 429) {
                    throw new RateLimitExceededException(e);
                }
                throw e;
            }
        }
    }

    public static class RateLimitExceededException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private static final String ERROR_MSG = "The Twitter API rate limit has been exceeded, try again later.";

        private RateLimitExceededException() {
            super(ERROR_MSG);
        }

        private RateLimitExceededException(Exception e) {
            super(ERROR_MSG, e);
        }

    }

}
