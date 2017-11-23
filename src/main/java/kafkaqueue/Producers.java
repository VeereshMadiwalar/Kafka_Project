package kafkaqueue;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import student.proto.StudentOuterClass.Student;

//Handler Class to handle server request
public class Producers {
	
	public static void producer(String topic,student.proto.StudentOuterClass.Student Student) {

		if (topic == null) {
			System.err.println("Topic is null");
			System.exit(-1);
		}
		
		String topicName = topic;
		Properties configProperties = new Properties();
		KafkaProducer<String, Student> producer = null;
		
		try
		{
			//Configure the Producer
			
			configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
			
			configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
			
			ProducerRecord<String, Student> rec = null;
			
			configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"kafka.StudentSerializer");
			
			producer = new KafkaProducer<String, Student>(configProperties);

			rec = new ProducerRecord<String, Student>(topicName, Student);
						
			producer.send(rec);
		
		}
		catch(Exception e)
		{
		
			System.out.println(e);
			System.out.println(e.getStackTrace());
			
		}
		
		producer.close();
		
		System.out.println("Object got pushed to Kafka Queue Successfully");
	}

}