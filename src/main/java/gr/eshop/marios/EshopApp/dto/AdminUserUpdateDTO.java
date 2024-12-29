package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdminUserUpdateDTO {

    @NotEmpty(message = "Το userUuid είναι υποχρεωτικό")
    private String userUuid;

    @NotNull(message = "Το όνομα δεν μπορεί να μην υπάρχει")
    private Long Id;

    @NotNull(message = "Το uuid δεν μπορεί να μην υπάρχει")
    private String uuid;

    @NotNull(message = "Το όνομα δεν μπορεί να μην υπάρχει")
    private String firstname;

    @NotNull(message = "Το επίθετο δεν μπορεί να μην υπάρχει")
    private String lastname;




}
