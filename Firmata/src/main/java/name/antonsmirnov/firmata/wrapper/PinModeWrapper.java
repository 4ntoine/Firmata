package name.antonsmirnov.firmata.wrapper;

import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.message.SetPinModeMessage;
import name.antonsmirnov.firmata.serial.SerialException;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper which remembers pin modes
 */
public class PinModeWrapper implements IFirmata {

    /**
     * Set pin mode event listener
     */
    public static interface Listener {
        void onSetPinMode(int pin, int mode);
    }

    private IFirmata firmata;
    private Listener listener;

    public void addListener(IFirmata.Listener listener) {
        firmata.addListener(listener);
    }

    public void removeListener(IFirmata.Listener listener) {
        firmata.removeListener(listener);
    }

    public boolean containsListener(IFirmata.Listener listener) {
        return firmata.containsListener(listener);
    }

    public void clearListeners() {
        firmata.clearListeners();
    }

    // pins configuration
    private Map<Integer, Integer> pinsConfig = new HashMap<Integer, Integer>();

    /**
     * Get remembered pin modes
     * @return pin modes
     */
    public Map<Integer, Integer> getPinsConfig() {
        return pinsConfig;
    }

    public PinModeWrapper(IFirmata firmata) {
        this(firmata, null);
    }

    /**
     * Constructor
     * @param firmata wrapped firmata
     * @param listener set pin mode event listener
     */
    public PinModeWrapper(IFirmata firmata, Listener listener) {
        this.firmata = firmata;
        this.listener = listener;
        
        clear();
    }

    /**
     * Clear pins config
     * (should be invoked on serial.stop())
     */
    public void clear() {
        pinsConfig.clear();
    }

    public void send(Message message) throws SerialException {
        firmata.send(message);

        if (message instanceof SetPinModeMessage) {
            SetPinModeMessage setPinModeMessage = (SetPinModeMessage) message;

            // remember
            pinsConfig.put(setPinModeMessage.getPin(), setPinModeMessage.getMode());

            // fire event
            if (listener != null)
                listener.onSetPinMode(setPinModeMessage.getPin(), setPinModeMessage.getMode());
        }
    }

    public void onDataReceived(int incomingByte) {
        firmata.onDataReceived(incomingByte);
    }
}
