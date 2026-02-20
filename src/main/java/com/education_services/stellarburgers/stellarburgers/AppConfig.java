package com.education_services.stellarburgers.stellarburgers;
import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:config.properties"})
public interface AppConfig extends Config {
    String baseUrl();
    String browser();
}