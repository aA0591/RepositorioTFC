package com.webratio.units.store.social;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
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
import com.webratio.units.store.commons.auth.OAuth2StatelessManager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

/**
 * The social network service implementation for LinkedIn.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LinkedInService extends AbstractApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.LINKEDIN_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.LINKEDIN_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL_STATE = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
    private static final String REQ_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/authorization[response_type=code|state="
            + REQ_TOKEN_URL_STATE + "]";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken[grant_type=authorization_code]";

    /* API URLs */
    private static final String USER_PROFILE_URL = "https://api.linkedin.com/v1/people/${USER-ID}:(id,first-name,last-name,headline,site-standard-profile-request,location,picture-url,email-address)";
    private static final String SEARCH_CONTACTS_URL = "https://api.LinkedIn.com/v1/people-search:(people:(id,first-name,last-name,picture-url,location:(name)),num-results)[keywords=${keywords}|start=${start}|count=${count}]";
    private static final String USER_CONNECTIONS_URL = "https://api.linkedin.com/v1/people/~/connections[count=${count}|start=${start}]";
    private static final String MESSAGE_POST_URL = "https://api.linkedin.com/v1/people/~/mailbox";
    private static final String GROUP_POSTS_URL = "https://api.linkedin.com/v1/groups/${group-id}/posts:(id,title,summary,creator:(first-name,last-name,picture-url,headline),likes,attachment:(image-url,content-domain,content-url,title,summary),relation-to-viewer)?category=discussion&order=popularity&count=5";
    private static final String USER_GROUPS_URL = "https://api.linkedin.com/v1/people/~/group-memberships:(group:(id,name,small-logo-url),membership-state)";
    private static final String CREATE_GROUP_POST_URL = "https://api.linkedin.com/v1/groups/${group-id}/posts";
    private static final String CREATE_POST_COMMENT_URL = "https://api.linkedin.com/v1/posts/${post-id}/comments";
    private static final String GET_SUGGESTED_GROUPS_URL = "https://api.linkedin.com/v1/people/~/suggestions/groups:(id,name,small-logo-url,is-open-to-non-members)[count=${count}|start=${start}]";
    private static final String JOIN_GROUP_URL = "https://api.linkedin.com/v1/people/~/group-memberships/${group-id}";
    private static final String GET_SUGGESTED_COMPANIES_URL = "https://api.linkedin.com/v1/people/~/suggestions/to-follow/companies:(id,name,logo-url)[count=${count}|start=${start}]";
    private static final String GET_FOLLOWING_COMPANIES_URL = "https://api.linkedin.com/v1/people/~/following/companies:(id,name,logo-url)[count=${count}|start=${start}]";
    private static final String SEARCH_COMPANIES_URL = "https://api.linkedin.com/v1/company-search:(companies:(id,name,logo-url))[keywords=${keywords}|count=${count}|start=${start}]";
    private static final String GET_COMPANY_BY_ID = "https://api.linkedin.com/v1/companies/${company-id}:(id,name,website-url,logo-url,description,company-type)";
    private static final String FOLLOW_COMPANY_URL = "https://api.linkedin.com/v1/people/~/following/companies";
    private static final String SEARCH_JOBS_URL = "https://api.linkedin.com/v1/job-search:(jobs:(id,company:(name,id),position:(title)))[job-title=${job-title}|company-name=${company-name}]";
    private static final String SEARCH_JOBS_BY_KEYWORD_URL = "https://api.linkedin.com/v1/job-search:(jobs:(id,company:(name,id),position:(title)))[keywords=${keywords}|count=${count}|start=${start}]";
    private static final String GET_SUGGESTED_JOBS_URL = "https://api.linkedin.com/v1/people/~/suggestions/job-suggestions:(jobs:(id,company:(name,id),position:(title)))[start=${start}|count=${count}]";
    private static final String GET_BOOKMARKED_JOBS_URL = "https://api.linkedin.com/v1/people/~/job-bookmarks:(job:(id,company:(name,id),position:(title)))[count=${count}|start=${start}]";
    private static final String BOOKMARK_JOB_URL = "https://api.linkedin.com/v1/people/~/job-bookmarks";
    private static final String GET_JOB_BY_ID_URL = "https://api.linkedin.com/v1/jobs/${job-id}:(id,company:(id,name),position:(title,location:(name)),skills-and-experience,description,salary,site-job-url,posting-date)";

    /* OAuth API keys */
    private final String consumerKey;
    private final String consumerSecret;

    public LinkedInService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);
        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));
        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for the 'LinkedIn' social network data provider")
                    .addHint("Set the API Key for the 'LinkedIn' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'LinkedIn' social network data provider")
                    .addHint("Set the Secret Key for the 'LinkedIn' social network data provider");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "LinkedIn";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.application.IApplication#getHomePage()
     */
    public String getHomePage() {
        return "http://www.linkedin.com";
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
            return authMgr.getAccessToken();
        } catch (Exception e) {
            throw new RTXException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.units.store.commons.auth.IAuthorizationAwareService# readAccessToken(org.apache.http.HttpResponse)
     */
    // Copied from GooglePlusService
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

    // Copied from GooglePlusService implementing OAuth2
    private IAuthManager locateOAuthManager(Map localContext, Map sessionContext) throws RTXException {
        String accessToken = BeanHelper.asString(localContext.get(localContext.get("unitId") + ".accessToken"));
        IAuthManager authMgr;
        if (!((accessToken == null) || ("".equals(accessToken)))) {
            authMgr = new OAuth2StatelessManager(accessToken);
        } else {
            authMgr = (IAuthManager) sessionContext.get(OAUTH_MANAGER_KEY);
            if (authMgr == null) {
                authMgr = new OAuth2Manager(REQ_TOKEN_URL, ACCESS_TOKEN_URL, consumerKey, consumerSecret, (ISocialNetworkService) this) {
                    private static final long serialVersionUID = 1L;

                    public void sign(HttpRequestBase request) throws RTXException {
                        if (accessToken != null) {
                            request.setHeader("Authorization", "Bearer " + accessToken.getValue());
                        }
                    };
                };
            }
        }
        sessionContext.put(OAUTH_MANAGER_KEY, authMgr);
        return authMgr;
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
        authMgr.setAccessToken(null);
        sessionContext.remove(CURRENT_USER_KEY);
    }

    public List searchContacts(String keywords, Integer start, Integer count, Map localContext, Map sessionContext)
            throws RTXException {
        List contacts = new ArrayList();
        try {
            keywords = URLEncoder.encode(keywords, "UTF-8");
            String searchUrl = SEARCH_CONTACTS_URL;
            searchUrl = StringUtils.replace(searchUrl, "${keywords}", keywords);
            searchUrl = setUrlIndexParams(searchUrl, count, start);

            Element root = getResponse(searchUrl, localContext, sessionContext);

            if (root != null) {
                contacts = getListOfContacts("//person", root);
            }
        } catch (Exception e) {
            throw new RTXException("Unable to retrieve contact(s) information", e);
        }
        return contacts;
    }

    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        return searchContacts(keywords, null, null, localContext, sessionContext);
    }

    /**
     * Send messages to a list of recipients
     * 
     * @param localContext
     * @param sessionContext
     * @param message
     * @param recipients
     * @param title
     * @throws RTXException
     */
    public void sendMessage(Map localContext, Map sessionContext, String message, String[] recipients, String title)
            throws RTXException {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("mailbox-item");
        Element recipentsElem = root.addElement("recipients");
        for (int i = 0; i < recipients.length; i++) {
            recipentsElem.addElement("recipient").addElement("person").addAttribute("path", "/people/" + recipients[i]);
        }
        root.addElement("subject").setText(title);
        root.addElement("body").setText(message);

        sendRequestWithDoc(MESSAGE_POST_URL, doc, HttpPost.METHOD_NAME, localContext, sessionContext);

    }

    public LinkedInUser getUserById(String userId, Map localContext, Map sessionContext) throws RTXException {

        String url = StringUtils.replace(USER_PROFILE_URL, "${USER-ID}", userId);

        Element element = getResponse(url, localContext, sessionContext);

        return new LinkedInUser(element, this);
    }

    /**
     * Comment on a Post
     * 
     * @param postId
     * @param comment
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void createPostComment(String postId, String comment, Map localContext, Map sessionContext) throws RTXException {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("comment");
        root.addElement("text").setText(comment);

        String effectiveUrl = CREATE_POST_COMMENT_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${post-id}", postId);

        sendRequestWithDoc(effectiveUrl, doc, HttpPost.METHOD_NAME, localContext, sessionContext);
    }

    /**
     * Post on a group
     * 
     * @param groupId
     * @param title
     * @param body
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void createGroupPost(String groupId, String title, String body, Map localContext, Map sessionContext) throws RTXException {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("post");
        root.addElement("title").setText(title);
        root.addElement("summary").setText(body);

        String effectiveUrl = CREATE_GROUP_POST_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${group-id}", groupId);

        sendRequestWithDoc(effectiveUrl, doc, HttpPost.METHOD_NAME, localContext, sessionContext);

    }

    /**
     * Get the groups of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserGroups(Map localContext, Map sessionContext) throws RTXException {
        List groups = new ArrayList();
        Element root = getResponse(USER_GROUPS_URL, localContext, sessionContext);

        if (root != null) {
            groups = getListOfGroups("/group-memberships/group-membership/group", root);
        }
        return groups;
    }

    /**
     * Get the posts for the identified Group
     * 
     * @param groupId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getGroupPosts(String groupId, Map localContext, Map sessionContext) throws RTXException {
        List posts = new ArrayList();
        String effectiveUrl = GROUP_POSTS_URL;
        effectiveUrl = StringUtils.replace(effectiveUrl, "${group-id}", groupId);

        Element root = getResponse(effectiveUrl, localContext, sessionContext);

        if (root != null) {
            posts = getListOfPosts("//post", root);
        }

        return posts;
    }

    /**
     * Get a list of Groups suggested by LinkedIn to the current user
     * 
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedGroups(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {

        List groups = new ArrayList();

        String url = setUrlIndexParams(GET_SUGGESTED_GROUPS_URL, count, start);

        Element root = getResponse(url, localContext, sessionContext);

        if (root != null) {
            groups = getListOfGroups("//group", root);
        }

        return groups;
    }

    /**
     * Get a list of Groups suggested by LinkedIn to the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedGroups(Map localContext, Map sessionContext) throws RTXException {

        return getSuggestedGroups(null, null, localContext, sessionContext);
    }

    /**
     * Join a identified group
     * 
     * @param groupId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void joinGroup(String groupId, Map localContext, Map sessionContext) throws RTXException {

        String url = StringUtils.replace(JOIN_GROUP_URL, "${group-id}", groupId);

        Document document = DocumentFactory.getInstance().createDocument();
        Element root = document.addElement("group-membership");
        Element membershipState = root.addElement("membership-state");
        membershipState.addElement("code").setText("member");

        sendRequestWithDoc(url, document, HttpPut.METHOD_NAME, localContext, sessionContext);
    }

    /**
     * Leave a group
     * 
     * @param groupId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void leaveGroup(String groupId, Map localContext, Map sessionContext) throws RTXException {
        String url = StringUtils.replace(JOIN_GROUP_URL, "${group-id}", groupId);

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer consumer = new ServiceConsumer(url, authMgr);

        consumer.doDelete();
    }

    /**
     * Get a list of companies suggested by LinkedIn to the Current user
     * 
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedCompanies(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {
        String url = setUrlIndexParams(GET_SUGGESTED_COMPANIES_URL, count, start);
        Element root = getResponse(url, localContext, sessionContext);
        return getListOfCompanies("//company", root);
    }

    /**
     * Get a list of companies suggested by LinkedIn to the Current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedCompanies(Map localContext, Map sessionContext) throws RTXException {

        return getSuggestedCompanies(null, null, localContext, sessionContext);

    }

    /**
     * Get the list of Companies the Current User is following
     * 
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFollowingCompanies(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {
        String url = setUrlIndexParams(GET_FOLLOWING_COMPANIES_URL, count, start);
        Element root = getResponse(url, localContext, sessionContext);
        return getListOfCompanies("//company", root);
    }

    /**
     * Get the list of Companies the Current User is following
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFollowingCompanies(Map localContext, Map sessionContext) throws RTXException {
        return getFollowingCompanies(null, null, localContext, sessionContext);
    }

    /**
     * Search for companies by Keywords
     * 
     * @param keywords
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List searchCompanies(String keywords, Integer start, Integer count, Map localContext, Map sessionContext)
            throws RTXException {
        String url = SEARCH_COMPANIES_URL;
        url = StringUtils.replace(url, "${keywords}", keywords);
        url = setUrlIndexParams(url, count, start);
        Element root = getResponse(url, localContext, sessionContext);
        return getListOfCompanies("//companies/company", root);
    }

    /**
     * Search for companies by Keywords
     * 
     * @param keywords
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List searchCompanies(String keywords, Map localContext, Map sessionContext) throws RTXException {

        return searchCompanies(keywords, null, null, localContext, sessionContext);

    }

    /**
     * Get a Company details by Id
     * 
     * @param companyId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public LinkedInCompany getCompanyById(String companyId, Map localContext, Map sessionContext) throws RTXException {

        String url = StringUtils.replace(GET_COMPANY_BY_ID, "${company-id}", companyId);

        Element root = getResponse(url, localContext, sessionContext);

        return new LinkedInCompany(root, this);
    }

    /**
     * Start following a company
     * 
     * @param companyId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void followCompany(String companyId, Map localContext, Map sessionContext) throws RTXException {

        Document doc = DocumentFactory.getInstance().createDocument();
        Element company = doc.addElement("company");
        company.addElement("id").setText(companyId);

        sendRequestWithDoc(FOLLOW_COMPANY_URL, doc, HttpPost.METHOD_NAME, localContext, sessionContext);

    }

    /**
     * Stop following a company
     * 
     * @param companyId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void unfollowCompany(String companyId, Map localContext, Map sessionContext) throws RTXException {

        String url = FOLLOW_COMPANY_URL + "/id=" + companyId;

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer consumer = new ServiceConsumer(url, authMgr);

        consumer.doDelete();

    }

    /**
     * Search for Jobs by Title and Company name
     * 
     * @param jobTitle
     * @param companyName
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List searchJobs(String jobTitle, String companyName, Map localContext, Map sessionContext) throws RTXException {

        String url = SEARCH_JOBS_URL;

        try {
            jobTitle = URLEncoder.encode(jobTitle, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RTXException(e);
        }

        try {
            companyName = URLEncoder.encode(companyName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RTXException(e);
        }

        url = StringUtils.replace(url, "${job-title}", jobTitle);
        url = StringUtils.replace(url, "${company-name}", companyName);

        Element root = getResponse(url, localContext, sessionContext);

        return getListOfJobs("//job", root);

    }

    /**
     * Search Jobs by key words
     * 
     * @param keyword
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List searchJobsByKeyword(String keyword, Integer start, Integer count, Map localContext, Map sessionContext)
            throws RTXException {

        String url = SEARCH_JOBS_BY_KEYWORD_URL;
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RTXException(e);
        }
        url = StringUtils.replace(url, "${keywords}", keyword);
        url = setUrlIndexParams(url, count, start);

        Element root = getResponse(url, localContext, sessionContext);

        return getListOfJobs("//job", root);
    }

    /**
     * Get a list of Jobs suggested by LinkedIn to the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedJobs(Map localContext, Map sessionContext) throws RTXException {

        return getSuggestedJobs(null, null, localContext, sessionContext);
    }

    /**
     * Get a list of Jobs suggested by LinkedIn to the current user
     * 
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getSuggestedJobs(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {
        String url = setUrlIndexParams(GET_SUGGESTED_JOBS_URL, count, start);
        Element root = getResponse(url, localContext, sessionContext);
        return getListOfJobs("//jobs/job", root);
    }

    /**
     * Mark a job as favourite
     * 
     * @param jobId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void bookmarkJob(String jobId, Map localContext, Map sessionContext) throws RTXException {
        Document doc = DocumentFactory.getInstance().createDocument();

        Element bookmark = doc.addElement("job-bookmark");
        Element job = bookmark.addElement("job");
        job.addElement("id").setText(jobId);

        sendRequestWithDoc(BOOKMARK_JOB_URL, doc, HttpPost.METHOD_NAME, localContext, sessionContext);
    }

    /**
     * Get the Jobs bookmarked by the current user
     * 
     * @param start
     * @param count
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getBookmarkedJobs(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {
        String url = setUrlIndexParams(GET_BOOKMARKED_JOBS_URL, count, start);
        Element root = getResponse(url, localContext, sessionContext);
        return getListOfJobs("//job-bookmark/job", root);
    }

    /**
     * Get the Jobs bookmarked by the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getBookmarkedJobs(Map localContext, Map sessionContext) throws RTXException {

        return getBookmarkedJobs(null, null, localContext, sessionContext);
    }

    /**
     * Get a Job details by Id
     * 
     * @param jobId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public LinkedInJob getJobById(String jobId, Map localContext, Map sessionContext) throws RTXException {

        String url = StringUtils.replace(GET_JOB_BY_ID_URL, "${job-id}", jobId);

        Element root = getResponse(url, localContext, sessionContext);

        return new LinkedInJob(root);

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
                String url = StringUtils.replace(USER_PROFILE_URL, "${USER-ID}", "~");
                ServiceConsumer serviceConsumer = new ServiceConsumer(url, authMgr);
                HttpResponse response = serviceConsumer.doGet();
                if (response != null) {
                    user = new LinkedInUser(HttpResponseReader.getDocumentElement(response), this);
                    sessionContext.put(CURRENT_USER_KEY, user);
                }
            }
        }
        return user;
    }

    /**
     * Get the connections of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getUserConnections(Integer start, Integer count, Map localContext, Map sessionContext) throws RTXException {
        List contacts = new ArrayList();
        try {

            String url = setUrlIndexParams(USER_CONNECTIONS_URL, count, start);

            Element root = getResponse(url, localContext, sessionContext);
            if (root != null) {
                for (Iterator iterator = root.selectNodes("//person").iterator(); iterator.hasNext();) {
                    contacts.add(new LinkedInContact((Element) iterator.next(), this));
                }
            }
        } catch (Exception e) {
            throw new RTXException("Unable to retrieve user's connections", e);
        }
        return contacts;
    }

    /**
     * Sends a GET Request to the specified URL, and returns the XML element in the response
     * 
     * @param url
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    private Element getResponse(String url, Map localContext, Map sessionContext) throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer consumer = new ServiceConsumer(url, authMgr);

        HttpResponse response = consumer.doGet();

        return HttpResponseReader.getDocumentElement(response);

    }

    /**
     * Sends a POST or PUT request to the specified URL, with the specified XML Document in the request body
     * 
     * @param url
     * @param doc
     * @param methodName
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    private void sendRequestWithDoc(String url, Document doc, String methodName, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        ServiceConsumer consumer = new ServiceConsumer(url, authMgr);

        consumer.send(doc, methodName);
    }

    /**
     * Reads a XML Document with a list of Jobs and returns a List of LinkedInJob objects
     * 
     * @param nodePath
     * @param root
     * @return
     */
    private List getListOfJobs(String nodePath, Element root) {

        List jobs = new ArrayList();

        for (Iterator iterator = root.selectNodes(nodePath).iterator(); iterator.hasNext();)
            jobs.add(new LinkedInJob((Element) iterator.next()));

        return jobs;
    }

    /**
     * Reads a XML Document with a list of Companies and returns a List of LinkedInCompany objects
     * 
     * @param nodePath
     * @param root
     * @return
     * @throws RTXException
     */
    private List getListOfCompanies(String nodePath, Element root) throws RTXException {

        List companies = new ArrayList();

        for (Iterator iterator = root.selectNodes(nodePath).iterator(); iterator.hasNext();)
            companies.add(new LinkedInCompany((Element) iterator.next(), this));

        return companies;

    }

    /**
     * Reads a XML Document with a list of Contacts and returns a List of LinkedInContact objects
     * 
     * @param nodePath
     * @param root
     * @return
     * @throws RTXException
     */
    private List getListOfContacts(String nodePath, Element root) throws RTXException {

        List contacts = new ArrayList();

        for (Iterator iterator = root.selectNodes(nodePath).iterator(); iterator.hasNext();)
            contacts.add(new LinkedInContact((Element) iterator.next(), this));

        return contacts;

    }

    /**
     * Reads a XML Document with a list of Groups and returns a List of LinkedInGroup objects
     * 
     * @param nodePath
     * @param root
     * @return
     * @throws RTXException
     */
    private List getListOfGroups(String nodePath, Element root) throws RTXException {

        List groups = new ArrayList();

        for (Iterator iterator = root.selectNodes(nodePath).iterator(); iterator.hasNext();)
            groups.add(new LinkedInGroup((Element) iterator.next(), this));

        return groups;
    }

    /**
     * Reads a XML Document with a list of Posts and returns a List of LinkedInPost objects
     * 
     * @param nodePath
     * @param root
     * @return
     */
    private List getListOfPosts(String nodePath, Element root) {

        List groups = new ArrayList();

        for (Iterator iterator = root.selectNodes(nodePath).iterator(); iterator.hasNext();)
            groups.add(new LinkedInPost((Element) iterator.next()));

        return groups;
    }

    /**
     * Set the parameters for pagination in the url
     * 
     * @param url
     * @param count
     *            Number of elements retrieved
     * @param start
     *            Offset
     * @return
     * @throws RTXException
     */
    private String setUrlIndexParams(String url, Integer count, Integer start) throws RTXException {
        url = StringUtils.replace(url, "${count}", (count != null) ? count.toString() : "20");
        url = StringUtils.replace(url, "${start}", (start != null) ? start.toString() : "0");
        return url;
    }

}
