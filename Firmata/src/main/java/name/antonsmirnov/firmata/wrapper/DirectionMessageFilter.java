package name.antonsmirnov.firmata.wrapper;

/**
 * Filter to filter only messages which have according direction property
 */
public class DirectionMessageFilter implements IMessageFilter {

    private DirectionMessagePropertyManager propertyManager;

    public DirectionMessageFilter(DirectionMessagePropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public boolean isAllowed(MessageWithProperties data) {
        return (propertyManager.get(data).equals(propertyManager.isIncoming()));
    }
}
