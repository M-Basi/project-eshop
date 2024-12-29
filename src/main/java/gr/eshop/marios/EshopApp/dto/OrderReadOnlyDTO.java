package gr.eshop.marios.EshopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderReadOnlyDTO {

    private Long id;
    private String uuid;
    private String orderTrackingNumber;
    private String createdDate;
    private Double totalPrice;
    private String status;
    private Set<OrderItemReadOnlyDTO> orderItems;


}
