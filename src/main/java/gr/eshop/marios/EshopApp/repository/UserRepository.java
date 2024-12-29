package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.core.enums.Role;
import gr.eshop.marios.EshopApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    Optional<User> findByUuid(String uuid);
    Optional<User> findByCustomerLastname(String lastname);
    Optional<User> findByRole(Role role);
    Optional<User> findByIsActive(Boolean isActive);
    Optional<User> findByUsername(String username);



}
