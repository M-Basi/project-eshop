package gr.eshop.marios.EshopApp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BrandReadOnlyDTO {
    private Long id;

    private String brandName;
}
