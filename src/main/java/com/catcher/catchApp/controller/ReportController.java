package com.catcher.catchApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReportController {
    @GetMapping("/api/report")
    @ResponseBody
    public String report() {
        return "신고";
    }
}
