package config;

import hr.eestec_zg.frmscore.config.CoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@Import(value = {CoreConfig.class, TestDataConfig.class})
public class TestAppConfig {
}
