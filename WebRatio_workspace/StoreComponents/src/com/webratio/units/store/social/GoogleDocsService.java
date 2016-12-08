package com.webratio.units.store.social;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentList;
import com.google.api.services.drive.model.ParentReference;
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
public class GoogleDocsService extends AbstractApplicationService implements ISocialNetworkService {

    /* context keys */
    private static final String OAUTH_MANAGER_KEY = "com.webratio.rtx.GOOGLEDOCS_OAUTH_MANAGER";
    private static final String CURRENT_USER_KEY = "com.webratio.rtx.GOOGLEDOCS_CURRENT_USER";

    /* OAuth URLs */
    private static final String REQ_TOKEN_URL = "https://accounts.google.com/o/oauth2/auth[response_type=code|scope=https://docs.google.com/feeds/ https://www.googleapis.com/auth/drive https://docs.googleusercontent.com/ https://spreadsheets.google.com/feeds/ https://www.googleapis.com/auth/userinfo.profile]";
    private static final String ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token[grant_type=authorization_code]";

    /* API URLs */
    private static final String USER_PROFILE_URL = "https://www.googleapis.com/oauth2/v1/userinfo[access_token=${ACCESS_TOKEN}]";

    private static final String USER_FILES = "https://www.googleapis.com/drive/v2/files/${fileId}";

    /* OAuth API keys */
    private final String consumerKey;
    private final String consumerSecret;

    private Drive service;

    private static final Map mimeTypes = new HashMap() {
        {
            put("doc", "application/msword");
            put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            put("xls", "application/vnd.ms-excel");
            put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            put("ppt", "application/vnd.ms-powerpoint");
            put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            put("odt", "application/vnd.oasis.opendocument.text, application/x-vnd.oasis.opendocument.text");
            put("ods", "application/vnd.oasis.opendocument.spreadsheet, application/x-vnd.oasis.opendocument.spreadsheet");
            put("pdf", "application/pdf");
            put("pages", "application/x-iwork-pages-sffpages");
            put("ai", "application/illustrator");
            put("psd", "image/vnd.adobe.photoshop");
            put("tiff", "image/tiff");
            put("svg", "image/svg");
            put("png", "image/png");
            put("eps", "application/eps");
            put("ps", "application/postscript");
            put("xps", "application/vnd.ms-xpsdocument");
            put("zip", "application/zip");
            put("rar", "application/x-rar-compressed");
        }
    };

    private static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    public GoogleDocsService(String id, RTXManager rtx, Element descr) throws RTXException {
        super(id, rtx, descr);
        Map serviceDataProviderInfo = rtx.getModelService().getServiceDataProviderInfo(id);
        consumerKey = BeanHelper.asString(serviceDataProviderInfo.get("apiKey"));
        consumerSecret = BeanHelper.asString(serviceDataProviderInfo.get("secretKey"));
        if (StringUtils.isBlank(consumerKey)) {
            throw new RTXException("Invalid API Key for 'GoogleDocs' the social network data provider")
                    .addHint("Set the API Key for the 'GoogleDocs' social network data provider");
        }
        if (StringUtils.isBlank(consumerSecret)) {
            throw new RTXException("Invalid Secret Key for the 'GoogleDocs' social network data provider")
                    .addHint("Set the Secret Key for the 'GoogleDocs' social network data provider");
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

            createGoogleCredentials(authMgr.getAccessToken());

            return authMgr.getAccessToken();

        } catch (Exception e) {
            throw new RTXException(e);
        }
    }

    private void createGoogleCredentials(AccessToken token) {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential().setAccessToken(token.getValue());

        service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

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

        return "http://www.docs.google.com";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.webratio.rtx.core.AbstractService#getName()
     */
    public String getName() {
        return "GoogleDocs";
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
                    user = new GoogleDocsUser(HttpResponseReader.getJSonObject(response), this);
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
     * Get the files(Documents and Folders) inside the root folder
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getRootFolderContents(Map localContext, Map sessionContext) throws RTXException {
        return getFolderContents("root", localContext, sessionContext);
    }

    /**
     * Get the files(Documents and Folders) inside the specified folder
     * 
     * @param folderId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFolderContents(String folderId, Map localContext, Map sessionContext) throws RTXException {
        List files = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        try {
            if (authMgr.isAuthorized() && service != null) {
                // Get the list of references for each file
                Children.List request = service.children().list(folderId);
                ChildList children = request.execute();

                // for each file id, get the details

                for (int i = 0; i < children.getItems().size(); i++) {
                    ChildReference child = (ChildReference) children.getItems().get(i);
                    File file = service.files().get(child.getId()).execute();
                    files.add(new GoogleDocsFile(file));
                }
            } else {
                throw new RTXException("Not yet authorized");
            }

        } catch (IOException e) {
            throw new RTXException(e);
        }

        return files;
    }

    /**
     * Create a new folder on the specified parent folder(root if is null)
     * 
     * @param name
     * @param description
     * @param parentId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String createNewFolder(String name, String description, String parentId, Map localContext, Map sessionContext)
            throws RTXException {

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        String folderId = null;
        if (authMgr.isAuthorized() && service != null) {
            File folder = new File();
            folder.setTitle(name);
            folder.setDescription(description);

            if (parentId != null && !parentId.equals(""))
                folder.setParents(Arrays.asList(new ParentReference[] { new ParentReference().setId(parentId) }));

            String mimeType = FOLDER_MIME_TYPE;

            folder.setMimeType(mimeType);

            try {
                File sentFolder = service.files().insert(folder).execute();

                folderId = sentFolder.getId();

            } catch (IOException e) {
                throw new RTXException(e);
            }

        }

        return folderId;

    }

    /**
     * Get all the folders
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFolders(Map localContext, Map sessionContext) throws RTXException {
        List folders = new ArrayList();
        List files = getFilesByAPI(localContext, sessionContext);
        for (int i = 0; i < files.size(); i++) {
            GoogleDocsFile file = (GoogleDocsFile) files.get(i);
            if (file.isFolder().booleanValue())
                folders.add(file);
        }

        return folders;
    }

    /**
     * Get the parent folders of the specified file(Folder or Document)
     * 
     * @param fileId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getParentFolders(String fileId, Map localContext, Map sessionContext) throws RTXException {
        List folders = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        try {
            if (authMgr.isAuthorized() && service != null) {
                ParentList parents = service.parents().list(fileId).execute();
                for (int i = 0; i < parents.size(); i++) {
                    ParentReference parent = (ParentReference) parents.getItems().get(i);
                    File folder = service.files().get(parent.getId()).execute();
                    folders.add(new GoogleDocsFile(folder));
                }
            } else {
                throw new RTXException("Not yet authorized");
            }

        } catch (IOException e) {
            throw new RTXException(e);
        }
        return folders;
    }

    /**
     * Put a File(Document or Folder) into the specified folder
     * 
     * @param fileId
     * @param targetFolderId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void insertFileIntoFolder(String fileId, String targetFolderId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (targetFolderId == null)
            targetFolderId = "root";
        try {
            if (authMgr.isAuthorized() && service != null) {
                ChildReference child = new ChildReference();
                child.setId(fileId);

                service.children().insert(targetFolderId, child).execute();
            } else {
                throw new RTXException("Not yet authorized");
            }
        } catch (IOException e) {
            throw new RTXException(e);
        }
    }

    /**
     * Move the file(Document or Folder) to the target folder
     * 
     * @param fileId
     * @param originalFolderId
     * @param targetFolderId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void moveFileToFolder(String fileId, String originalFolderId, String targetFolderId, Map localContext, Map sessionContext)
            throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (originalFolderId == null)
            originalFolderId = "root";
        if (targetFolderId == null)
            targetFolderId = "root";

        try {
            if (authMgr.isAuthorized() && service != null) {
                // remove the file from the original folder
                service.children().delete(originalFolderId, fileId).execute();

                // insert the file into the target folder
                ChildReference child = new ChildReference();
                child.setId(fileId);
                service.children().insert(targetFolderId, child).execute();
            } else {
                throw new RTXException("Not yet authenticated");
            }

        } catch (IOException e) {
            throw new RTXException(e);
        }
    }

    /**
     * Remove the specified file(document or folder) from the target folder
     * 
     * @param fileId
     * @param targetFolderId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void removeFileFromFolder(String fileId, String targetFolderId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (targetFolderId == null)
            targetFolderId = "root";
        try {
            if (authMgr.isAuthorized() && service != null) {
                service.children().delete(targetFolderId, fileId).execute();
            } else {
                throw new RTXException("Not yet authorized");
            }
        } catch (IOException e) {
            throw new RTXException(e);
        }
    }

    /**
     * Get all the Files(Documents and Folder) using the REST API
     * 
     * @param title
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFilesByUrl(String title, Map localContext, Map sessionContext) throws RTXException {

        List documents = new ArrayList();
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        if (authMgr.isAuthorized()) {
            ServiceConsumer consumer = new ServiceConsumer(USER_FILES, authMgr);
            HttpResponse response = consumer.doGet();
            if (response != null) {
                JSONObject jsonObject = HttpResponseReader.getJSonObject(response);
                logError("jsonResponse\n" + jsonObject.toString(), new Exception());
                JSONArray jsonArray = jsonObject.optJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    documents.add(new GoogleDocsFile(jsonArray.getJSONObject(i)));
                }
            }
        }

        return documents;
    }

    /**
     * Get all the files (Documents and folders) using the Drive API
     * 
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public List getFilesByAPI(Map localContext, Map sessionContext) throws RTXException {

        List documents = new ArrayList();
        List files = new ArrayList();

        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        try {
            if (authMgr.isAuthorized() && service != null) {
                Files.List request = service.files().list();
                FileList requestFiles = request.execute();
                files.addAll(requestFiles.getItems());
                request.setPageToken(requestFiles.getNextPageToken());
                for (int i = 0; i < files.size(); i++) {
                    File currentFile = (File) files.get(i);
                    documents.add(new GoogleDocsFile(currentFile));
                }
            } else {
                throw new RTXException("Not yet authenticated");
            }
        } catch (IOException e) {
            throw new RTXException(e);
        }
        return documents;
    }

    /**
     * Upload a Document
     * 
     * @param docStream
     * @param documentTitle
     * @param documentDescription
     * @param fileExtension
     * @param fileName
     * @param parentId
     * @param convertDocument
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String uploadFile(InputStream docStream, String documentTitle, String documentDescription, String fileExtension,
            String fileName, String parentId, Boolean convertDocument, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);

        String redirectURL = null;
        if (authMgr.isAuthorized() && service != null) {

            /*
             * Set upload file data
             */
            File uploadFile = new File();
            uploadFile.setTitle(documentTitle);
            uploadFile.setDescription(documentDescription);

            if (parentId != null)
                uploadFile.setParents(Arrays.asList(new ParentReference[] { new ParentReference().setId(parentId) }));

            String mimeType = (mimeTypes.containsKey(fileExtension)) ? mimeTypes.get(fileExtension).toString()
                    : "application/octet-stream";

            uploadFile.setMimeType(mimeType);

            java.io.File fileContent;
            try {
                /*
                 * Set the file content
                 */
                fileContent = java.io.File.createTempFile("com.webratio.store.googledocs", ".file");
                FileOutputStream fout = new FileOutputStream(fileContent);

                IOUtils.copy(docStream, fout);

                FileContent mediaContent = new FileContent(mimeType, fileContent);

                /*
                 * Make upload request
                 */
                File sentFile = service.files().insert(uploadFile, mediaContent).setConvert(convertDocument).execute();

                redirectURL = sentFile.getAlternateLink();

            } catch (IOException e) {
                throw new RTXException(e);
            }

        }

        return redirectURL;

    }

    /**
     * Get the details about the specified File (Document or Folder)
     * 
     * @param fileId
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public GoogleDocsFile getFile(String fileId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized() && service != null) {
            try {
                File file = service.files().get(fileId).execute();
                GoogleDocsFile gdocsFile = new GoogleDocsFile(file);

                return gdocsFile;

            } catch (IOException e) {

                throw new RTXException(e);
            }
        }
        return null;

    }

    /**
     * Update the metadata of a File(Document or Folder)
     * 
     * @param fileId
     * @param newTitle
     * @param newDescription
     * @param newStarred
     * @param localContext
     * @param sessionContext
     * @return
     * @throws RTXException
     */
    public String updateFile(String fileId, String newTitle, String newDescription, Boolean newStarred, Map localContext,
            Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        if (authMgr.isAuthorized() && service != null) {

            try {
                File file = service.files().get(fileId).execute();

                file.setTitle(newTitle);
                file.setDescription(newDescription);
                file.getLabels().setStarred(newStarred);

                File updateFile = service.files().update(fileId, file).execute();

                return updateFile.getAlternateLink();

            } catch (IOException e) {

                throw new RTXException(e);
            }

        }

        return null;
    }

    /**
     * Delete a File (Document or Folder) from Google Drive
     * 
     * @param fileId
     * @param localContext
     * @param sessionContext
     * @throws RTXException
     */
    public void deleteFile(String fileId, Map localContext, Map sessionContext) throws RTXException {
        IAuthManager authMgr = locateOAuthManager(localContext, sessionContext);
        try {
            if (authMgr.isAuthorized() && service != null)
                service.files().delete(fileId).execute();
            else
                throw new RTXException("Not yet authorized");
        } catch (IOException e) {
            throw new RTXException(e);
        }

    }

}
