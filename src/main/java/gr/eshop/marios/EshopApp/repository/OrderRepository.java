package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByUuid(String uuid);
    Optional<Order> findByCustomerUserId(Long id);

    Optional<Order> findByCustomerUserUuid(String uuid);
    Optional<Order> findByCustomerUuid(String uuid);
    Optional<Order> findByOrderTrackingNumber(String trackingNumber);



}
