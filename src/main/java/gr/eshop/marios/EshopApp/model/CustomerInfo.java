package gr.eshop.marios.EshopApp.model;


import gr.eshop.marios.EshopApp.model.static_data.Region;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name= "customer_infos")
public class CustomerInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;


    private String country;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private String city;

    private String street;

    private String streetNumber;

    private String zipCode;

    @OneToOne
    @JoinColumn(name="customer_id", nullable = false)
    private Customer customer;

}
