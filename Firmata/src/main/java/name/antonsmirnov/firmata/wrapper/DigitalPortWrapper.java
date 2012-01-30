package name.antonsmirnov.firmata.wrapper;

import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.message.DigitalMessage;
import name.antonsmirnov.firmata.message.Message;
import name.antonsmirnov.firmata.serial.SerialException;

import java.util.HashMap;
import java.util.Map;

public class DigitalPortWrapper implements IFirmata {

    private IFirmata firmata;

    public DigitalPortWrapper(IFirmata firmata) {
        this.firmata = firmata;
    }

    public void addListener(Listener listener) {
        firmata.addListener(listener);
    }

    public void removeListener(Listener listener) {
        firmata.removeListener(listener);
    }

    public boolean containsListener(Listener listener) {
        return firmata.containsListener(listener);
    }

    public void clearListeners() {
        firmata.clearListeners();
    }

    private Map<Integer, Integer> portValues = new HashMap<Integer, Integer>();

    public Map<Integer, Integer> getPortValues() {
        return portValues;
    }

    public void clear() {
        portValues.clear();
    }

    public void send(Message message) throws SerialException {
        firmata.send(message);

        if (message instanceof DigitalMessage) {
            DigitalMessage digitalMessage = (DigitalMessage) message;
            portValues.put(digitalMessage.getPort(), digitalMessage.getValue());
        }
    }

    public void onDataReceived(int incomingByte) {
        firmata.onDataReceived(incomingByte);
    }
}
