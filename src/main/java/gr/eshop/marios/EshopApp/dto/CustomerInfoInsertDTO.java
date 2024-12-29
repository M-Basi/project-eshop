package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerInfoInsertDTO {

    @NotEmpty(message = "To customerUuid του customer είναι υποχρεωτικό")
    private String customerUuid;

    @NotEmpty(message = "Το τηλέφωνο είναι υποχρεωτικό")
    private String phoneNumber;

    private String country;

    @NotEmpty(message = "Η περιφέρεια είναι υποχρεωτική")
    private String region;


    @NotEmpty(message = "Η πόλη είναι υποχρεωτική")
    private String city;

    @NotEmpty(message = "Η οδός είναι υποχρεωτική")
    private String street;

    @NotEmpty(message = "Ο αριθμός οδού είναι υποχρεωτικός")
    private String streetNumber;

    @NotEmpty(message = "Ο ΤΚ είναι υποχρεωτικός")
    @Pattern(regexp = "^\\d{5}$", message = "Το ΤΚ πρεέπι να είναι 5 ψηφία")
    private String zipCode;
}
