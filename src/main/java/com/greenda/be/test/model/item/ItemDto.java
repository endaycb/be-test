package com.greenda.be.test.model.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {

    private Long id;
    private String name;
    private Integer price;
    private Integer stock;

}
