package com.example.rhythme_backend.controller;


import org.joda.time.LocalTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class Controller {
    private final LocalTime now = new LocalTime();

    @GetMapping("/")
    public String Home() {
        return "빌드된 " + "현재 날짜는 : " + LocalDate.now() + " 현재 시간은 : " + now;
    }
}
