package com.example.rhythme_backend.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Messages {

    private String username;
    private String content;
    private Date date;

    public Messages(String username, String content, Date date) {
        this.username = username;
        this.content = content;
        this.date = date;
    }

}