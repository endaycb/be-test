package com.greenda.be.test;

import com.greenda.be.test.entity.Item;
import com.greenda.be.test.entity.Order;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.execption.NotFoundException;
import com.greenda.be.test.mapper.OrderMapper;
import com.greenda.be.test.model.order.OrderDto;
import com.greenda.be.test.model.order.OrderRequestDto;
import com.greenda.be.test.repository.ItemRepository;
import com.greenda.be.test.repository.OrderRepository;
import com.greenda.be.test.service.ItemService;
import com.greenda.be.test.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ItemService itemService;

    @Test
    void testSaveSufficientStock() {
        Long itemId = 1L;
        OrderRequestDto request = new OrderRequestDto(itemId, 5);
        Item item = new Item(itemId, "Pen", 10000);
        Order order = new Order();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemService.getStock(itemId)).thenReturn(10); // sufficient stock, current stock is 10 and the order is 5
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto result = orderService.save(null, request);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testSaveInsufficientStock() {
        Long itemId = 1L;
        OrderRequestDto request = new OrderRequestDto(itemId, 15);
        Item item = new Item(itemId, "Pen", 10000);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemService.getStock(itemId)).thenReturn(10); // insufficient stock, try to order 10, but the stock is 5

        assertThrows(BadRequestException.class, () -> orderService.save(null, request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testSaveUpdateExistingOrder() {
        Long itemId = 1L;
        String existingOrderNo = "O1";
        OrderRequestDto request = new OrderRequestDto(itemId, 3);
        Item item = new Item(itemId, "Pen", 10000);
        Order existingOrder = new Order();
        existingOrder.setOrderNo(existingOrderNo);

        when(orderRepository.findById(existingOrderNo)).thenReturn(Optional.of(existingOrder));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemService.getStock(itemId)).thenReturn(10);
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.toDto(existingOrder)).thenReturn(new OrderDto());

        OrderDto result = orderService.save(existingOrderNo, request);

        assertNotNull(result);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void testSaveWithInvalidItemIdThrows() {
        Long itemId = 99L;
        OrderRequestDto request = new OrderRequestDto(itemId, 5);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.save(null, request));
    }

    @Test
    void testDelete() {
        String orderNo = "O1";
        doNothing().when(orderRepository).deleteById(orderNo);

        orderService.delete(orderNo);

        verify(orderRepository).deleteById(orderNo);
    }

    @Test
    void testList() {
        String orderNo = "O1";
        Pageable pageable = PageRequest.of(0, 10);
        Order order = new Order();
        order.setOrderNo(orderNo);

        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(pageable)).thenReturn(page);
        when(orderMapper.toDtos(List.of(order))).thenReturn(List.of(new OrderDto()));

        Page<OrderDto> result = orderService.list(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGet() {
        String orderNo = "O1";
        Order order = new Order();
        order.setOrderNo(orderNo);

        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(new OrderDto());

        OrderDto result = orderService.get(orderNo);

        assertNotNull(result);
    }

    @Test
    void testGetNotFound() {
        String orderNo = "O1";

        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.get(orderNo));
    }
}