package info.rolandkrueger.userservice.api.exceptions;

import java.text.MessageFormat;

/**
 * @author Roland Krüger
 */
public class UnexpectedAPIFormatException extends RuntimeException {
    public UnexpectedAPIFormatException(String message) {
        super(message);
    }

    public UnexpectedAPIFormatException(String messagePattern, Object ... arguments) {
        super(MessageFormat.format(messagePattern, arguments));
    }
}
