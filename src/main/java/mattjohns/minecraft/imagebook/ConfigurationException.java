package mattjohns.minecraft.imagebook;

import mattjohns.common.BaseException;

public class ConfigurationException extends BaseException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}