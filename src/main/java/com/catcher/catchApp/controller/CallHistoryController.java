package com.catcher.catchApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CallHistoryController {
    @GetMapping("/api/call-history")
    @ResponseBody
    public String callHistory() {
        return "통화 내역";
    }
}
