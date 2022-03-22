package com.splb.demo.controller;

import com.splb.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Autowired
    Service service;
    @RequestMapping("/hello")
    public String hello(){
        service.startService();
        return "index4.html";
    }
}
