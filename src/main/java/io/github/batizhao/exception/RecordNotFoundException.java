package io.github.batizhao.exception;

import org.springframework.lang.Nullable;

/**
 * @author batizhao
 * @since 2020-02-24
 */
public class RecordNotFoundException extends RuntimeException {

    /**
     * Create a new RecordNotFoundException with the specified message.
     *
     * @param msg the detail message
     */
    public RecordNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Create a new RecordNotFoundException with the specified message
     * and root cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public RecordNotFoundException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}
