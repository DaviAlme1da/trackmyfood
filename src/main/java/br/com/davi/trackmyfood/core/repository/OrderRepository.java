package br.com.davi.trackmyfood.core.repository;

import br.com.davi.trackmyfood.core.enums.StatusOrder;
import br.com.davi.trackmyfood.core.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByStatus(StatusOrder status);

}
