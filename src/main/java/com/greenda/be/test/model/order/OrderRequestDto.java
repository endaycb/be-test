package com.greenda.be.test.model.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "itemId must not be empty")
    private Long itemId;

    @NotNull(message = "qty must not be empty")
    private Integer qty;

}
