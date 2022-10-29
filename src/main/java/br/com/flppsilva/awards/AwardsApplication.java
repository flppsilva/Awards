package br.com.flppsilva.awards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AwardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwardsApplication.class, args);
    }

}
