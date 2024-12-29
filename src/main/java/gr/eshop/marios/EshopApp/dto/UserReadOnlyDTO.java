package gr.eshop.marios.EshopApp.dto;

import gr.eshop.marios.EshopApp.core.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadOnlyDTO {



    private String uuid;

    private String username;



    private Role role;

    private Boolean isActive;





}
