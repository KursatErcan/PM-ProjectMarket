package com.kursat.pm_projectmarket.Model;

public class Comment {

    private String title;
    private String comment;
    String point;
    String token;


    public Comment(String title, String comment,String point,String token) {
        this.title = title;
        this.comment = comment;
        this.point = point;
        this.token = token;
    }

    public String getPoint() {
        return point;
    }

    public String getToken() {
        return token;
    }

    public String getTitle() { return title;
    }

    public String getComment() { return comment;
    }
}
