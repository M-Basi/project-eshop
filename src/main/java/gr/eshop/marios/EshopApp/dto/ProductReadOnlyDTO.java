package gr.eshop.marios.EshopApp.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductReadOnlyDTO {

    private Long id;
    private String uuid;

    private String name;


    private BrandReadOnlyDTO brand;


    private String sku;


    private Double price;


    private CategoryReadOnlyDTO category; // Example: "Laptop", "Smartphone", etc.

    private Integer quantity;

    private String description;

    private Boolean isActive;

    private Boolean inStock;

    private AttachmentReadOnlyDTO image;
}
