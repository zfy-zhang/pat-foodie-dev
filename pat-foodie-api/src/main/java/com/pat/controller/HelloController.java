package com.pat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Name: pat-foodie-dev
 * File Name: HelloController
 * Package Name: com.pat.controller
 * Author: elisha
 * Date: 2020/5/3 23:18
 * Copyright (c) 2020,All Rights Reserved.
 * Description：
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
