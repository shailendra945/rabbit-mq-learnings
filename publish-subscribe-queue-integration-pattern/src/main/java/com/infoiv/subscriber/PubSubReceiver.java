package com.infoiv.subscriber;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PubSubReceiver {

    private static final Logger logger = LoggerFactory.getLogger(PubSubReceiver.class);
    private Channel channel;
    private Connection connection;
    private final static String EXCHANGE_NAME = "pubsub_exchange";


    public void initialize(){
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("shailendra");
            factory.setPassword("password");
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String receive(String queue) {
        if (channel == null) {
            initialize();
        }
        String message = null;
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueDeclare(queue, false, false, false, null);
            channel.queueBind(queue, EXCHANGE_NAME, "");
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queue, true, consumer);
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            message = new String(delivery.getBody());
            logger.info("Message Received : " + message);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ShutdownSignalException e) {
            logger.error(e.getMessage(), e);
        } catch (ConsumerCancelledException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }



    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
