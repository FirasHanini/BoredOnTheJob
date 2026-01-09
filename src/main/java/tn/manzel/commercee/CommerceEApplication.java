package tn.manzel.commercee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommerceEApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceEApplication.class, args);
    }

}
