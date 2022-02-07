package sch.frog.sparrow.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sch.frog.sparrow.bean.OOMBean;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping("/oom")
@RestController
public class OOMController {

    private static final Logger logger = LoggerFactory.getLogger(OOMController.class);

    @RequestMapping("/triggerOOM")
    public String triggerOOM(){
        ArrayList<Object> objs = new ArrayList<>();
        while(true){
            objs.add(new OOMBean());
        }
    }

    private final AtomicLong total = new AtomicLong(0);

    @RequestMapping("/accumulationOOM")
    public ArrayList<OOMBean> accumulationOOM(){
        long t = total.getAndAdd(10000);
        logger.info("accumulation total : {}", t);
        ArrayList<OOMBean> beans = new ArrayList<>();
        for(int i = 0 ; i < t; i++){
            beans.add(new OOMBean());
        }
        return beans;
    }
}
