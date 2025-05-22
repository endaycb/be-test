package com.greenda.be.test.model.inventory;

import com.greenda.be.test.model.item.ItemDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

    private Long id;
    private ItemDto item;
    private Integer qty;
    private InventoryType type;

}
