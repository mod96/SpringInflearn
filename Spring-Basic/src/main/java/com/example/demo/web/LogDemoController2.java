package com.example.demo.web;

import com.example.demo.common.MyLogger;
import com.example.demo.common.MyLogger2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController2 {
    private final LogDemoService2 logDemoService;
    private final MyLogger2 myLogger2;

    @RequestMapping("log-demo-2")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        System.out.println("myLogger = " + myLogger2.getClass());
        myLogger2.setRequestURL(requestURL);
        myLogger2.log("controller test");
        logDemoService.logic("testId");
        return "";
    }
}
