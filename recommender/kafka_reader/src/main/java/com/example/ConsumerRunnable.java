package com.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class ConsumerRunnable implements Runnable  { 
    private Consumer runner;

    private CountDownLatch latch;
    private final Logger log = LoggerFactory.getLogger(Consumer.class);

    public KafkaConsumer<String, String> consumer;


    public ConsumerRunnable(
        CountDownLatch latch, 
        String bootstrapServers, 
        String groupId, 
        String topic
    ) {
        this.latch = latch;

        Properties prop = new Properties();

        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        consumer = new KafkaConsumer<String, String>(prop);
        
        consumer.subscribe(Arrays.asList(topic));
    }   
    /*
     * 
     */
    @Override
    public void run() {
        try { 
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    // Extraft the values into hashmaps 
                    Map<String, String> field = runner.extractField(record.value());

                    // Add id field for idempotent 
                    runner.sink(field);
                }
            }
        } catch (WakeupException e) { 
            log.info("Received shutdown signal!");
        } finally { 
            consumer.close();

            latch.countDown();
        }
    }
    public void shutdown() { 
        consumer.wakeup();
    }
}