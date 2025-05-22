package com.greenda.be.test.mapper;

import com.greenda.be.test.entity.Inventory;
import com.greenda.be.test.model.inventory.InventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryMapper {

    private final ItemMapper itemMapper;

    public InventoryDto toDto(Inventory entity) {
        return InventoryDto.builder()
                .id(entity.getId())
                .item(itemMapper.toDto(entity.getItem()))
                .qty(entity.getQty())
                .type(entity.getType())
                .build();
    }

    public List<InventoryDto> toDtos(List<Inventory> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
