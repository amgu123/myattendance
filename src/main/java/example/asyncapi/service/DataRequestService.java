package example.asyncapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.asyncapi.model.DataRequest;
import example.asyncapi.repository.DataRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataRequestService {
	private final DataRequestRepository repository;

	@Transactional(readOnly = true)
	public List<DataRequest> getAllRequests() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<DataRequest> getRequestById(Long id) {
		return repository.findById(id);
	}

	@Transactional
	public DataRequest saveRequest(DataRequest request) throws InterruptedException {
		//Thread.sleep(3000); // Pause for 3 seconds
		System.out.println("saving into the database--" + request);
		return repository.save(request);
	}

	@Transactional
	public void deleteRequest(Long id) {
		repository.deleteById(id);
	}
}