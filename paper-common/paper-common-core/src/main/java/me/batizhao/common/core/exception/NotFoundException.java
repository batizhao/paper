package me.batizhao.common.core.exception;

/**
 * @author batizhao
 * @since 2020-03-20
 **/
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
