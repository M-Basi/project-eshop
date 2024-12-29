package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long>, JpaSpecificationExecutor<CustomerInfo> {
    Optional<CustomerInfo> findByCustomerUserId(Long id);
    Optional<CustomerInfo> findByCustomerUuid(String uuid);
    Optional<CustomerInfo> findByCustomerId(Long id);

}
