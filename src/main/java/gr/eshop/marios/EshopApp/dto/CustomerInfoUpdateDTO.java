package gr.eshop.marios.EshopApp.dto;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerInfoUpdateDTO {


    @NotNull(message = "Το id δεν μπορεί να μην υπάρχει.")
    private Long Id;

    @NotNull(message = "Το τηλ δεν μπορεί να μην υπάρχει.")
    private String phoneNumber;

    @NotNull(message = "Το τηλ δεν μπορεί να μην υπάρχει.")
    private String country;

    @ManyToOne
    private String region;

    @NotNull(message = "H πόλη δεν μπορεί να μην υπάρχει.")
    private String city;

    @NotNull(message = "Ο δρόμος δεν μπορεί να μην υπάρχει.")
    private String street;

    @NotNull(message = "Το αρθμός οδού δεν μπορεί να μην υπάρχει.")
    private String streetNumber;

    @NotNull(message = "Ο ΤΚ δεν μπορεί να μην υπάρχει.")
    private String zipCode;


}
