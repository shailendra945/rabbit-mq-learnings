package com.infoiv.sender;

import com.infoiv.sender.Sender;

public class DefaultExchangeSenderDemo {
    public static void main(String [] args) {
        sendToDefaultExchange();
    }
    public static void sendToDefaultExchange() {
        Sender sender = new Sender();
        sender.initialize();
        sender.send("Test message.");
        sender.destroy();
    }
}