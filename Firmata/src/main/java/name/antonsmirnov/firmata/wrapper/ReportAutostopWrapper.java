package name.antonsmirnov.firmata.wrapper;

import name.antonsmirnov.firmata.IFirmata;
import name.antonsmirnov.firmata.message.*;
import name.antonsmirnov.firmata.serial.SerialException;

import java.util.HashSet;
import java.util.Set;

/**
 * Disables pin state reporting after first state message received
 */
public class ReportAutostopWrapper extends IFirmata.StubListener implements IFirmata {
    
    private IFirmata firmata;

    public ReportAutostopWrapper(IFirmata firmata) {
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

    // active reporting pins
    private Set<Integer> digitalReporting = new HashSet<Integer>(); // integer - port
    private Set<Integer> analogReporting = new HashSet<Integer>();  // integer - pin

    public void send(Message message) throws SerialException {
        firmata.send(message);

        // digital
        if (message instanceof ReportDigitalPortMessage) {
            ReportDigitalPortMessage digitalMessage = (ReportDigitalPortMessage) message;
            if (digitalMessage.isEnable())
                digitalReporting.add(digitalMessage.getPort());
        }

        // analog
        if (message instanceof ReportAnalogPinMessage) {
            ReportAnalogPinMessage analogMessage = (ReportAnalogPinMessage) message;
            if (analogMessage.isEnable())
                analogReporting.add(analogMessage.getPin());
        }
    }

    @Override
    public void onAnalogMessageReceived(AnalogMessage message) {
        if (analogReporting.contains(message.getPin())) {
            analogReporting.remove(message.getPin());

            disableAnalogReporting(message);
        }
    }

    private void disableAnalogReporting(AnalogMessage message) {
        try {
            firmata.send(new ReportAnalogPinMessage(message.getPin(), false));
        } catch (SerialException e) {
        // TODO: fix (bad)
        }
    }

    @Override
    public void onDigitalMessageReceived(DigitalMessage message) {
        if (digitalReporting.contains(message.getPort())) {
            digitalReporting.remove(message.getPort());

            disableDigitalReporting(message);
        }
    }

    private void disableDigitalReporting(DigitalMessage message) {
        try {
            firmata.send(new ReportDigitalPortMessage(message.getPort(), false));
        } catch (SerialException e) {
            // TODO: fix (bad)
        }
    }

    public void clear() {
        digitalReporting.clear();
        analogReporting.clear();
    }

    public void onDataReceived(int incomingByte) {
        firmata.onDataReceived(incomingByte);
    }


}
