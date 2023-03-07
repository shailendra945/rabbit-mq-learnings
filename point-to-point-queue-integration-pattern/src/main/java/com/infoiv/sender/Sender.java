package com.infoiv.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Sender {

    private final static String QUEUE_NAME = "event_queue";
    private final static Logger LOGGER =
            LoggerFactory.getLogger(Sender.class);
    private static final String DEFAULT_EXCHANGE = "";
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

    public void send(String message) {
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_NAME, null,
                    message.getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void send(String exchange, String type, String message) {
        try {
            channel.exchangeDeclare(exchange, type);
            channel.basicPublish(exchange, "", null,
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
