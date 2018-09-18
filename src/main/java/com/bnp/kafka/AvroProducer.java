package com.bnp.kafka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import org.apache.kafka.clients.producer.ProducerConfig;


public class AvroProducer
{
    public static final String key = "demo";
    public static final String userSchema = "{\"type\":\"record\"," +
            "\"name\":\"myrecord\"," +
            "\"fields\":[{\"name\":\"name\",\"type\":\"string\"},"
            + "{\"name\":\"ID\",\"type\":\"string\"}," +
            "{\"name\":\"gender\",\"type\":\"string\"}]}";
    public List<String> readFile() throws IOException{
        String file = "";
        List<String> content = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = "";
            while((line=br.readLine()) !=null ){
                content.add(line);
            }
        }catch(FileNotFoundException e) {

        }
        return content;
    }

    public static <V> void main(String[] args ){
        Properties props = new Properties();
        //kafka servers
        // props.put("bootstrap.servers", "s00vl9976336.fr.net.intra:9092");

        props.put("bootstrap.servers", "s00vl9982859:9092,s00vl9982860:9092,s00vl9982666:9092,s00vl9982874:9092,s00vl9982875:9092,s00vl9982876:9092");

        //configure the following three settings for SSL Encryption
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "C:/work/big_data/kafkafactory.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "Kafka2018");

        // configure the following three settings for SSL Authentication
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "C:/work/big_data/kafkafactory.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "Kafka2018");
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "Kafka2018");



        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", "http://s00vl9982862.fr.net.intra:8081");


        Producer<String,GenericRecord> producer1 = new KafkaProducer<>(props);
        String topic = "flinkAvro02";



     //   System.out.println("start");

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(userSchema);

        try{
            for(int i=1;i<=100;i++){

                GenericRecord avroRecord = new GenericData.Record(schema);
                avroRecord.put("name", "Candy");
                avroRecord.put("ID", "10001");
                avroRecord.put("gender","female");
                ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic,key,avroRecord);
                producer1.send(record);
                System.out.println("sent: "+String.valueOf(i));
            }

        }catch(SerializationException e){

        }
        finally{
            producer1.flush();
            producer1.close();
        }
    }
}