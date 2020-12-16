package com.example.tezu.Model;


public class model{


    private String file,magazineTitle;
    private String magazinePublisher,semester;



    public model() {

    }


    public model(String file, String magazineTitle, String magazinePublisher, String semester) {
        this.file = file;
        this.magazineTitle = magazineTitle;
        this.magazinePublisher = magazinePublisher;
        this.semester = semester;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMagazinePublisher() {
        return magazinePublisher;
    }

    public void setMagazinePublisher(String magazinePublisher) {
        this.magazinePublisher = magazinePublisher;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getMagazineTitle() {
        return magazineTitle;
    }

    public void setMagazineTitle(String magazineTitle) {
        this.magazineTitle = magazineTitle;
    }
}
