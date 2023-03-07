package com.infoiv.sender;

public class FanoutSenderDemo {

    private static  final String EXCHANGE_TYPE_FANOUT ="fanout";
    public static void main(String[] args) {

        sendToFanoutExchange("pubsub_exchange");
    }

    public static void sendToFanoutExchange(String exchange) {
        Sender sender = new Sender();
        sender.initialize();
        sender.send(exchange,EXCHANGE_TYPE_FANOUT,"Test message.");
        sender.destroy();
    }
}
