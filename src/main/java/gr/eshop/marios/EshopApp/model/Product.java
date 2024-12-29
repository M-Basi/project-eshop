package gr.eshop.marios.EshopApp.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_uuid", columnList = "uuid"),
                @Index(name = "idx_name", columnList = "name"),
                @Index(name = "idx_brand", columnList = "brand_id"),
                @Index(name = "idx_category", columnList = "category_id"),
                @Index(name = "idx_isActive", columnList = "is_active"),
                @Index(name = "idx_inStock", columnList = "in_stock"),
        }
)
public class Product extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Example: "Laptop", "Smartphone", etc.

    @Column(nullable = false)
    private Integer quantity;


    private Boolean inStock;


    private String description;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id")
    private AttachmentPhoto attachmentPhoto;


    @PrePersist
    public void initializeUUID() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    public boolean isInStock() {
        return this.quantity > 0;
    }



}
