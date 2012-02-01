package name.antonsmirnov.firmata.wrapper;

/**
 * Direction for the message: incoming/outcoming
 */
public class DirectionMessagePropertyManager extends MessagePropertyManager<Boolean> {

    private static final String KEY = "isIncoming";

    private boolean isIncoming;

    public boolean isIncoming() {
        return isIncoming;
    }

    public DirectionMessagePropertyManager(boolean isIncoming) {
        super(KEY);
        this.isIncoming = isIncoming;
    }

    @Override
    protected Boolean createProperty() {
        return isIncoming;
    }
}
