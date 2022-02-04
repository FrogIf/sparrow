package sch.frog.sparrow;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SparrowApplication {

    public static void main(String[] args){
        SpringApplication application = new SpringApplication(SparrowApplication.class);
        application.addListeners(new ApplicationPidFileWriter("app.pid"));
        application.run(args);
    }

    @Bean(value = "meterRegistryCustomizer")
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(@Value("${spring.application.name}") String applicationName) {
        return meterRegistry -> meterRegistry.config()
                .commonTags("application", applicationName);
    }

}
