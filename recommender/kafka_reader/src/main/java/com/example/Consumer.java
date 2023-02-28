
package com.example;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.NoArgsConstructor;

import org.apache.kafka.common.errors.InterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@NoArgsConstructor
public class Consumer {
    private final Logger log = LoggerFactory.getLogger(Consumer.class);

    String bootStrapServers = "127.0.0.1:9092";
    String groupId = "kakfa-stream-processing";
    String topic = "click_event";
    CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) { 
        new Consumer().run();
    }
    
    public Map<String, String> extractField(String consumerValue) { 
        Map<String, String> hashmap = new HashMap<>();

        JsonObject viewEvent = JsonParser
            .parseString(consumerValue)
            .getAsJsonObject();
            
        JsonObject properties = viewEvent.getAsJsonObject("properties");
        JsonObject contextObject = viewEvent.getAsJsonObject("context");

        hashmap.put("event", viewEvent.get("event").getAsString());
        hashmap.put("user_id", viewEvent.get("user_id").getAsString());
        hashmap.put("message_id", viewEvent.get("messageid").getAsString());
        hashmap.put("product_id", properties.get("productid").getAsString());
        hashmap.put("source", contextObject.get("source").getAsString());
        hashmap.put("click_timestamp", viewEvent.get("click_timestamp").getAsString());
        
        return hashmap;
    }

    public void sink(Map<String, String> field)   { 
        // Intialise database 
        Logger log = LoggerFactory.getLogger(Consumer.class);

        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres"; 
        String password = "password"; 

        String query = "INSERT INTO public.product_view_history(\n" +
        "\t event, message_id, user_id, product_id, source, click_timestamp)\n" +
        "\tVALUES (?, ?, ?, ?, ?, ?);";


        try ( 
            Connection dbConnetion = DriverManager.getConnection(url, user, password);
            PreparedStatement sqlDb = dbConnetion.prepareStatement(query);
        ) {

            sqlDb.setString(1, field.get("event"));
            sqlDb.setString(2, field.get("message_id"));
            sqlDb.setString(3, field.get("user_id"));
            sqlDb.setString(4, field.get("product_id"));
            sqlDb.setString(5, field.get("source"));
            sqlDb.setTimestamp(6, Timestamp.valueOf(field.get("click_timestamp")));
            sqlDb.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }           


       
    }

    private void run() { 

        log.info("Creating the consumer thread");
        ConsumerRunnable consumer = new ConsumerRunnable(latch, bootStrapServers, groupId, topic);
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> { 
            log.info("Caught shutdown hook");

            consumer.shutdown();

            try { 
                latch.await(); 
            } catch (InterruptException e) { 
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.info("Application has exited!");

        }));

    }
}
