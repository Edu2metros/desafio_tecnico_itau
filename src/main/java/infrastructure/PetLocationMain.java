package infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"domain.service", "infrastructure.adapter", "domain.model"})
@EnableJpaRepositories(basePackages = "infrastructure.adapter.repository")
@EntityScan(basePackages = "domain.model")
public class PetLocationMain {
    public static void main(String[] args) {
        SpringApplication.run(PetLocationMain.class, args);
    }
}

// test