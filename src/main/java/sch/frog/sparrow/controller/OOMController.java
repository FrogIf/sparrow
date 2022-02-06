package sch.frog.sparrow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sch.frog.sparrow.bean.OOMBean;

import java.util.ArrayList;

@RequestMapping("/oom")
@Controller
public class OOMController {

    @RequestMapping("/triggerOOM")
    public String triggerOOM(){
        ArrayList<Object> objs = new ArrayList<>();
        while(true){
            objs.add(new OOMBean());
        }
    }
}
