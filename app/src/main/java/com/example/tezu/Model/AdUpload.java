package com.example.tezu.Model;

public class AdUpload {

    private String adTitle, adDesc, adCategory, adPrice , adImage , adUserId, adUploadId;

    public AdUpload() {

    }

    public AdUpload(String adTitle, String adDesc, String adCategory, String adPrice, String adImage , String adUserId, String adUploadId) {

        if (adTitle.trim().equals(""))
        {
            adTitle = "No Name";
        }
        if (adPrice.trim().equals(""))
        {
            adPrice = "0";
        }

        this.adTitle = adTitle;
        this.adDesc = adDesc;
        this.adCategory = adCategory;
        this.adPrice = adPrice;
        this.adImage = adImage;
        this.adUserId = adUserId;
        this.adUploadId = adUploadId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdDesc() {
        return adDesc;
    }

    public void setAdDesc(String adDesc) {
        this.adDesc = adDesc;
    }

    public String getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(String adCategory) {
        this.adCategory = adCategory;
    }

    public String getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(String adPrice) {
        this.adPrice = adPrice;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(String adUserId) {
        this.adUserId = adUserId;
    }

    public String getAdUploadId() {
        return adUploadId;
    }

    public void setAdUploadId(String adUploadId) {
        this.adUploadId = adUploadId;
    }
}
