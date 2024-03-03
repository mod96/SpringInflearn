package com.example.demo.web;

import com.example.demo.common.MyLogger;
import com.example.demo.common.MyLogger2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService2 {
    private final MyLogger2 myLogger;
    public void logic(String testId) {
        myLogger.log("serviceId = " + testId);
    }
}
