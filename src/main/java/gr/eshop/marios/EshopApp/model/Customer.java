package gr.eshop.marios.EshopApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "customers")
public class Customer extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerInfo customerInfo;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentInfo paymentInfo;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        if (order == null) orders = new HashSet<>();
        orders.add(order);
    }

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }




    public Set<Order> getAllOrders() {
        if (orders == null) orders = new HashSet<>();
        return Collections.unmodifiableSet(orders);
    }


}
