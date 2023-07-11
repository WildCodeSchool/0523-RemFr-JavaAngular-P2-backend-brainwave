package com.templateproject.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class DemoController {

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public String userAccess() {
        return "User access";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public String adminAccess() {
        return "Admin access";
    }
}