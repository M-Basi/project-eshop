package gr.eshop.marios.EshopApp.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerReadOnlyDTO {


    private String uuid;
    private String firstname;
    private String lastname;
    private UserReadOnlyDTO userReadOnlyDTO;
    private CustomerInfoReadOnlyDTO customerInfoReadOnlyDTO;
    private PaymentInfoReadOnlyDTO paymentInfoReadOnlyDTO;
    private Set<OrderReadOnlyDTO> ordersReadOnlyDTOs;

}
