package com.kursat.pm_projectmarket.Model;

public class User {
    private  String id;
    private  String Name;
    private  String email;
    private  String profileImageUrl;
    private  String bio;


    public User() {
    }

    public User(String id, String name, String email, String profileImageUrl, String bio) {
        this.id = id;
        Name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
