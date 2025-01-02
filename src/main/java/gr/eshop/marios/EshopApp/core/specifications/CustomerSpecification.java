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

    /**
     * Creates a specification to filter customers by associated user username.
     *
     * @param username the username of the user associated with the customer
     * @return a specification for filtering customers
     */
    public static Specification<Customer> customerUserUsernameIs(String username) {
        return ((root, query, criteriaBuilder) -> {
            if (username == null || username.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("username"), username);
        });
    }

    /**
     * Creates a specification to filter customers by associated user active status.
     *
     * @param isActive the active status of the user associated with the customer
     * @return a specification for filtering customers
     */
    public static Specification<Customer> trUserIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
           if (isActive == null) {
               return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
           }

           Join<Customer, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("isActive"), isActive);
        });
    }

    /**
     * Creates a specification to filter customers by associated customer info phone number.
     *
     * @param phoneNumber the phone number in the customer info
     * @return a specification for filtering customers
     */
    public static Specification<Customer> trCustomerInfoPhoneIs(String phoneNumber) {
        return ((root, query, criteriaBuilder) -> {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Customer, CustomerInfo> customerInfo = root.join("customerInfo");
            return criteriaBuilder.equal(customerInfo.get("phoneNumber"), phoneNumber);
        });
    }

    /**
     * Creates a specification to filter customers by associated order ID.
     *
     * @param orderId the ID of the order associated with the customer
     * @return a specification for filtering customers
     */
    public static Specification<Customer> trCustomerOrderIs(String orderId) {
        return ((root, query, criteriaBuilder) -> {
            if (orderId == null || orderId.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Customer, Order> order = root.join("order");
            return criteriaBuilder.equal(order.get("orderId"), orderId);
        });
    }

    /**
     * Creates a specification to perform a case-insensitive search on a string field of the customer.
     *
     * @param field the name of the field to search
     * @param value the value to search for
     * @return a specification for filtering customers
     */
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
