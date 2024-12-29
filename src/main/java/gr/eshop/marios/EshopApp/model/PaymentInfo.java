package gr.eshop.marios.EshopApp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= "payment_info")
public class PaymentInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String card;

    private String cardName;

    private String expiredDate;

    private String cardValidation;

    @OneToOne
    @JoinColumn(name="customer_id", nullable = false)
    private Customer customer;


}
