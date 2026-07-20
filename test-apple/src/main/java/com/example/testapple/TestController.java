package com.example.testapple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    TestController.class);

    @GetMapping("/test")
    public String test() {

        LOGGER.info(
                "Test endpoint called");

        return "Hello";
    }
}