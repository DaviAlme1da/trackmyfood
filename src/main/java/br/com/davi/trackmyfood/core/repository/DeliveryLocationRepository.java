package br.com.davi.trackmyfood.core.repository;

import br.com.davi.trackmyfood.core.model.DeliveryLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation,Long> {

    List<DeliveryLocation> findByOrderIdOrderByTimestampAsc(Long idOrder);

}
