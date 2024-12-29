package gr.eshop.marios.EshopApp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= "orders")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true)
    private String orderTrackingNumber;

    private Double totalPrice;

    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();;

    public void addOrderedItem(OrderItem orderItem) {
        if (orderItems == null) orderItems = new HashSet<>();
        orderItems.add(orderItem);
    }


    @PrePersist
    public void onCreate() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
        if (orderTrackingNumber == null) orderTrackingNumber = generateTrackingNumber();

    }

    public Set<OrderItem> getAllOrderItems() {
        if (orderItems == null)  orderItems = new HashSet<>();
        return Collections.unmodifiableSet(orderItems);
    }

    public Double totalPrice() {
        Double totalPriceForItems = 0.0;
        if (orderItems == null) {
            totalPriceForItems = 0.0;
        } else {
            for (OrderItem orderItem : orderItems) {
                totalPriceForItems += orderItem.totalItemOrderPrice();
            }
        }
        return totalPriceForItems;
    }

    private String generateTrackingNumber() {
        return "TR-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }


}
