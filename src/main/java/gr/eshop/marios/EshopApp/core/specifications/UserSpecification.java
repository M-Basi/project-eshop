package gr.eshop.marios.EshopApp.core.specifications;

import gr.eshop.marios.EshopApp.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {

    }

    public static Specification<User> trUsersIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }


            return criteriaBuilder.equal(root.get("isActive"), isActive);
        });
    }

    public static Specification<User> trStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" +
                    value.toUpperCase() + "%");   // case-insensitive search
        });
    }
}
