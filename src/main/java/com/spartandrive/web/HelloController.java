package com.spartandrive.web;

/**
 * Created by mrugen on 11/20/15.
 */
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spartan  Drive!";
    }

}
