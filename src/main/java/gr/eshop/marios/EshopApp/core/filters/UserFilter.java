package gr.eshop.marios.EshopApp.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserFilter extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String id;

    @Nullable
    private String username;

    @Nullable
    private Boolean isActive;
}
