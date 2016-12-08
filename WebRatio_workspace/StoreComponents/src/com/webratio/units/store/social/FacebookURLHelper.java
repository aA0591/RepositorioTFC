package com.webratio.units.store.social;

public class FacebookURLHelper {

    private static final String GRAPH_API_BASE_URL = "https://graph.facebook.com/v2.3/";

    /**
     * Create url for accessing a Facebook object by its Id
     * 
     * @param objectId
     * @return
     */
    public static String createUrl(String objectId) {
        return GRAPH_API_BASE_URL + objectId;
    }

    /**
     * Create a url for accessing a Facebook object connection see definition of connection at:
     * https://developers.facebook.com/docs/reference/api/
     * 
     * @param objectId
     * @param connection
     * @return
     */
    public static String createUrl(String objectId, FacebookConnectionType connection) {
        return GRAPH_API_BASE_URL + objectId + "/" + connection.getValue();
    }

    /**
     * Create url for accessing a Facebook object connection with a target Facebook object for example, get the mutual friends with a
     * determined user
     * 
     * @param objectId
     * @param targetId
     * @param connection
     * @return
     */
    public static String createUrl(String objectId, String targetId, FacebookConnectionType connection) {
        return GRAPH_API_BASE_URL + objectId + "/" + connection.getValue() + "/" + targetId;
    }

    /**
     * Create url for accessing a Facebook object connection with a target Facebook object for example, get the mutual friends with a
     * determined user
     * 
     * @param objectId
     * @param targetId
     * @param connection
     * @param fields
     * @return
     */
    public static String createUrl(String objectId, String targetId, FacebookConnectionType connection, String[] fields) {
        return GRAPH_API_BASE_URL + objectId + "/" + connection.getValue() + "/" + targetId + getQuery(fields);
    }

    /**
     * Create a url for accessing a Facebook object connection. See definition of connection at:
     * https://developers.facebook.com/docs/reference/api/
     * 
     * @param objectId
     * @param connection
     * @param fields
     * @return
     */
    public static String createUrl(String objectId, FacebookConnectionType connection, String[] fields) {

        return GRAPH_API_BASE_URL + objectId + "/" + connection.getValue() + getQuery(fields);

    }

    /**
     * creates a query string from the array of desired fields
     * 
     * @param fields
     * @return
     */
    private static String getQuery(String[] fields) {
        String fieldQuery = "?fields=";
        StringBuffer sb = new StringBuffer();

        sb.append(fieldQuery);
        sb.append(fields[0]);
        if (fields.length > 1) {
            for (int i = 1; i < fields.length; i++) {
                sb.append("," + fields[i]);
            }
        }

        return sb.toString();
    }

}
