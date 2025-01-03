package com.example.FinderUnited.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/success")
public class DeploymentSuccessController {

    @GetMapping("/check")
    public String checkDeployment() {
        return "Deployment is successfull";
    }
}
