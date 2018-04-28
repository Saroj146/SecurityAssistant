package com.pwds.ultimate.securityassistant.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Ultimate on 4/25/2018.
 */

public class LoginDto {
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("username")
    @Expose
    public String username;

    public LoginDto(String username, String password) {
        this.password = password;
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
