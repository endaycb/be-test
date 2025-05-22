package com.greenda.be.test.model.inventory;

import com.greenda.be.test.model.item.ItemDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDto {

    @NotNull(message = "itemId must not be empty")
    private Long itemId;

    @NotNull(message = "qty must not be empty")
    private Integer qty;

    @NotNull(message = "type must not be empty")
    private InventoryType type;

}
