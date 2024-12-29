package gr.eshop.marios.EshopApp.dto;

import gr.eshop.marios.EshopApp.model.static_data.Region;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerInfoReadOnlyDTO {

    private Long id;

    private String phoneNumber;

    private String country;

    private Region region;

    private String city;

    private String street;

    private String streetNumber;

    private String zipCode;
}
