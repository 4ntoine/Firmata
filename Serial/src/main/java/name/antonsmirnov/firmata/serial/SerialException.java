package name.antonsmirnov.firmata.serial;

/**
 * Serial exception
 */
public class SerialException extends RuntimeException {

    public SerialException(Exception e) {
        super(e);
    }
}
