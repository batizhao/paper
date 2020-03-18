package io.github.batizhao.ims;

import io.github.batizhao.common.security.annotation.EnablePaperResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePaperResourceServer
public class PaperImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperImsApplication.class, args);
    }

}
