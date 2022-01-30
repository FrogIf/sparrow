package sch.frog.sparrow.config;

import org.springframework.core.env.PropertySource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ExternalFilePropertySource extends PropertySource<Properties> {
    private boolean canLoad = false;

    public ExternalFilePropertySource() {
        super("sparrow-external", new Properties());
        try{
            String externalConfigPath = System.getProperty("external-config", "external.properties");
            File file = new File(externalConfigPath);
            if(file.exists()){
                try(
                        FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                ){
                    super.source.load(reader);
                    canLoad = true;
                }
            }else{
                String absolutePath = file.getAbsolutePath();
                logger.warn("config file is not find " + absolutePath);
            }
        }catch(IOException e){
            logger.error("external config load failed : {}", e);
        }
    }

    @Override
    public Object getProperty(String name) {
        if(!canLoad){
            return null;
        }
        return super.source.getProperty(name);
    }
}
