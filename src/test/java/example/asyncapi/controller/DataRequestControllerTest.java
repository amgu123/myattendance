package example.asyncapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import example.asyncapi.model.DataRequest;
import example.asyncapi.service.DataRequestService;
import example.asyncapi.service.KafkaProducerService;

@WebMvcTest(DataRequestController.class)
class DataRequestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DataRequestService dataRequestService;

	@MockBean
	private KafkaProducerService kafkaProducerService;

	@Test
	void getAllRequests_ShouldReturnListOfRequests() throws Exception {
		DataRequest request = new DataRequest(1L, "test", LocalDateTime.now(),LocalDateTime.now());
		when(dataRequestService.getAllRequests()).thenReturn(Arrays.asList(request));

		mockMvc.perform(get("/api/requests")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[0].id").value("1"))
				.andExpect(jsonPath("$[0].content").value("test"));
	}

	@Test
	void getRequestById_WhenExists_ShouldReturnRequest() throws Exception {
		DataRequest request = new DataRequest(1L, "test", LocalDateTime.now(), LocalDateTime.now());
		when(dataRequestService.getRequestById(1L)).thenReturn(Optional.of(request));

		mockMvc.perform(get("/api/requests/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.content").value("test"));
	}

	@Test
	void getRequestById_WhenNotExists_ShouldReturn404() throws Exception {
		when(dataRequestService.getRequestById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/requests/1")).andExpect(status().isNotFound());
	}
}