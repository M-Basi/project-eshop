package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {



    @NotNull(message = "Το uuid δεν μπορεί να μην υπάρχει.")
    private String uuid;

    @NotNull(message = "Το email δεν μπορεί να μην υπάρχει.")
    @Email(message = "Μη έγκυρο email")
    private String username;

    @NotNull(message = "Το password δεν μπορεί να μην υπάρχει.")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$%!^&*]).{8,}$", message = "Invalid Password")
    private String password;

}
