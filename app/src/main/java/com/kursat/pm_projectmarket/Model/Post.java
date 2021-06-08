package com.kursat.pm_projectmarket.Model;
public class Post {
    private String userName;
    private String userId;
    private String price;
    private String title;
    private String postImageUrl;
    private String token;
    private String profileImage;

    public Post(String userId,String userName, String price, String title, String postImageUrl,String profileImage,String token) {
        this.userName = userName;
        this.userId = userId;
        this.price = price;
        this.title = title;
        this.postImageUrl = postImageUrl;
        this.token=token;
        this.profileImage=profileImage;
    }
    Post(){

    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getToken() {
        return token;
    }
}
