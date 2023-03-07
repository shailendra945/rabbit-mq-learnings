package com.infoiv.subscriber;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CompetingReceiver {

    private final static String QUEUE_NAME = "event_queue";
    private final static Logger LOGGER = LoggerFactory.getLogger(CompetingReceiver.class);
    private Connection connection = null;
    private Channel channel = null;
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

    public String receive() {
        if (channel == null) {
            initialize();
        }
        String message = null;
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true,consumer);
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            message = new String(delivery.getBody());
            LOGGER.info("Message received: " + message);
            return message;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ShutdownSignalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ConsumerCancelledException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return message;
    }

    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }
}
