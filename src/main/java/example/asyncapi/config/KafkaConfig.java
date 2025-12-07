package example.asyncapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import example.asyncapi.model.DataRequest;

@Configuration
public class KafkaConfig {
	public static final String DATA_REQUEST_TOPIC = "data-requests";
    public static final String DATA_REQUEST_DLQ = "data-request-dlq";


	@Bean
	public NewTopic dataRequestTopic() {
		return TopicBuilder.name(DATA_REQUEST_TOPIC).partitions(1).replicas(1).build();
	}

	@Bean
	public KafkaTemplate<String, DataRequest> kafkaTemplate(ProducerFactory<String, DataRequest> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

}