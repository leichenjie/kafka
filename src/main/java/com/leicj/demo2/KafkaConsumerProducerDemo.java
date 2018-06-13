package com.leicj.demo2;

public class KafkaConsumerProducerDemo {
    public static void main(String[] args) {
        KafkaProducer producerThread = new KafkaProducer(KafkaProperties.topic2);
        producerThread.start();
        KafkaConsumer consumerThread = new KafkaConsumer(KafkaProperties.topic2);
        consumerThread.start();
    }
}
