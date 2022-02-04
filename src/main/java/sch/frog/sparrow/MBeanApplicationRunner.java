package sch.frog.sparrow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sch.frog.sparrow.jmx.Hello;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Component
public class MBeanApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MBeanApplicationRunner.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("sparrow:name=Hello");
        Hello hello = new Hello(applicationName);
        platformMBeanServer.registerMBean(hello, objectName);

        logger.info("mbean {} init finish.", objectName);
    }

}
