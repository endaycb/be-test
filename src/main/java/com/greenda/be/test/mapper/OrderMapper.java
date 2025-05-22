package com.greenda.be.test.mapper;

import com.greenda.be.test.entity.Order;
import com.greenda.be.test.model.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ItemMapper itemMapper;

    public OrderDto toDto(Order entity) {
        return OrderDto.builder()
                .orderNo(entity.getOrderNo())
                .item(itemMapper.toDto(entity.getItem()))
                .qty(entity.getQty())
                .price(entity.getPrice())
                .build();
    }

    public List<OrderDto> toDtos(List<Order> entities) {
        return entities.stream().map(this::toDto).toList();
    }

}
