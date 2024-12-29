package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {


    Optional<Customer> findByUuid(String uuid);
    Optional<Customer> findByLastname(String lastname);
    Optional<Customer> findByPaymentInfoId(Long id);
    Optional<Customer> findByCustomerInfoId(Long id);
    Optional<Customer> findByUserUuid(String uuid);





}
