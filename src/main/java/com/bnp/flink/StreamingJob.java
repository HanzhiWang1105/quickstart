/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public class StreamingJob {


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
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		FlinkKafkaConsumer010<String> myConsumer = new FlinkKafkaConsumer010<>("flink02", new SimpleStringSchema(), properties);
		myConsumer.setStartFromEarliest();


		DataStream<String> stream = env.addSource(myConsumer);
		DataStream<WordWithCount> windowCounts = stream.flatMap(new FlatMapFunction<String, WordWithCount>() {
			@Override
			public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
				for(String word: value.split("\\s+|\\.+|\\'+|\\\"+|\\,+")){
					word = word.toLowerCase();
					out.collect(new WordWithCount(word,1L));
				}
			}
		}).keyBy("word")
				.timeWindow(Time.seconds(5),Time.seconds(1))
				.reduce(new ReduceFunction<WordWithCount>() {
					@Override
					public WordWithCount reduce(WordWithCount a, WordWithCount b) throws Exception {
						return new WordWithCount(a.word, a.count + b.count);
					}
				});
		windowCounts.print();
		env.execute();
	}


	public static class WordWithCount {

		public String word;
		public long count;

		public WordWithCount() {}

		public WordWithCount(String word, long count) {
			this.word = word;
			this.count = count;
		}

		@Override
		public String toString() {
			return word + " : " + count;
		}
	}
}
