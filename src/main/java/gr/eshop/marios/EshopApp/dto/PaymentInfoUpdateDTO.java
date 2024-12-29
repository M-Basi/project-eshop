package gr.eshop.marios.EshopApp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentInfoUpdateDTO {


    @NotNull(message = "To id δεν μπορεί να μην υπάρχει.")
    private Long id;

    @NotNull(message = "Ο αριθμός κάρτας δεν μπορεί να μην υπάρχει.")
    @Pattern(regexp = "^\\d{16}$", message = "Ο Αριθμός κάρτας πρέπει να είναι 16 ψηφία")
    private String card;

    @NotNull(message = "Το όνομα κατόχου κάρτας είναι υποχρεωτικό")
    private String cardName;

    @NotNull(message = "H Ημερομηνία λήξης κάρτας είναι υποχρεωτικό")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "H ημερομηνία λήξης πρέπει να είναι της μορφής ΜΜ/ΥΥ")
    private String expiredDate;

    @NotNull(message = "Το CVV είναι υποχρεωτικό")
    @Pattern(regexp = "^\\d{3}$", message = "Το CVV πρέπει να είναι 3 ψηφία")
    private String cardValidation;




}
