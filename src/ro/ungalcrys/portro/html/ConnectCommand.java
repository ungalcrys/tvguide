package ro.ungalcrys.portro.html;

import java.io.IOException;

import ro.ungalcrys.portro.Util;

public abstract class ConnectCommand<T> {
    private static final int MAX_CONNECT_TRIES_COUNT = 4;

    /**
     * Tries to do a connect action multiple times, if can't do it, at the end will show a
     * exception.
     * 
     * @return a connection input stream or document or something else related to a connection.
     */
    public T getResult() {
        // IOException exception = null;
        T result = null;
        for (int i = 0; i < MAX_CONNECT_TRIES_COUNT; i++) {
            try {
                result = doConnectAction();
                break;
            } catch (IOException e) {
                Util.printException(e);
                // Util.printDebug("Failed doConnectAction() index " + i);
                // exception = e;
                Util.sleep(Util.ONE_DECISEC * (int) Math.pow(2, i));
            }
        }
        // if (result == null)
        // Util.printException(exception);
        return result;
    }

    protected abstract T doConnectAction() throws IOException;
}
