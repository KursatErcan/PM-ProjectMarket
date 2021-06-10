package com.kursat.pm_projectmarket.Model;

public class Comment {

    private String postTitle;
    private String comment;
    String score;
    String token;


    public Comment(String postTitle, String comment,String score,String token) {
        this.postTitle = postTitle;
        this.comment = comment;
        this.score = score;
        this.token = token;
    }

    public String getScore() {
        return score;
    }

    public String getToken() {
        return token;
    }

    public String getPostTitle() { return postTitle; }

    public String getComment() { return comment;
    }
}
