package io.github.batizhao.exception;

/**
 * @author batizhao
 * @date 2020/9/23
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
