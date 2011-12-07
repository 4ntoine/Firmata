package name.antonsmirnov.firmata.message;

/**
 * Report protocol version message
 */
public class ReportProtocolVersionMessage extends Message {

    public ReportProtocolVersionMessage() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        return obj instanceof ReportProtocolVersionMessage;
    }

    @Override
    public String toString() {
        return "ReportFirmwareVersionMessage[]";
    }
}
