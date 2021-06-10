package com.kursat.pm_projectmarket.Model;

public class Comment {

    private String postTitle;
    private String comment;
    private Long score;
    private String token;


    public Comment(String postTitle, String comment,Long score,String token) {
        this.postTitle = postTitle;
        this.comment = comment;
        this.score = score;
        this.token = token;
    }

    public Long getScore() {
        return score;
    }

    public String getToken() {
        return token;
    }

    public String getPostTitle() { return postTitle; }

    public String getComment() { return comment;
    }
}
