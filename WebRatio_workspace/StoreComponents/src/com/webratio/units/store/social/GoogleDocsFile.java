package com.webratio.units.store.social;

import java.util.List;

import org.json.JSONObject;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;

/**
 * 
 * @author Felipe Bean class representing a Document or a Folder on GoogleDocs
 *         Reference:https://developers.google.com/drive/v2/reference/files
 */

@SuppressWarnings("rawtypes")
public class GoogleDocsFile {

    private String title;
    private String docId;
    private String description;
    private DateTime createdDate;
    private String author;
    private String lastModifiedBy;
    private Boolean writersCanInvite;
    private String link;
    private String downloadUrl;
    private String downloadPdfUrl;
    private String type;
    private DateTime modifiedDate;
    private DateTime lastViewedByMe;
    private Boolean starred;
    private Boolean trashed;
    private Boolean hidden;
    private String embedLink;
    private List owners;
    private Long fileSize;
    private List parentFolders;
    private Boolean isFolder;

    public static String getFolderMimeType() {
        return FOLDER_MIME_TYPE;
    }

    private final static String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    public GoogleDocsFile(JSONObject jsonObject) {
        title = jsonObject.optString("title");
        docId = jsonObject.optString("id");
        description = jsonObject.optString("description");
        writersCanInvite = new Boolean(jsonObject.getBoolean("writersCanShare"));
        link = jsonObject.optString("alternateLink");
        type = jsonObject.optString("mimeType");
        if (type.equals(FOLDER_MIME_TYPE))
            this.isFolder = Boolean.TRUE;
        else
            this.isFolder = Boolean.FALSE;
        downloadUrl = jsonObject.optString("downloadUrl");
        modifiedDate = new DateTime(jsonObject.optString("modifiedDate"));
        starred = Boolean.valueOf(jsonObject.optBoolean("labels.starred"));
        trashed = Boolean.valueOf(jsonObject.optBoolean("labels.trashed"));
        hidden = Boolean.valueOf(jsonObject.optBoolean("labels.hidden"));
        embedLink = jsonObject.optString("embedLink");
    }

    public GoogleDocsFile(File file) {

        this.title = file.getTitle();
        this.docId = file.getId();
        this.type = file.getMimeType();
        if (type.equals(FOLDER_MIME_TYPE))
            this.isFolder = Boolean.TRUE;
        else
            this.isFolder = Boolean.FALSE;
        this.link = file.getAlternateLink();
        this.createdDate = file.getCreatedDate();
        this.downloadUrl = file.getDownloadUrl();
        this.description = file.getDescription();
        this.embedLink = file.getEmbedLink();
        this.lastModifiedBy = file.getLastModifyingUserName();
        this.hidden = file.getLabels().getHidden();
        this.starred = file.getLabels().getStarred();
        this.trashed = file.getLabels().getTrashed();
        this.owners = file.getOwnerNames();
        this.fileSize = file.getFileSize();
        this.parentFolders = file.getParents();

    }

    public List getOwners() {
        return owners;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public List getParentFolders() {
        return parentFolders;
    }

    public String getTitle() {
        return title;
    }

    public String getDocId() {
        return docId;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Boolean getWritersCanInvite() {
        return writersCanInvite;
    }

    public String getLink() {
        return link;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDownloadPdfUrl() {
        return downloadPdfUrl;
    }

    public String getType() {
        return type;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public DateTime getLastViewedByMe() {
        return lastViewedByMe;
    }

    public Boolean getStarred() {
        return starred;
    }

    public Boolean getTrashed() {
        return trashed;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public String getEmbedLink() {
        return embedLink;
    }

    public Boolean isFolder() {
        return isFolder;
    }

}
