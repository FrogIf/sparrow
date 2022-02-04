package sch.frog.sparrow.prometheus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ManagedResource(objectName = "frog:name=FrogTest", description = "frog test")
public class FrogMonitor {

    @Value("${spring.application.name}")
    private String applicationName;

    private final AtomicInteger access = new AtomicInteger(0);

    @ManagedAttribute
    public String getName(){
        return "Frog";
    }

    @ManagedAttribute
    public int getAccess(){
        return access.get();
    }

    public void accessIncrement(){
        access.incrementAndGet();
    }

    @ManagedAttribute
    public MonitorInfo getMonitorInfo(){
        MonitorInfo res = new MonitorInfo();
        res.setApplication(applicationName);
        res.setCount(new Random().nextInt(100));
        return res;
    }

}
