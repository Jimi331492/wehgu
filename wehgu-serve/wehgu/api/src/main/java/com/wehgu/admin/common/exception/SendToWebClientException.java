package com.wehgu.admin.common.exception;

public class SendToWebClientException extends RuntimeException {

    public SendToWebClientException(){
        super();
    }

    public SendToWebClientException(String message){
        super(message);
    }

    public SendToWebClientException(String message, Throwable cause){
        super(message,cause);
    }

    public SendToWebClientException(Throwable cause){
        super(cause);
    }

    protected SendToWebClientException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace){
        super(message,cause,enableSuppression,writableStackTrace);
    }
}