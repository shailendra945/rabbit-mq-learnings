package com.infoiv.subscriber;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimerTask;

public class HackthonReceiver extends TimerTask {

    private static final String HACKATON_QUEUE = "hackaton_queue";
    private final static Logger LOGGER = LoggerFactory.getLogger(HackthonReceiver.class);
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
            channel.queueDeclare(HACKATON_QUEUE, false, false, false, null);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(HACKATON_QUEUE, true,consumer);
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

    @Override
    public void run() {
        initialize();
        receive();
        destroy();
    }
}
