package ch.iceage.demo.todolist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for development profile.
 * Allows requests from localhost:3000.
 */
@Configuration
@Profile("dev")
public class DevCorsConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DevCorsConfig.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Dev CORS configuration enabled: allowing http://localhost:3000");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
