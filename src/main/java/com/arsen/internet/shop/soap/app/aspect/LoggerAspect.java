package com.arsen.internet.shop.soap.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    private static final String CONTROLLER_START = "[%s] %s:%s has started...";
    private static final String CONTROLLER_FINISHED = "[%s] %s:%s has finished...";

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getBeforeRequests(JoinPoint point) {
        print(point, "GET", CONTROLLER_START);
    }

    @After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getAfterRequests(JoinPoint point) {
        print(point, "GET", CONTROLLER_FINISHED);
    }


    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postBeforeRequests(JoinPoint point) {
        print(point, "POST", CONTROLLER_START);
    }

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postAfterRequests(JoinPoint point) {
        print(point, "POST", CONTROLLER_FINISHED);
    }

    private void print(JoinPoint point, String request, String message){
        String controllerName = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        log.info(String.format(message, request, controllerName, methodName));
    }
}
