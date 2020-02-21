package io.github.batizhao.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author batizhao
 * @since 2020-02-21
 */
@RestController
@RequestMapping("exception")
public class TestExceptionController {

    @GetMapping("500")
    public void handleRequest() {
        throw new RuntimeException("RuntimeException 运行时异常");
    }

}
