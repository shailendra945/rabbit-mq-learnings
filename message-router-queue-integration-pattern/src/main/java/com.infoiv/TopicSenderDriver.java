package com.infoiv;

import com.infoiv.sender.Sender;

public class TopicSenderDriver {

    private static final String TOPIC_EXCHANGE =                     "topic_exchange";

    public static void sendToTopicExchange() {
        Sender sender = new Sender();
        sender.initialize();
        sender.sendEvent(TOPIC_EXCHANGE, "Test message 1.","seminar.java");
        sender.sendEvent(TOPIC_EXCHANGE, "Test message 2.","seminar.rabbitmq");
        sender.sendEvent(TOPIC_EXCHANGE, "Test message 3.","hackaton.rabbitmq");
        sender.destroy();
    }

    public static void main(String[] args) {
        sendToTopicExchange();
    }
}
