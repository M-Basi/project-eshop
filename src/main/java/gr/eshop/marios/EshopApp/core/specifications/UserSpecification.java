package gr.eshop.marios.EshopApp.core.specifications;

import gr.eshop.marios.EshopApp.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {

    }

    /**
     * Creates a specification to filter users by active status.
     *
     * @param isActive the active status of the user
     * @return a specification for filtering users
     */
    public static Specification<User> trUsersIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }


            return criteriaBuilder.equal(root.get("isActive"), isActive);
        });
    }

    /**
     * Creates a specification to perform a case-insensitive search on a string field of the user.
     *
     * @param field the name of the field to search
     * @param value the value to search for
     * @return a specification for filtering users
     */
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
