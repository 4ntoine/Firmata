package name.antonsmirnov.firmata.message.factory;

/**
 * Message parameters are invalid
 * (most likely to hardware features)
 */
public class MessageValidationException extends Exception {

    public MessageValidationException(String message) {
        super(message);
    }
}
