package mattjohns.common.storage;

import mattjohns.common.BaseException;

public class StorageException extends BaseException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable throwable) {
        super(message, throwable);
    }
}