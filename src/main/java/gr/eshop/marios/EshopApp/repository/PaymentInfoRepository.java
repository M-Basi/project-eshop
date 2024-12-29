package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long>, JpaSpecificationExecutor<PaymentInfo> {
    Optional<PaymentInfo> findByCustomerId(Long id);
    Optional<PaymentInfo> findByCustomerUuid(String uuid);
    Optional<PaymentInfo> findByCustomerUserId(Long id);
    Optional<PaymentInfo> findByCustomerUserUuid(String uuid);
}
