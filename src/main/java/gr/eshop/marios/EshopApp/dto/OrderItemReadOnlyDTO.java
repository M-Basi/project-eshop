package gr.eshop.marios.EshopApp.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemReadOnlyDTO {


    private Long id;

    private String name;

    private String sku;

    private AttachmentReadOnlyDTO image;

    private Double price;

    private String brand;

    private String category;



    private Integer quantity;

    private Double totalPrice;

}
