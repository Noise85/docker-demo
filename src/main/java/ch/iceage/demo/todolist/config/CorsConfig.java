package ch.iceage.demo.todolist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for development profile.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    private final String allowedOrigins;

    public CorsConfig(@Value("${cors.allowed.origins}") String allowedOrigins) {
        Assert.notNull(allowedOrigins, "cors.allowed.origins must not be null");
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("CORS configuration enabled: allowing " + this.allowedOrigins);
        registry.addMapping("/**")
                .allowedOrigins(this.allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
