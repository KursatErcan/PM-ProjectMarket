package com.kursat.pm_projectmarket.Model;

public class Comment {

    private String title;
    private String comment;


    public Comment(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    public String getTitle() { return title;
    }

    public String getComment() { return comment;
    }
}
