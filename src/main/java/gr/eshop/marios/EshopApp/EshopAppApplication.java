package gr.eshop.marios.EshopApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EshopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopAppApplication.class, args);
    }

}
