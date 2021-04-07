package io.github.batizhao.aspect;


import java.lang.annotation.*;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SystemLog {

    String value() default "";

}
