package name.antonsmirnov.firmata.wrapper;

/**
 * Filters messages
  */
public interface IMessageFilter {

    /**
     * Return true if message is allowed and should not be filtered
     */
    boolean isAllowed(MessageWithProperties data);
}
