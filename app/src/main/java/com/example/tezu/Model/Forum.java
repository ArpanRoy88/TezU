package com.example.tezu.Model;

import com.google.firebase.database.ServerValue;

public class Forum {

        String forumID;
        String title;
        String description;
        String userId;
        Object timestamp;

    public Forum() {
    }

    public String getForumID() {
        return forumID;
    }

    public void setForumID(String forumID) {
        this.forumID = forumID;
    }

    public Forum(String forumID, String title, String description, String userId, String timestamp) {
        this.forumID = forumID;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getPostID() {
        return forumID;
    }

    public void setPostID(String forumID) {
        this.forumID = forumID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
