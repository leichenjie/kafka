package com.leicj.demo1;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerMsgTask implements Runnable {
    private KafkaStream m_stream;
    private int m_threadNumber;

    public ConsumerMsgTask(KafkaStream m_stream, int m_threadNumber) {
        this.m_stream = m_stream;
        this.m_threadNumber = m_threadNumber;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[],byte[]> it = m_stream.iterator();
        while (it.hasNext()) {
            System.out.println("Thread " + m_threadNumber + ":" + new String(it.next().message()));
        }
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
