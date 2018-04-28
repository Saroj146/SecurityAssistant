package com.pwds.ultimate.securityassistant.Model;

/**
 * Created by Ultimate on 4/27/2018.
 */

public class CommunityPostDto {
    private String body, title;

    public CommunityPostDto(String title, String body){
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
