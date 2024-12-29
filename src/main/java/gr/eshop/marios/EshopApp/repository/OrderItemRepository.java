package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    Optional<OrderItem> findByOrderId(Long id);
}
