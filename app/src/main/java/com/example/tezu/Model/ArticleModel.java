package com.example.tezu.Model;

public class ArticleModel {

    private String articleTitle,articleDate,articleBody;
    private String articleAuthor,articleDepartment;

    public ArticleModel() {
    }

    public ArticleModel(String articleTitle, String articleDate, String articleBody, String articleAuthor, String articleDepartment) {
        this.articleTitle = articleTitle;
        this.articleDate = articleDate;
        this.articleBody = articleBody;
        this.articleAuthor = articleAuthor;
        this.articleDepartment = articleDepartment;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleDepartment() {
        return articleDepartment;
    }

    public void setArticleDepartment(String articleDepartment) {
        this.articleDepartment = articleDepartment;
    }
}
