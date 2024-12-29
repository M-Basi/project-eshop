package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {
    Optional<AdminUser> findByUuid(String uuid);
    Optional<AdminUser> findByUserUsername(String username);
    Optional<AdminUser> findByUserUuid(String uuid);

}
