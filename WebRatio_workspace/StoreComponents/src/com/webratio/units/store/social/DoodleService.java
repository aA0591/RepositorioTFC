package com.webratio.units.store.social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.webratio.rtx.RTXException;
import com.webratio.rtx.RTXManager;
import com.webratio.rtx.core.BeanHelper;
import com.webratio.units.store.commons.application.AbstractApplicationService;
import com.webratio.units.store.commons.application.ISocialNetworkService;
import com.webratio.units.store.commons.application.IUser;
import com.webratio.units.store.commons.auth.AccessToken;
import com.webratio.units.store.commons.auth.IAuthManager;
import com.webratio.units.store.commons.auth.OAuth1Manager;
import com.webratio.units.store.commons.auth.ServiceConsumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DoodleService extends AbstractApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.DOODLE_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.DOODLE_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL = "http://doodle.com/api1/oauth/requesttoken?doodle_get=name|email|initiatedPolls";
    private static final String ACCESS_TOKEN_URL = "http://doodle.com/api1/oauth/accesstoken";
    public static final String AUTHORIZE_TOKEN_URL = "http://doodle.com/api1/oauth/authorizeConsumer";

    /* API URLs */
    private static final String POLL_URL = "http://doodle.com/api1/polls/";
    private static final String USER_URL = "http://doodle.com/api1/user";

    private final String consumerKey;
    private final String consumerSecret;

    public DoodleService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);

        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);

        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));

        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for 'Doodle' the social network data provider")
                    .addHint("Set the API Key for the 'Doodle' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'Doodle' social network data provider")
                    .addHint("Set the Secret Key for the 'Doodle' social network data provider");
        }
    }

    public String computeAuthorizationUrl(String callbackUrl, Map localContext, Map sessionContext) throws RTXException {
        try {
            return locateOAuthManager(localContext, sessionContext).getAuthorizationUrl(callbackUrl);
        } catch (Exception e) {
            throw new RTXException("Unable to compute the authorization url", e);
        }
    }

    private IAuthManager locateOAuthManager(Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = (IAuthManager) sessionContext.get(OAUTH_MANAGER_KEY);
        if (authMgr == null) {

            String reqTokenUrl = StringUtils.replace(REQ_TOKEN_URL, "|", "%7C");

            authMgr = new OAuth1Manager(reqTokenUrl, ACCESS_TOKEN_URL, AUTHORIZE_TOKEN_URL, consumerKey, consumerSecret, this);
            sessionContext.put(OAUTH_MANAGER_KEY, authMgr);

        }
        return authMgr;
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
        // TODO Auto-generated method stub
        return null;
    }

    public String getHomePage() {

        return "http://www.doodle.com";
    }

    public IUser getUser(Map localContext, Map sessionContext) throws RTXException {
        // TODO Auto-generated method stub
        return null;
    }

    public List searchContacts(String keywords, Map localContext, Map sessionContext) throws RTXException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Create a Doodle Poll
     * 
     * @param poll
     * @param localContext
     * @param sessionContext
     * @return
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public DoodlePoll createDoodlePoll(DoodlePoll poll, Map localContext, Map sessionContext) throws DoodleServiceException,
            RTXException, IOException {

        HttpResponse response = null;

        try {

            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

            if (authMgr.isAuthorized()) {

                // construct the xml payload
                String payload = DoodleXMLManager.createPollXml(poll);

                ServiceConsumer consumer = new ServiceConsumer(POLL_URL, authMgr);

                Document document = DocumentHelper.parseText(payload);

                Map headers = new HashMap();
                headers.put("Content-Type", "application/xml; charset=UTF-8");

                response = consumer.send(document, HttpPost.METHOD_NAME, headers);

                StatusLine status = response.getStatusLine();
                // we expect 201 Created
                switch (status.getStatusCode()) {
                case HttpStatus.SC_CREATED:
                    break;
                // unexpected response code
                default: {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("Invalid response code: " + status.toString());
                }
                }
                Header contentLocationHeader = response.getFirstHeader("Content-Location");
                if (contentLocationHeader == null) {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("Poll ID was not returned by server");
                }
                String pollID = contentLocationHeader.getValue();
                Header xDoodleKeyHeader = response.getFirstHeader("X-DoodleKey");
                if (xDoodleKeyHeader == null) {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("8-character DoodleKey was not returned by server");
                }
                String adminKey = xDoodleKeyHeader.getValue();
                DoodlePoll pollReturn = new DoodlePoll(pollID, adminKey);

                return pollReturn;
            }

            return null;

        } catch (IOException ioEx) {
            throw new DoodleServiceException("Communication error occurred", ioEx);
        } catch (DocumentException e) {
            throw new RTXException(e);
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());

        }
    }

    /**
     * Update a doodle Poll
     * 
     * @param poll
     * @param localContext
     * @param sessionContext
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public void modify(DoodlePoll poll, Map localContext, Map sessionContext) throws DoodleServiceException, RTXException, IOException {
        HttpResponse response = null;
        try {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
            // create new PUT request
            if (authMgr.isAuthorized()) {

                String payload = DoodleXMLManager.createPollXml(poll);
                Document document = DocumentHelper.parseText(payload);

                ServiceConsumer consumer = new ServiceConsumer(POLL_URL + poll.getId(), authMgr);

                Map headers = new HashMap();
                headers.put("Content-Type", "application/xml; charset=UTF-8");
                headers.put("X-DoodleKey", poll.getAdminKey());

                response = consumer.send(document, HttpPut.METHOD_NAME, headers);

                StatusLine status = response.getStatusLine();
                // we expect 200 OK
                switch (status.getStatusCode()) {
                case HttpStatus.SC_OK:
                    break;
                case HttpStatus.SC_UNAUTHORIZED: {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("the keys do not match");
                    // unexpected response code
                }
                default: {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("Invalid response code: " + status.toString());
                }
                }
            }

        } catch (IOException ioEx) {
            throw new DoodleServiceException("Communication error occurred", ioEx);
        } catch (DocumentException e) {
            throw new RTXException(e);
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());

        }
    }

    /**
     * Vote on a Doodle Poll on behalf of the current user
     * 
     * @param poll
     * @param participant
     * @param localContext
     * @param sessionContext
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public void vote(DoodlePoll poll, DoodleParticipant participant, Map localContext, Map sessionContext)
            throws DoodleServiceException, RTXException, IOException {
        HttpResponse response = null;
        try {

            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

            if (authMgr.isAuthorized()) {
                HttpPost request;
                // ID denotes the poll�s 16-character identifier.

                request = new HttpPost(POLL_URL + poll.getId() + "/participants");

                // construct the xml payload
                String payload = DoodleXMLManager.createVoteXml(participant);
                Document document = DocumentHelper.parseText(payload);
                ServiceConsumer consumer = new ServiceConsumer(POLL_URL + poll.getId() + "/participants", authMgr);

                Map headers = new HashMap();
                headers.put("Content-Type", "application/xml");

                response = consumer.send(document, HttpPost.METHOD_NAME, headers);
                StatusLine status = response.getStatusLine();

                // we expect 201 Created
                switch (status.getStatusCode()) {
                case HttpStatus.SC_CREATED:
                    break;
                case HttpStatus.SC_CONFLICT: {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        StringBuffer builder = new StringBuffer();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line).append(System.getProperty("line.separator"));
                        }
                        if (builder.indexOf("participant with name") >= 0) {
                            EntityUtils.consume(response.getEntity());
                            throw new DoodleServiceException("Participant with name " + participant.getName() + " already exists.");
                        } else if (builder.indexOf("row constraint") >= 0) {
                            EntityUtils.consume(response.getEntity());
                            throw new DoodleServiceException("Row constraint exceeded. You may select only one option.");
                        } else {
                            EntityUtils.consume(response.getEntity());
                            throw new DoodleServiceException("Column constraint exceeded.");
                        }
                    }
                }
                    // unexpected response code
                default: {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("Invalid response code: " + status.toString());
                }
                }

            }
        } catch (IOException ioEx) {
            throw new DoodleServiceException("Communication error occurred", ioEx);
        } catch (DocumentException e) {
            throw new RTXException(e);
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Comment on a Doodle Poll on behalf of the current user
     * 
     * @param poll
     * @param comment
     * @param localContext
     * @param sessionContext
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public void comment(DoodlePoll poll, DoodleComment comment, Map localContext, Map sessionContext) throws DoodleServiceException,
            RTXException, IOException {
        HttpResponse response = null;

        try {
            // make oauth handshake to authorize consumer
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

            if (authMgr.isAuthorized()) {
                HttpPost request;
                // ID denotes the poll�s 16-character identifier.

                request = new HttpPost(POLL_URL + poll.getId() + "/comments");

                // construct the xml payload
                String payload = DoodleXMLManager.createCommentsXml(comment);
                Document document = DocumentHelper.parseText(payload);
                ServiceConsumer consumer = new ServiceConsumer(POLL_URL + poll.getId() + "/comments", authMgr);

                Map headers = new HashMap();
                headers.put("Content-Type", "application/xml");

                response = consumer.send(document, HttpPost.METHOD_NAME, headers);
                StatusLine status = response.getStatusLine();

                // we expect 201 Created
                switch (status.getStatusCode()) {
                case HttpStatus.SC_CREATED:
                    break;
                // unexpected response code
                default: {
                    EntityUtils.consume(response.getEntity());
                    throw new DoodleServiceException("Invalid response code: " + status.toString());
                }
                }
            }
        } catch (IOException ioEx) {
            throw new DoodleServiceException("Communication error occurred", ioEx);
        } catch (DocumentException e) {
            throw new RTXException(e);
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());

        }
    }

    /**
     * Get the Polls of the current user
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     * @throws DoodleServiceException
     * @throws IOException
     */
    public DoodleUser getUserPolls(Map localContext, Map sessionContext) throws RTXException, DoodleServiceException, IOException {

        HttpResponse response = null;
        try {
            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

            if (authMgr.isAuthorized()) {

                ServiceConsumer consumer = new ServiceConsumer(USER_URL, authMgr);
                response = consumer.doGet();

                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                SAXReader reader = new SAXReader();
                Document doc = reader.read(stream);

                if (doc == null) {
                    return null;
                }

                DoodleUser user = DoodleXMLManager.getUserFromDocument(doc);

                return user;
            }

        } catch (ClientProtocolException e) {

            throw new DoodleServiceException();
        } catch (IOException e) {
            throw new DoodleServiceException();
        } catch (DocumentException e) {

            throw new DoodleServiceException();
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());
        }
        return null;
    }

    /**
     * Get a Poll details by Id
     * 
     * @param pollId
     * @param doodleKey
     * @param localContext
     * @param sessionContext
     * @return
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public DoodlePoll getPollById(String pollId, String doodleKey, Map localContext, Map sessionContext)
            throws DoodleServiceException, RTXException, IOException {
        HttpResponse response = null;
        try {

            IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

            if (authMgr.isAuthorized()) {

                HttpGet request = new HttpGet(POLL_URL + pollId);

                // execute the request
                Map headers = new HashMap();
                headers.put("X-DoodleKey", doodleKey);

                ServiceConsumer consumer = new ServiceConsumer(POLL_URL + pollId, authMgr);
                response = consumer.doGet(null, headers);
                StatusLine status = response.getStatusLine();

                switch (status.getStatusCode()) {
                case HttpStatus.SC_OK:
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    throw new DoodleServiceException("no such poll");
                case HttpStatus.SC_GONE:
                    throw new DoodleServiceException("the poll has been deleted");
                default: {
                    throw new DoodleServiceException("Invalid response code: " + status.toString());
                }
                }

                HttpEntity entity = response.getEntity();
                // parse response body
                InputStream inentity = entity.getContent();
                SAXReader reader = new SAXReader();
                Document doc = reader.read(inentity);
                if (doc == null)
                    return null;
                // construct Poll object from the parsed document
                DoodlePoll result = DoodleXMLManager.getPollFromDocument(pollId, doodleKey, doc);
                return result;
            }

        } catch (ParseException pEx) {
            throw new DoodleServiceException("Parse error occurred", pEx);
        } catch (IOException ioEx) {
            throw new DoodleServiceException("Communication error occurred", ioEx);
        } catch (DocumentException e) {
            throw new RTXException(e);
        } finally {
            if (response != null)
                EntityUtils.consume(response.getEntity());

        }
        return null;
    }

    /**
     * Close a Poll
     * 
     * @param pollId
     * @param adminKey
     * @param selection
     * @param localContext
     * @param sessionContext
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public void close(String pollId, String adminKey, List selection, Map localContext, Map sessionContext)
            throws DoodleServiceException, RTXException, IOException {
        DoodlePoll poll = getPollById(pollId, adminKey, localContext, sessionContext);
        List options = poll.getOptions();
        DoodleState state = DoodleState.CLOSED;

        for (int i = 0; i < selection.size(); i++) {
            if ((poll.getLevels() == DoodleLevel.TWO && ((Integer) selection.get(i)).intValue() == 1)
                    || (poll.getLevels() == DoodleLevel.THREE && ((Integer) selection.get(i)).intValue() == 2))
                ((DoodleOption) options.get(i)).setFinal_(true);

        }

        DoodlePoll pollclose = new DoodlePoll(pollId, adminKey, poll, state, options);
        this.modify(pollclose, localContext, sessionContext);

    }

    /**
     * Delete a poll
     * 
     * @param pollId
     * @param doodleKey
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     * @throws DoodleServiceException
     */
    public void deletePoll(String pollId, String doodleKey, Map localContext, Map sessionContext) throws RTXException,
            DoodleServiceException {

        HttpResponse response = null;
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {
            ServiceConsumer consumer = new ServiceConsumer(POLL_URL + pollId, authMgr);

            Map headers = new HashMap();
            headers.put("X-DoodleKey", doodleKey);

            response = consumer.doDelete(headers);
            StatusLine status = response.getStatusLine();

            switch (status.getStatusCode()) {
            case HttpStatus.SC_NO_CONTENT:
                break;
            case HttpStatus.SC_UNAUTHORIZED:
                throw new DoodleServiceException("Keys do not match");
            case HttpStatus.SC_BAD_GATEWAY:
                throw new DoodleServiceException("Calendar Synchronization failed");
            default:
                throw new DoodleServiceException("Unvalid response status: " + status.toString());
            }

        }
    }

    /**
     * Get the participants of a Poll
     * 
     * @param pollId
     * @param doodleKey
     * @param localContext
     * @param sessionContext
     * @return
     * @throws DoodleServiceException
     * @throws RTXException
     * @throws IOException
     */
    public List getParticipants(String pollId, String doodleKey, Map localContext, Map sessionContext) throws DoodleServiceException,
            RTXException, IOException {

        DoodlePoll poll = getPollById(pollId, doodleKey, localContext, sessionContext);

        return poll.getParticipants();

    }

}
