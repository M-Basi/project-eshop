package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerUpdateDTO {


    @NotNull(message = "Το uuid δεν μπορεί να μην υπάρχει")
    private String uuid;


    @NotNull(message = "Το όνομα δεν μπορεί να μην υπάρχει")
    private String firstname;

    @NotNull(message = "Το επίθετο δεν μπορεί να μην υπάρχει")
    private String lastname;

}
