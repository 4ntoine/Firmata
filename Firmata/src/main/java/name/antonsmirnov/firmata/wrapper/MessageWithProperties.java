package name.antonsmirnov.firmata.wrapper;

import name.antonsmirnov.firmata.message.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message with associated properties
 */
public class MessageWithProperties implements Serializable {

    public MessageWithProperties(Message message) {
        setMessage(message);
    }

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    private Map<String, Object> properties = new HashMap<String, Object>();

    public Object getProperty(String key) {
        return properties.get(key);
    }
    
    public void setProperty(String key, Object property) {
        properties.put(key, property);
    }

    @Override
    public String toString() {
        return message.toString() + " -> " + properties.toString();
    }
}
