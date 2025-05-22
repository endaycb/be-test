package com.greenda.be.test.model.order;

import com.greenda.be.test.model.item.ItemDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String orderNo;
    private ItemDto item;
    private Integer qty;
    private Integer price;

}
