package gr.eshop.marios.EshopApp.dto;



import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerInsertDTO {

    @NotEmpty(message = "Ο userUuid είναι υποχρεωτικός")
    private String userUuid;

    @NotEmpty(message = "Το όνομα είναι υποχρεωτικό")
    private String firstname;

    @NotEmpty(message = "Το επίθετο είναι υποχρεωτικό")
    private String lastname;


}
