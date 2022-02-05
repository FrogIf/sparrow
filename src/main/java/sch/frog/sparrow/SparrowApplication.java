package sch.frog.sparrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class SparrowApplication {

    public static void main(String[] args){
        SpringApplication application = new SpringApplication(SparrowApplication.class);
        application.addListeners(new ApplicationPidFileWriter("app.pid"));
        application.run(args);
    }

}
