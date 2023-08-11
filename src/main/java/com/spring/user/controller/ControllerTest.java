package com.spring.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {

    @RequestMapping("/test")
    public String Test(){

        return "connection test";
    }
}
