package gr.eshop.marios.EshopApp.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerFilters extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String id;


    @Nullable
    private String username;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String lastname;

    @Nullable
    private String orderId;

    @Nullable
    private Boolean active;



}
