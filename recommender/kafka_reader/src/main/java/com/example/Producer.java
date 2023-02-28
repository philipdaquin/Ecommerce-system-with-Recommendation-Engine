package com.example;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Producer {
    private final Logger log = LoggerFactory.getLogger(Producer.class);
    String bootStrap = "127.0.0.1:9092";

    public static void main(String[] args) throws IOException { 
        new Producer().run();
    }    

    private void run() throws IOException {
        
        // Iniitialise a kafka producer 
        KafkaProducer<String,  String> producer = createProducer();

        try (Stream<String> streams = Files.lines(Paths.get("/src/main/resources/product_views.json"))) { 
            streams.forEachOrdered(events -> { 


                Timestamp time = new Timestamp(System.currentTimeMillis());

                wait(1000);

                JsonObject json = JsonParser.parseString(events).getAsJsonObject();
                
                json.addProperty("click_timestamp", time.toString());

                String gsonObject = new Gson().toJson(json);

                ProducerRecord<String, String> record = new ProducerRecord<String,String>("click_event", null, gsonObject);


                producer.send(record, new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        log.info(
                            "Topic:" + record.topic() + "\n" + 
                            "Partition:" + record.partition() + "\n" + 
                            "Value:" + record.value() + "\n" + 
                            "Timestamp:" + record.timestamp() + "\n" 
                        );
                    }
                     
                });

                
            });
        }


    }

    private KafkaProducer<String, String> createProducer() { 
        Properties props = new Properties();

        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrap);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        props.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));

        return new KafkaProducer<String,String>(props);

    }
    private static void wait(int seconds) {
        try { 
            Thread.sleep(seconds);
        } catch(InterruptedException e) { 
            Thread.currentThread().interrupt();
        }
    }

}
