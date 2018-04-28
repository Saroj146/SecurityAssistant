package com.pwds.ultimate.securityassistant.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ultimate on 4/28/2018.
 */

public class RegisterDto {
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("contactPerson")
    @Expose
    public String contactPerson;
    @SerializedName("contactPersonNo")
    @Expose
    public String contactPersonNo;
    @SerializedName("userType")
    @Expose
    public String userType;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPersonNo() {
        return contactPersonNo;
    }

    public String getUserType() {
        return userType;
    }

    public Double getLat() { return lat; }

    public Double getLng() { return lng; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonNo(String contactPersonNo) { this.contactPersonNo = contactPersonNo; }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public void setLng(Double lng) { this.lng = lng; }
}
