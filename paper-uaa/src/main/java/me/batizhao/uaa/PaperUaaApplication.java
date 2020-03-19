package me.batizhao.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "me.batizhao.ims.api.feign")
public class PaperUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperUaaApplication.class, args);
    }

}
