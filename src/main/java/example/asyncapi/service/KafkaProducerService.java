package example.asyncapi.service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import example.asyncapi.config.KafkaConfig;
import example.asyncapi.model.DataRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
	private final KafkaTemplate<String, DataRequest> kafkaTemplate;
	private static final int MAX_RETRIES = 3;
	private static final long INITIAL_RETRY_DELAY_MS = 1000; // 1 second
	private static final double BACKOFF_MULTIPLIER = 2.0;

	public void sendDataRequest(DataRequest request) {
		log.debug("Sending data request to Kafka: {}", request);
		sendWithRetry(request, 0, INITIAL_RETRY_DELAY_MS);
	}

	private void sendWithRetry(DataRequest request, int retryCount, long delayMs) {
		kafkaTemplate.send(KafkaConfig.DATA_REQUEST_TOPIC, "MACHINE1", request).addCallback(
				result -> handleSuccess(result, request),
				ex -> handleFailureWithRetry(ex, request, retryCount, delayMs));
	}

	private void handleSuccess(SendResult<String, DataRequest> result, DataRequest request) {
		log.debug("Message sent successfully: topic={}, partition={}, offset={}", result.getRecordMetadata().topic(),
				result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
	}

	private void handleFailureWithRetry(Throwable ex, DataRequest request, int retryCount, long delayMs) {
		log.warn("Attempt {} failed for message: {}", retryCount + 1, request, ex);

		if (retryCount < MAX_RETRIES) {
			// Calculate next retry delay with exponential backoff
			long nextDelayMs = (long) (delayMs * BACKOFF_MULTIPLIER);

			try {
				log.info("Retrying message in {} ms. Attempt {}/{}", delayMs, retryCount + 1, MAX_RETRIES);

				// Schedule retry after delay
				scheduleRetry(request, retryCount, nextDelayMs);

			} catch (Exception scheduleEx) {
				log.error("Failed to schedule retry", scheduleEx);
				sendToDlq(request, ex);
			}
		} else {
			log.error("Max retries ({}) exceeded. Sending to DLQ", MAX_RETRIES);
			sendToDlq(request, ex);
		}
	}

	private void scheduleRetry(DataRequest request, int retryCount, long nextDelayMs) {
		CompletableFuture.delayedExecutor(nextDelayMs, TimeUnit.MILLISECONDS)
				.execute(() -> sendWithRetry(request, retryCount + 1, nextDelayMs));
	}

	private void sendToDlq(DataRequest request, Throwable originalError) {
		try {
			// Enrich the request with failure information
			DataRequestFailure failure = new DataRequestFailure(request, originalError.getMessage(),
					LocalDateTime.now(), MAX_RETRIES);

			// Send to DLQ
			kafkaTemplate.send(KafkaConfig.DATA_REQUEST_DLQ, "MACHINE1", request).addCallback(dlqResult -> {
				log.info("Message sent to DLQ successfully after {} retries", MAX_RETRIES);
				persistFailureDetails(failure);
			}, dlqEx -> log.error("Failed to send message to DLQ", dlqEx));

		} catch (Exception dlqEx) {
			log.error("Critical: Failed to process message and DLQ handling failed", dlqEx);
			// Consider additional error handling strategies
			handleCriticalFailure(request, dlqEx);
		}
	}

	private void persistFailureDetails(DataRequestFailure failure) {
		try {
			// Implement persistence logic here
			// For example, save to database or write to a file
			log.info("Persisted failure details: {}", failure);
		} catch (Exception ex) {
			log.error("Failed to persist failure details", ex);
		}
	}

	private void handleCriticalFailure(DataRequest request, Exception ex) {
		// Implement critical failure handling
		// For example:
		// 1. Save to local storage
		// 2. Trigger alerts
		// 3. Send notifications
		log.error("Critical failure in message processing", ex);
	}
}