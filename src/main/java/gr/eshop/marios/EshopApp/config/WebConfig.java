package gr.eshop.marios.EshopApp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the `uploads` folder
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // Relative to project root


    }
}
