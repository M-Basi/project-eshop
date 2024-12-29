package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderItemInsertDTO {

    @NotNull(message = "O διεθνής κωδικός του προϊόντος είναι υποχρεωτική")
    private String sku;


    @NotNull(message = "Η ποσότητα του προϊόντος είναι υποχρεωτική")
    private Integer quantity;



}
