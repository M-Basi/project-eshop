package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO {


    @NotNull(message = "Το id του προϊόντος είναι υποχρεωτικό")
    private Long id;

    @NotNull(message = "Το uuid του προϊόντος είναι υποχρεωτικό")
    private String uuid;

    @NotNull(message = "Το όνομα του προϊόντος είναι υποχρεωτικό")
    private String name;

    @NotNull(message = "Η επωνυμία του προϊόντος είναι υποχρεωτική")
    private String brandName;

    @NotNull(message = "O διεθνής κωδικός του προϊόντος είναι υποχρεωτική")
    private String sku;

    @NotNull(message = "Η τιμή είναι υποχρεωτική")
    private Double price;

    @NotNull(message = "Η κατηγορία του προϊόντος είναι υποχρεωτική")
    private String categoryName; // Example: "Laptop", "Smartphone", etc.

    @NotNull(message = "Η ποσότητα του προϊόντος είναι υποχρεωτική")
    private Integer quantity;


    private Boolean isActive;


    private Boolean inStock;

    private String description;


}
