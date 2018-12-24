package com.exception.exceptionNeedSendToClient;

public class ExceptionNeedSendToClient extends Exception {

    public ExceptionNeedSendToClient(Throwable t) {
        super(t);
    }

    public ExceptionNeedSendToClient() {
        super();
    }

    public ExceptionNeedSendToClient(String msg) {
        super(msg);
    }
}
