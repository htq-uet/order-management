package com.ghtk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class DemoController {

    @GetMapping("/shop/demo")
    public ResponseEntity<String> shopDemo() {
        System.out.println("Hello World!");
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/staff/demo")
    public ResponseEntity<String> staffDemo() {
        System.out.println("Hello World!");
        return ResponseEntity.ok("Hello World!");
    }
}
