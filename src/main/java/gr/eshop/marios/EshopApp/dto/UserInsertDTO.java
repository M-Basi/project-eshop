package gr.eshop.marios.EshopApp.dto;

import gr.eshop.marios.EshopApp.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInsertDTO {


    @Email(message = "Μη έγκυρο email")
    private String username;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$%!^&*]).{8,}$", message = "Invalid Password")
    private String password;



    private Role role;


    private Boolean isActive;



    


}
