package gr.eshop.marios.EshopApp.repository;


import gr.eshop.marios.EshopApp.model.Brand;
import gr.eshop.marios.EshopApp.model.Category;
import gr.eshop.marios.EshopApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByUuid(String uuid);
    Optional<Product> findByName(String name);
    Optional<Product> findByBrand(Brand brand);
    Optional<Product> findByCategory(Category category);
    Optional<Product> findBySku(String sku);
    Optional<Product> findByIsActive(Boolean isActive);
    Optional<Product> findByInStock(Boolean inStock);
    Optional<Product> findByCategoryId(Long categoryId);
    Optional<Product> findByBrandId(Long brandId);

}
