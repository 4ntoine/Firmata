package name.antonsmirnov.firmata.wrapper;

import java.util.Map;

/**
 * Property for the message
 */
public interface IMessagePropertyManager<ConcretePropertyClass> {
    ConcretePropertyClass get(MessageWithProperties data);
    void set(MessageWithProperties data);
}
