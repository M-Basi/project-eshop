package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderInsertDTO {

    @NotEmpty(message = "To customerUuid είναι υποχρεωτική")
    private String customerUuid;


    private String status;

    @NotEmpty(message = "Το επίθετο είναι υποχρεωτικό")
    private List<OrderItemInsertDTO> orderItems;
}
