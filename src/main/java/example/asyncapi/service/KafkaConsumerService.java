package example.asyncapi.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import example.asyncapi.config.KafkaConfig;
import example.asyncapi.model.DataRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
	private final DataRequestService dataRequestService;

	@KafkaListener(topics = KafkaConfig.DATA_REQUEST_TOPIC)
	public void consumeDataRequest(DataRequest request) throws InterruptedException {
		log.debug("Received data request from Kafka: {}", request);
		System.out.println("------KafkaConsumerService----------"+request.toString());
		dataRequestService.saveRequest(request);
	}
}