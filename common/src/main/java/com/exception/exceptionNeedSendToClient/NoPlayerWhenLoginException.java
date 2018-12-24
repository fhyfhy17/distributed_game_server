package com.exception.exceptionNeedSendToClient;


public class NoPlayerWhenLoginException extends ExceptionNeedSendToClient {

    private static final long serialVersionUID = -412341456L;


    private String msg;

    public NoPlayerWhenLoginException() {
        super();
    }

    public NoPlayerWhenLoginException(Throwable t) {
        super(t);
    }

    public NoPlayerWhenLoginException(String msg) {
        super(msg);

        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
