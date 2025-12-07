package example.asyncapi.service;

import java.time.LocalDateTime;

import example.asyncapi.model.DataRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataRequestFailure {
	private DataRequest originalRequest;
	private String errorMessage;
	private LocalDateTime failureTime;
	private int retryAttempts;
}
