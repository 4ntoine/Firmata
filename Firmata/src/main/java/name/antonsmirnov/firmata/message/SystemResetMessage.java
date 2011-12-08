package name.antonsmirnov.firmata.message;

/**
 * System reset message
 */
public class SystemResetMessage extends Message {

    public SystemResetMessage() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        return obj instanceof SystemResetMessage;
    }

    @Override
    public String toString() {
        return "SystemResetMessage[]";
    }
}
