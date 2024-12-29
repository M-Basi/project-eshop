package gr.eshop.marios.EshopApp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryReadOnlyDTO {
    private Long id;

    private String categoryName;
}
