package com.example.jueldemo;

import org.springframework.stereotype.Component;

@Component
public class BeanDemo {

    private String blog = "https://spring.hhui.top";

    private Integer num = 8;

    public String hello(String name) {
        return "hello " + name + ", welcome to my blog  " + blog + ", now person: " + num;
    }
}