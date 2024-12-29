package gr.eshop.marios.EshopApp.core.specifications;


import gr.eshop.marios.EshopApp.model.Customer;
import gr.eshop.marios.EshopApp.model.CustomerInfo;
import gr.eshop.marios.EshopApp.model.Order;
import gr.eshop.marios.EshopApp.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    private CustomerSpecification() {

    }

    public static Specification<Customer> customerUserUsernameIs(String username) {
        return ((root, query, criteriaBuilder) -> {
            if (username == null || username.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("username"), username);
        });
    }

    public static Specification<Customer> trUserIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
           if (isActive == null) {
               return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
           }

           Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("isActive"), isActive);
        });
    }

    public static Specification<Customer> trCustomerInfoPhoneIs(String phoneNumber) {
        return ((root, query, criteriaBuilder) -> {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Customer, CustomerInfo> customerInfo = root.join("customerInfo");
            return criteriaBuilder.equal(customerInfo.get("phoneNumber"), phoneNumber);
        });
    }

    public static Specification<Customer> trCustomerOrderIs(String orderId) {
        return ((root, query, criteriaBuilder) -> {
            if (orderId == null || orderId.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Customer, Order> order = root.join("order");
            return criteriaBuilder.equal(order.get("orderId"), orderId);
        });
    }

    public static Specification<Customer> trStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" +
                    value.toUpperCase() + "%");   // case-insensitive search
        });
    }
}
