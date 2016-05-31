package mattjohns.common;

public class BaseException extends Exception {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable throwable) {
        super(message, throwable);
    }

}