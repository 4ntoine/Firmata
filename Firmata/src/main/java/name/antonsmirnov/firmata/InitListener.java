package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.message.FirmwareVersionMessage;
import name.antonsmirnov.firmata.message.ProtocolVersionMessage;

/**
 * Init Listener
 */
public class InitListener extends IFirmata.StubListener {

    /**
     * Init wrapper listener
     */
    public static interface Listener {
        void onInitialized();
    }

    private Listener listener;

    private FirmwareVersionMessage firmware;

    public FirmwareVersionMessage getFirmware() {
        return firmware;
    }

    private ProtocolVersionMessage protocol;

    public ProtocolVersionMessage getProtocol() {
        return protocol;
    }

    public InitListener(Listener listener) {
        this.listener = listener;
        clear();
    }

    public void clear() {
        firmware = null;
        protocol = null;
    }

    private void checkInitAndFire() {
        if (firmware != null && protocol != null)
            listener.onInitialized();
    }

    public boolean isInitialized() {
        return firmware != null && protocol != null;
    }

    public void onFirmwareVersionMessageReceived(FirmwareVersionMessage message) {
        this.firmware = message;
        checkInitAndFire();
    }

    public void onProtocolVersionMessageReceived(ProtocolVersionMessage message) {
        this.protocol = message;
        checkInitAndFire();
    }
}
