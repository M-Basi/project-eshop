package gr.eshop.marios.EshopApp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="orderItems")
public class OrderItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Example: "Laptop", "Smartphone", etc.

    @ManyToOne
    @JoinColumn(name="attachmentPhoto_id", nullable = false)
    private AttachmentPhoto attachmentPhoto;

    private Double unitPrice;

    private Integer quantity;

    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public Double totalItemOrderPrice() {
        return (double) Math.round(this.unitPrice * this.quantity);
    }




}








