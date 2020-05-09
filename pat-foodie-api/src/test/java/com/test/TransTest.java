package com.test;

import com.pat.Application;
import com.pat.service.StuService;
import com.pat.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project Name: pat-foodie-dev
 * File Name: TransTest
 * Package Name: com.test
 * Author: elisha
 * Date: 2020/5/8 22:01
 * Copyright (c) 2020,All Rights Reserved.
 * Description：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Test
    public void myTest() {
//        stuService.testPropagationTrans();
        testTransService.testPropagationTrans();
    }

}