package gr.eshop.marios.EshopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentInfoReadOnlyDTO {

    private Long id;
    private String card;
    private String cardName;
    private String expiredDate;
    private String cardValidation;

}
