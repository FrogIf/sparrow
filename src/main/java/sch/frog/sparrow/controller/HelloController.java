package sch.frog.sparrow.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Controller
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/log")
    public String outputLog(){
        logger.info("output something, random number : {}", new Random().nextInt(100));
        return "log";
    }

    @RequestMapping("/exceptionLog")
    public String exceptionLog(){
        Exception e = new Exception("exception for log");
        logger.error(e.getMessage(), e);
        return "exception log";
    }

}
