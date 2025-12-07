package example.asyncapi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.asyncapi.model.DataRequest;
import example.asyncapi.service.DataRequestService;
import example.asyncapi.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class DataRequestController {
	private final DataRequestService dataRequestService;
	private final KafkaProducerService kafkaProducerService;

	@GetMapping
	public ResponseEntity<List<DataRequest>> getAllRequests() {
		return ResponseEntity.ok(dataRequestService.getAllRequests());
	}

	@GetMapping("/test/{count}")
	public ResponseEntity<Object> callAPIMultipleTimes(@PathVariable String count) {

		Integer cc = Integer.parseInt(count);
		for (int i = 0; i <= cc; i++) {
			DataRequest re = new DataRequest();
			re.setActualDataTime(LocalDateTime.now());
			re.setData(i + "");
			System.err.println("calling api " + i + " ----");
			kafkaProducerService.sendDataRequest(re);
		}

		return ResponseEntity.ok("ok");
	}

	@GetMapping("/{id}")
	public ResponseEntity<DataRequest> getRequestById(@PathVariable Long id) {
		return dataRequestService.getRequestById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Object> createRequest(@RequestBody DataRequest request) {
		// request.setId(UUID.randomUUID().toString());
		request.setActualDataTime(LocalDateTime.now());

		System.out.println("--------rew ----" + request);
		// Send to Kafka
		try {
			kafkaProducerService.sendDataRequest(request);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
		}

		return ResponseEntity.accepted().body("ok");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
		dataRequestService.deleteRequest(id);
		return ResponseEntity.noContent().build();
	}
}