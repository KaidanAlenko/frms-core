package config;

import hr.eestec_zg.frmscore.config.AppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@Import(value = {AppConfig.class, TestDataConfig.class})
public class TestAppConfig {
}
