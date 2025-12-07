package example.asyncapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {
	@Bean
	public NewTopic dataRequestTopic() {
		return TopicBuilder.name(KafkaConfig.DATA_REQUEST_TOPIC).partitions(3).replicas(2).build();
	}

	@Bean
	public NewTopic dlqTopic() {
		return TopicBuilder.name(KafkaConfig.DATA_REQUEST_DLQ).partitions(1).replicas(2).build();
	}
}
