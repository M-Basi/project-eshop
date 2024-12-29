package gr.eshop.marios.EshopApp.core.filters;


import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductFilters extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String id;

    @Nullable
    private String name;

    @Nullable
    private String sku;

    @Nullable
    private String brand;

    @Nullable
    private String category;


    @Nullable
    private Boolean isActive;

    @Nullable
    private Boolean isInStock;


}
