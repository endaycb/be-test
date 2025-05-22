package com.greenda.be.test.model.item;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    @NotNull(message = "name must not be empty")
    private String name;

    @NotNull(message = "price must not be empty")
    private Integer price;

}
