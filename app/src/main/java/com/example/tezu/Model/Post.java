package com.example.tezu.Model;

public class Post {
    String postid;
    String postevent;
    String description;
    String publisher;
    String title;
    String date_time;
    String location;

    public Post(String postid, String postevent, String description, String publisher, String title, String date_time, String location) {
        this.postid = postid;
        this.postevent = postevent;
        this.description = description;
        this.publisher = publisher;
        this.title = title;
        this.date_time = date_time;
        this.location = location;

    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostevent() {
        return postevent;
    }

    public void setPostevent(String postevent) {
        this.postevent = postevent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

