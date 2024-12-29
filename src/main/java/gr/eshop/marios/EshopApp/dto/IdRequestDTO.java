package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IdRequestDTO {

    @NotNull(message = "Το id δεν μπορεί να μην υπάρχει.")
    private Long id;
}
