package io.github.batizhao.uaa;

import io.github.batizhao.common.security.annotation.EnablePaperFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePaperFeignClients
public class PaperUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperUaaApplication.class, args);
    }

}
