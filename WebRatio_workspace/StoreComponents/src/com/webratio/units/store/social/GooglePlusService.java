package com.webratio.units.store.social;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.AbstractOAuthableApplicationService;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.application.IUser;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.HttpResponseReader;
import com.webratio.units.store.commons.auth.IAuthManager;
import com.webratio.units.store.commons.auth.OAuth2Manager;
import com.webratio.units.store.commons.auth.OAuth2StatelessManager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

/**
 * The social network service implementation for Google+.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GooglePlusService extends AbstractOAuthableApplicationService implements ISocialNetworkService {

    /* context keys */
    protected static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.GOOGLEPLUS_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.GOOGLEPLUS_CURRENT_USER";

    /* OAuth URLs */
    protected static final String REQ_TOKEN_URL = "https://accounts.google.com/o/oauth2/auth[response_type=code|scope=https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/drive]";
    private static final String ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token[grant_type=authorization_code]";
    private static final String SIGN_OUT_URL = "https://accounts.google.com/o/oauth2/revoke?token=${ACCESS_TOKEN}";

    /* API URLs */
    private static final String USER_PROFILE_URL = "https://www.googleapis.com/plus/v1/people/me";
    private static final String USER_EMAIL_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String SEARCH_CONTACTS_URL = "https://www.googleapis.com/plus/v1/people[query=${KEY_WORDS}]";
    private static final String GET_POSTS_OF_LOGGEDIN_USER_URL = "https://www.googleapis.com/plus/v1/people/me/activities/${collection}[alt=json|key=${YOUR_API_KEY}]";
    private static final String GET_POSTS_BY_USERID_URL = "https://www.googleapis.com/plus/v1/people/${userId}/activities/${collection}[alt=json|key=${YOUR_API_KEY}]";
    private static final String GET_COMMENTS_BY_POSTID_URL = "https://www.googleapis.com/plus/v1/activities/${postId}/comments[alt=json|key=${YOUR_API_KEY}]";

    /* OAuth API keys */
    protected final String consumerKey;
    private final String consumerSecret;

    public GooglePlusService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);
        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));
        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for 'Google+' the social network data provider")
                    .addHint("Set the API Key for the 'Google+' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'Google+' social network data provider")
                    .addHint("Set the Secret Key for the 'Google+' social network data provider");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "GooglePlus";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getHomePage()
     */
    public String getHomePage() {
        return "https://plus.google.com/";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#init(com.webratio.units.store.commons.auth.AccessToken,
     * java.util.Map, java.util.Map)
     */
    public void init(AccessToken accessToken, Map localContext, Map sessionContext) throws RTXException {
        locateOAuthManager(localContext, sessionContext).setAccessToken(accessToken);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#computeAuthorizationUrl(java.lang.String, java.util.Map,
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
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#getAccessToken(java.util.Map, java.util.Map)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#logout(java.util.Map, java.util.Map)
     */
    public void logout(Map localContext, Map sessionContext) throws RTXException {
        OAuth2Manager authMgr = (OAuth2Manager) locateOAuthManager(localContext, sessionContext);
        try {
            if (isAuthorized(localContext, sessionContext)) {
                try {
                    String url = StringUtils.replace(SIGN_OUT_URL, "${ACCESS_TOKEN}", authMgr.getAccessToken().getValue());
                    authMgr.getHTTPClient().execute(new HttpGet(url));
                } catch (Exception e) {
                    // ignores exceptions
                }
            }
        } finally {
            authMgr.setAccessToken(null);
            sessionContext.remove(CURRENT_USER_KEY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#isAuthorized(java.util.Map, java.util.Map)
     */
    public boolean isAuthorized(Map localContext, Map sessionContext) throws RTXException {
        return locateOAuthManager(localContext, sessionContext).isAuthorized();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getUser(java.util.Map, java.util.Map)
     */
    public IUser getUser(Map localContext, Map sessionContext) throws RTXException {
        IUser user = (IUser) sessionContext.get(CURRENT_USER_KEY);
        if (user == null) {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            if (authMgr.isAuthorized()) {
                ServiceConsumer serviceConsumer = new ServiceConsumer(USER_PROFILE_URL, authMgr);
                HttpResponse response = serviceConsumer.doGet();
                if (response != null) {
                    String email = getUserEmail(authMgr, user);
                    user = new GooglePlusUser(HttpResponseReader.getJSonObject(response), this, email);
                    sessionContext.put(CURRENT_USER_KEY, user);
                }
            }
        }
        return user;
    }

    private String getUserEmail(IAuthManager authMgr, IUser user) throws RTXException {
        ServiceConsumer serviceConsumer = new ServiceConsumer(USER_EMAIL_URL, authMgr);
        HttpResponse response = serviceConsumer.doGet();
        if (response != null) {
            return HttpResponseReader.getJSonObject(response).optString("email");
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.ISocialNetworkService#searchContacts(java.lang.String, java.util.Map,
     * java.util.Map)
     */
    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        List contacts = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized()) {
            String url = StringUtils.replace(SEARCH_CONTACTS_URL, "${KEY_WORDS}", keywords);
            ServiceConsumer serviceConsumer = new ServiceConsumer(url, authMgr);
            HttpResponse response = serviceConsumer.doGet();
            if (response != null) {
                JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
                JSONArray jsonContacts = jsonObject.optJSONArray("items");
                if (jsonContacts != null) {
                    for (int i = 0; i < jsonContacts.length(); i++) {
                        contacts.add(new GooglePlusContact(jsonContacts.getJSONObject(i), this));
                    }
                }
            }
        }
        return contacts;
    }

    public static List getCommentsById(String postId, String apiKey) throws RTXException {
        List comments = new ArrayList();
        String effectiveUrl = GET_COMMENTS_BY_POSTID_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${postId}", postId);
        effectiveUrl = StringUtils.replace(effectiveUrl, "${YOUR_API_KEY}", apiKey);

        ServiceConsumer serviceConsumer = new ServiceConsumer(effectiveUrl, null);
        HttpResponse response = serviceConsumer.doUnsignedGet();

        if (response != null) {
            JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
            JSONArray jsonComments = jsonObject.optJSONArray("items");
            if (jsonComments != null) {
                for (int i = 0; i < jsonComments.length(); i++) {
                    try {
                        comments.add(new GooglePlusComment(jsonComments.getJSONObject(i)));
                    } catch (ParseException e) {
                        throw new RTXException("Unable to parse the comment", e);
                    }
                }
            }
        }
        return comments;

    }

    public List getPostsOfLoggedUser(String apiKey, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        List posts = new ArrayList();
        String effectiveUrl = GET_POSTS_OF_LOGGEDIN_USER_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${collection}", "public");
        effectiveUrl = StringUtils.replace(effectiveUrl, "${YOUR_API_KEY}", apiKey);

        ServiceConsumer serviceConsumer = new ServiceConsumer(effectiveUrl, authMgr);
        HttpResponse response = serviceConsumer.doGet();

        if (response != null) {
            JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
            JSONArray jsonPosts = jsonObject.optJSONArray("items");
            if (jsonPosts != null) {
                for (int i = 0; i < jsonPosts.length(); i++) {
                    try {
                        posts.add(new GooglePlusPost(jsonPosts.getJSONObject(i)));
                    } catch (ParseException e) {
                        throw new RTXException("Unable to parse the post", e);
                    }
                }
            }
        }
        return posts;
    }

    public static List getPostsById(String userId, String apiKey) throws RTXException {
        List posts = new ArrayList();
        String effectiveUrl = GET_POSTS_BY_USERID_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${userId}", userId);
        effectiveUrl = StringUtils.replace(effectiveUrl, "${collection}", "public");
        effectiveUrl = StringUtils.replace(effectiveUrl, "${YOUR_API_KEY}", apiKey);

        ServiceConsumer serviceConsumer = new ServiceConsumer(effectiveUrl, null);
        HttpResponse response = serviceConsumer.doUnsignedGet();

        if (response != null) {
            JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
            JSONArray jsonPosts = jsonObject.optJSONArray("items");
            if (jsonPosts != null) {
                for (int i = 0; i < jsonPosts.length(); i++) {
                    try {
                        posts.add(new GooglePlusPost(jsonPosts.getJSONObject(i)));
                    } catch (ParseException e) {
                        throw new RTXException("Unable to parse the post", e);
                    }
                }
            }
        }
        return posts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService#readAccessToken(org.apache.http.HttpResponse)
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

}
