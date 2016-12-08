package com.webratio.units.store.googlegeocodingunit;

import com.webratio.rtx.beans.BasicContentUnitBean;

public class GoogleGeocodingUnitBean extends BasicContentUnitBean {

    public static String OK = "success";
    public static String KO = "error";

    private String address = "";
    private String latitude = "";
    private String longitude = "";
    private String firstValidAddress = "";
    private String city = "";
    private String stateOrProvince = "";
    private String country = "";
    private String fullAddress = "";
    private String resultCode = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMapCoordinates() {
        return ((this.latitude != null) && (this.latitude.length() > 0) && (this.longitude != null) && (this.longitude.length() > 0)) ? this.latitude
                + "," + this.longitude
                : null;
    }

    public void setMapCoordinates(String mapCoordinates) {
        if ((mapCoordinates != null) && (mapCoordinates.length() > 0)) {
            int sep = mapCoordinates.indexOf(',');
            this.latitude = mapCoordinates.substring(0, sep);
            this.longitude = mapCoordinates.substring(sep + 1);
        }
    }

    public String getFirstValidAddress() {
        return firstValidAddress;
    }

    public void setFirstValidAddress(String firstValidAddress) {
        this.firstValidAddress = firstValidAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getFirstAddr() {
        return this.firstValidAddress;
    }

    public String getLat() {
        return this.latitude;
    }

    public String getLong() {
        return this.longitude;
    }

    public String getCoords() {
        return this.latitude + "," + this.longitude;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
