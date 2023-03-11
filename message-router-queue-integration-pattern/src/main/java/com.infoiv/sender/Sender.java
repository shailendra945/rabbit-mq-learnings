package com.infoiv.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Sender {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(Sender.class);
    private static final String SEMINAR_QUEUE = "seminar_queue";
    private static final String HACKATON_QUEUE = "hackaton_queue";
    private static final String TOPIC_EXCHANGE = "topic_exchange";
    private Channel channel;
    private Connection connection;

    public void initialize() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("shailendra");
            factory.setPassword("password");
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    public void sendEvent(String exchange, String message, String messageKey) {
        try {
            channel.exchangeDeclare(TOPIC_EXCHANGE, "topic");
            channel.queueDeclare(SEMINAR_QUEUE, false, false,                   false, null);
            channel.queueDeclare(HACKATON_QUEUE, false, false,                 false, null);
            channel.queueBind(SEMINAR_QUEUE, TOPIC_EXCHANGE,"seminar.#");
            channel.queueBind(HACKATON_QUEUE, TOPIC_EXCHANGE,"hackaton.#");
            channel.basicPublish(TOPIC_EXCHANGE, messageKey, null,
                    message.getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

}
