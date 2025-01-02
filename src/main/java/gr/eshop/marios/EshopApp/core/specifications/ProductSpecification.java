package gr.eshop.marios.EshopApp.core.specifications;

import gr.eshop.marios.EshopApp.model.Brand;
import gr.eshop.marios.EshopApp.model.Category;
import gr.eshop.marios.EshopApp.model.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    private ProductSpecification() {}


    /**
     * Creates a specification to filter products by active status.
     *
     * @param isActive the active status of the product
     * @return a specification for filtering products
     */
    public static Specification<Product> trProductIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }


            return criteriaBuilder.equal(root.get("isActive"), isActive);
        });
    }


    /**
     * Creates a specification to filter products by stock availability.
     *
     * @param isInStock the stock availability of the product
     * @return a specification for filtering products
     */
    public static Specification<Product> trProductIsInStock(Boolean isInStock) {
        return ((root, query, criteriaBuilder) -> {
            if (isInStock == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }


            return criteriaBuilder.equal(root.get("inStock"), isInStock);
        });
    }

    /**
     * Creates a specification to filter products by brand name.
     *
     * @param brandName the name of the brand associated with the product
     * @return a specification for filtering products
     */
    public static Specification<Product> trProductByBrand(String brandName) {
        return ((root, query, criteriaBuilder) -> {
            if (brandName == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Product, Brand> brand = root.join("brand");
            return criteriaBuilder.equal(brand.get("brandName"), brandName);
        });
    }

    /**
     * Creates a specification to filter products by category name.
     *
     * @param categoryName the name of the category associated with the product
     * @return a specification for filtering products
     */
    public static Specification<Product> trProductByCategory(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Product, Category> category = root.join("category");
            return criteriaBuilder.equal(category.get("categoryName"), categoryName);
        });
    }


    /**
     * Creates a specification to perform a case-insensitive search on a string field of the product.
     *
     * @param field the name of the field to search
     * @param value the value to search for
     * @return a specification for filtering products
     */
    public static Specification<Product> trStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");   // case-insensitive search
        });
    }

}
