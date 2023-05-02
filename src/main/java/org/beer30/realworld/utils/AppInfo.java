package org.beer30.realworld.utils;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppInfo {

    public String getAppVersion() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("app-build.properties"));
            return properties.getProperty("app.verion", "Unknown Version");
        } catch (IOException ex) {
            log.error("Error Getting App Version", ex);  
            return "Error getting Version";    
        }
    }
    
}
