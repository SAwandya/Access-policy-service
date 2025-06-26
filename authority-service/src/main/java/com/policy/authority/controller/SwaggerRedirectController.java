package com.policy.authority.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SwaggerRedirectController {
    
    @GetMapping("/swagger")
    public RedirectView swagger() {
        return new RedirectView("/swagger-ui/index.html");
    }
    
    @GetMapping("/api")
    public RedirectView api() {
        return new RedirectView("/swagger-ui/index.html");
    }
}
