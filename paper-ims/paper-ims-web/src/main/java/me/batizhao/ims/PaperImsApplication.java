package me.batizhao.ims;

import me.batizhao.common.security.annotation.EnablePaperResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringBootApplication
@EnablePaperResourceServer
public class PaperImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperImsApplication.class, args);
    }

}
