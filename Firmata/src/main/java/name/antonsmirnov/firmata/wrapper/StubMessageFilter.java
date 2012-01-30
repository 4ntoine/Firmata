package name.antonsmirnov.firmata.wrapper;

/**
 *  Filter stub : all messages are allowed
 */
public class StubMessageFilter implements IMessageFilter {

    public boolean isAllowed(MessageWithProperties data) {
        return true;
    }
}
