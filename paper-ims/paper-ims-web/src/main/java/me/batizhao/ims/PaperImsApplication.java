package me.batizhao.ims;

import me.batizhao.common.security.annotation.EnablePaperResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePaperResourceServer
public class PaperImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperImsApplication.class, args);
    }

}
