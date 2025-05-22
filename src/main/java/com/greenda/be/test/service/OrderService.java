package com.greenda.be.test.service;

import com.greenda.be.test.entity.Item;
import com.greenda.be.test.entity.Order;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.execption.NotFoundException;
import com.greenda.be.test.mapper.OrderMapper;
import com.greenda.be.test.model.order.OrderDto;
import com.greenda.be.test.model.order.OrderRequestDto;
import com.greenda.be.test.repository.ItemRepository;
import com.greenda.be.test.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemService itemService;

    public OrderDto save(String id, OrderRequestDto request) {
        Order order = !ObjectUtils.isEmpty(id) ?
                orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found.")) :
                new Order();

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundException("Item not found."));
        int stock = itemService.getStock(item.getId());

        if (stock < request.getQty()) throw new BadRequestException("Insufficient stock");

        order.setItem(item);
        order.setQty(request.getQty());
        order.setPrice(item.getPrice());

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    public void delete(String id) {
        orderRepository.deleteById(id);
    }

    public Page<OrderDto> list(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);

        return new PageImpl<>(orderMapper.toDtos(page.getContent()), pageable, page.getTotalElements());
    }

    public OrderDto get(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        return orderMapper.toDto(order);
    }

}
