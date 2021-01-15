package com.kursat.pm_projectmarket.Model;
public class Post {
    private String postId;
    private String userId;
    private String price;
    private String title;
    private String postImage;

    public Post(){

    }

    public Post(String postId, String userId, String price, String title, String postImage) {
        this.postId = postId;
        this.userId = userId;
        this.price = price;
        this.title = title;
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
