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

public class SimpleProducer
{
    public static String[] readText(String s){
        s.toLowerCase();
        //String[] str = s.split("\\s+|\\.+|\\'+|\\\"+|\\,+");
        String[] str = s.split("\\n+");

        return str;
    }
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
    public static void myProducer(String topic, String[] str){
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

        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        int i = 0;
        try{
            for(String word: str){

                if(!word.equals("")){i ++;
                    producer.send(new ProducerRecord<String,String>(topic,Integer.toString(i),(word)));
                    System.out.println("send k: "+i + ", value: "+word);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            producer.close();
        }

    }
    public static <V> void main(String[] args )
    {
        String string = "Canalys senior analyst Ben Stanton said to The Verge, “Apple would not delay the launch of iPhone XR for any reason other than the device is not ready to be shipped at \n"
                + "adequate volume.” He elaborated, “The constraint with the device is around the new LCD display, which has bottlenecked production.”\n\n"
                + "Apple has not responded to multiple requests for comment. US-based IDC and Singapore-based Canalys track the parts that go into Apple devices and come up with estimates, \n"
                + "and from those estimates they provide analysis.\n\n"
                + "Stanton said he doesn’t expect customers will be so eager to switch over to a new iPhone as soon as possible that they’ll end up buying a iPhone XS or XS Max model in \n"
                + "the interim. “The price differential between the iPhone XS and XR is so great,” he said. “Consumers will not be upsold on impulse.”\n\n"
                + "APPLE KNOWS PEOPLE WANT CHEAPER SMARTPHONES, SO IT’S COUNTING ON THE IPHONE XR\n"
                + "Despite the one month delay in the iPhone XR, both analysts agreed that Apple is betting hard on its cheaper product. Reith said that Apple knew it was pushing its \n"
                + "luck on increasing iPhone prices up to $1,449 this year for the 512GB storage option on the XS Max.\n\n"
                + "“Apple has heard there’s a threshold for pricing. They realize there are a lot of people that won’t go that high,” he said. “To the average consumer, the products \n"
                + "will look the same from the outside... and this LCD device is a really good looking device.” If the average person looks at the three iPhones together, Reith reasoned, they’ll go for the cheapest one,\n"
                + " seeing that it’s also bigger than the iPhone XS, so it might have better value.\n\n"
                + "That’s why the delay might cost Apple some sales on its iPhone XR model, as people might end up buying the XS Max while Apple struggles with its LCD supply issues. \n"
                + "“I think it’s a real possibility,” said Reith, “Consumers can’t tell the difference. If they see two phones, and one of them is significantly bigger, people will just go to that.\n”"
                + "APPLE HAS GOT fanbois hot under the collar with the unveiling of the iPhone XS, iPhone XS Max and iPhone XR.\n\n"
                + "The iDevice trio had few surprises in store thanks to the huge number of leaks building up to Apple's launch event; the iPhone XS and XS Max pack 5.8in and 6.5in OLED screens, respectively, while the \"low-end\" XR features a 6.1in LCD 'Liquid Retina' screen. \n\n"
                + "All three models are the first to feature Apple's homegrown A12 Bionic CPU, the first commercially available 7nm processor, and all come adorned with the notched display that first debuted on last year's iPhone X.\n\n"
                + "We've rounded up everything you need to know about Apple's iPhone XS, XS Max and XR below. \n\n"
                + "Apple has dabbled in cheaper iPhones before, with the iPhone SE and iPhone 5C, but their designs stood apart from \n"
                + "the mainstream iPhones, and they didn't last. Here, the cheaper iPhone XR shares the design and many of the same specs \n"
                + "as the top-tier iPhones. It comes in bright colors and has some slimmed-down features, like a smaller battery than the new iPhone XSes.\n"
                + "The pricing is the most attractive for iPhone loyalists. Although there's nothing \"budget\" about the iPhone XR's $749 \n"
                + "starting price, people looking for a new iPhone this year could easily gravitate to the relatively cheaper XR.";
        String [] str = readText(string);
        myProducer("flinkAvro1",str);

    }
}