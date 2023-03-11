package com.infoiv.sender;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Sender {

    private static final String REQUEST_QUEUE = "request_queue";
    private static final String RESPONSE_QUEUE = "response_queue";
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

    public void sendRequest(String requestQueue, String message, String correlationId) {
        try {
            channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);
            channel.queueDeclare(RESPONSE_QUEUE, false, false, false, null);
            AMQP.BasicProperties amqpProps = new AMQP.BasicProperties();
            amqpProps = amqpProps.builder()
                    .correlationId(String.valueOf(correlationId))
                    .replyTo(RESPONSE_QUEUE).build();
            channel.basicPublish(DEFAULT_EXCHANGE, REQUEST_QUEUE, amqpProps,message.getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String waitForResponse(final String correlationId) {
        QueueingConsumer consumer = new QueueingConsumer(channel);
        String result = null;

        try {
            channel.basicConsume(RESPONSE_QUEUE, true, consumer);
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            if (delivery.getProperties() != null) {
                String msgCorrelationId = delivery.getProperties()
                        .getCorrelationId();
                if (!correlationId.equals(msgCorrelationId)) {
                    LOGGER.warn("Received response of another request.");
                } else {
                    result = message;
                }
            }
            LOGGER.info("Message received: " + message);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ShutdownSignalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ConsumerCancelledException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
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
