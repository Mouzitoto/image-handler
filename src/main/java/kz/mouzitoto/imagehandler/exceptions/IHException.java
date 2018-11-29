package kz.mouzitoto.imagehandler.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Родительский класс для специфичных ошибок ИР
 */
class IHException extends Exception {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHException.class);

    IHException(String message) {
        super(message);
        LOGGER.error(message, this);
    }
}
