package gr.eshop.marios.EshopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminUserReadOnlyDTO {

    private Long id;
    private String uuid;
    private String firstname;
    private String lastname;

}
