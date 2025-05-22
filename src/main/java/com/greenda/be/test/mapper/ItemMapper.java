package com.greenda.be.test.mapper;

import com.greenda.be.test.entity.Item;
import com.greenda.be.test.model.item.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemDto toDto(Item entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }

    public List<ItemDto> toDtos(List<Item> entities) {
        return entities.stream().map(this::toDto).toList();
    }

}
