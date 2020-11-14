package pl.lasota.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.RepositoryDefinition;

@SpringBootApplication
@EntityScan(basePackages = {"pl.lasota.crm.entity"})
@EnableJpaRepositories("pl.lasota.crm")
public class StartCrm {
    public static void main(String[] args) {
        SpringApplication.run(StartCrm.class, args);
    }
}
