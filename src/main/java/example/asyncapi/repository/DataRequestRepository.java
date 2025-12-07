package example.asyncapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.asyncapi.model.DataRequest;

@Repository
public interface DataRequestRepository extends JpaRepository<DataRequest, Long> {
}