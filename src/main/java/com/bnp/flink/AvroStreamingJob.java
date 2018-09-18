package com.bnp.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;

import java.util.Properties;
public class AvroStreamingJob {
    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "s00vl9982859:9092,s00vl9982860:9092,s00vl9982666:9092,s00vl9982874:9092,s00vl9982875:9092,s00vl9982876:9092");
        properties.put("group.id", "test");

        //configure the following three settings for SSL Encryption
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "C:/work/big_data/kafkafactory.jks");
        properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "Kafka2018");

        // configure the following three settings for SSL Authentication
        properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "C:/work/big_data/kafkafactory.jks");
        properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "Kafka2018");
        properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "Kafka2018");
        properties.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.put("schema.registry.url", "http://s00vl9982862.fr.net.intra:8081");

        FlinkKafkaConsumer010<String> myConsumer = new FlinkKafkaConsumer010<>("flinkAvro02", new SimpleStringSchema(), properties);
        myConsumer.setStartFromEarliest();


        DataStream<String> stream = env.addSource(myConsumer);
        stream.print();

        env.execute();
    }


}
