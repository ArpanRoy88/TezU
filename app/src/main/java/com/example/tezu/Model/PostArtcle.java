package com.example.tezu.Model;

public class PostArtcle {
    public String title;
    public String url;
    public String semester;
    public String date;
    public String publisher;
    public String user_id;


    public PostArtcle(String title, String url, String semester, String date, String publisher, String user_id) {
        this.title = title;
        this.url = url;
        this.semester = semester;
        this.date = date;
        this.publisher = publisher;
        this.user_id = user_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
