package name.antonsmirnov.firmata.wrapper;

/**
 * Abstract message property
 */
public abstract class MessagePropertyManager<ConcretePropertyClass>
        implements IMessagePropertyManager<ConcretePropertyClass> {

    private String key;

    public MessagePropertyManager(String key) {
        this.key = key;
    }

    public ConcretePropertyClass get(MessageWithProperties data) {
        return (ConcretePropertyClass)data.getProperty(key);
    }

    public void set(MessageWithProperties data) {
        ConcretePropertyClass property = createProperty();
        data.setProperty(key, property);
    }

    /**
     * set concrete property
     */
    protected abstract ConcretePropertyClass createProperty();
}
