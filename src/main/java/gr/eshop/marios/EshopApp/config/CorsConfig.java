package gr.eshop.marios.EshopApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:8080") // Επέτρεψε μόνο το Angular origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Επέτρεψε το preflight
                .allowedHeaders("*") // Επέτρεψε όλα τα headers
                .allowCredentials(true); // Αν χρειάζονται cookies ή tokens
    }
}
