package name.antonsmirnov.firmata;

import name.antonsmirnov.firmata.message.Message;

/**
 * Wait for incoming message from firmata
 */
public class FirmataWaiter {

    private int WAIT_INCREMENT = 10; // ms

    private Firmata firmata;

    public FirmataWaiter(Firmata firmata) {
        this.firmata = firmata;
    }

    private int waited;

    public void waitSeconds(int seconds, Class<? extends Message> message) throws WaitException {
        waited = 0;
        int wait = seconds * 1000; // sec -> ms

        while (true) {
            try {
                Thread.sleep(WAIT_INCREMENT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (firmata.getLastReceivedMessage() == null
                ||
                (
                    firmata.getLastReceivedMessage() != null
                    &&
                    message != null
                    &&
                    !firmata.getLastReceivedMessage().getClass().equals(message)
                )) {
                waited += WAIT_INCREMENT;
                if (waited > wait)
                    throw new WaitException(message);
            } else {
                break;
            }
        }
    }

    public void waitSeconds(int seconds) throws WaitException {
        waitSeconds(seconds, null);
    }
}
