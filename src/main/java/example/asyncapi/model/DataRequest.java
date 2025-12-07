package example.asyncapi.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "attendance")
public class DataRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
	private Long id;
	private String data;
	private LocalDateTime dateTime;
	private LocalDateTime actualDataTime;
}
