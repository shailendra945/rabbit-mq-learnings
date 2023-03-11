package com.infoiv;


import com.infoiv.receiver.RequestReceiver;

public class RequestReplyReceiverDriver {
    public static void main(String[] args) throws InterruptedException {

        final RequestReceiver receiver = new RequestReceiver();
        receiver.initialize();
        receiver.receive();
        receiver.destroy();
    }
}
