package com.greenda.be.test;

import com.greenda.be.test.entity.Item;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.mapper.ItemMapper;
import com.greenda.be.test.model.inventory.InventoryType;
import com.greenda.be.test.model.item.ItemDto;
import com.greenda.be.test.model.item.ItemRequestDto;
import com.greenda.be.test.repository.InventoryRepository;
import com.greenda.be.test.repository.ItemRepository;
import com.greenda.be.test.repository.OrderRepository;
import com.greenda.be.test.service.ItemService;
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
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @Test
    void testSave() {
        Long id = 1L;
        ItemRequestDto request = new ItemRequestDto("Pen", 10);
        Item item = new Item(id, "Pen", 10);

        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(any(Item.class))).thenReturn(new ItemDto(id, "Pen", 10, 0));

        ItemDto result = itemService.save(null, request);

        assertNotNull(result);
        assertEquals("Pen", result.getName());
    }

    @Test
    void testEdit() {
        Long id = 1L;
        Item item = new Item(id, "Old", 5);
        ItemRequestDto request = new ItemRequestDto("New", 20);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(new ItemDto(id, "New", 20, 0));

        ItemDto result = itemService.save(id, request);

        assertEquals("New", result.getName());
        assertEquals(20, result.getPrice());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        when(orderRepository.existsByItemId(id)).thenReturn(false);
        doNothing().when(itemRepository).deleteById(id);

        assertDoesNotThrow(() -> itemService.delete(id));
    }

    @Test
    void testDeleteWithOrder() {
        Long id = 1L;

        when(orderRepository.existsByItemId(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> itemService.delete(id));
    }

    @Test
    void testList() {
        Long id = 1L;
        Item item = new Item(id, "Pen", 5);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(List.of(item));

        when(itemRepository.findAll(pageable)).thenReturn(page);
        when(itemMapper.toDtos(List.of(item)))
                .thenReturn(List.of(new ItemDto(id, "Pen", 5, null)));
        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.T)).thenReturn(10);
        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.W)).thenReturn(2);
        when(orderRepository.sumQtyByItemId(id)).thenReturn(1);

        Page<ItemDto> result = itemService.list(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(7, result.getContent().getFirst().getStock()); // 10 - 2 - 1
    }

    @Test
    void testGet() {
        Long id = 1L;
        Item item = new Item(id, "Pen", 5);
        ItemDto dto = new ItemDto(id, "Pen", 5, null);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(dto);
        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.T)).thenReturn(10);
        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.W)).thenReturn(3);
        when(orderRepository.sumQtyByItemId(id)).thenReturn(2);

        ItemDto result = itemService.get(id);

        assertEquals(5, result.getStock()); // 10 - 3 - 2
    }

    @Test
    void testGetStock() {
        Long id = 1L;

        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.T)).thenReturn(10);
        when(inventoryRepository.sumQtyByItemIdAndType(id, InventoryType.W)).thenReturn(4);
        when(orderRepository.sumQtyByItemId(id)).thenReturn(3);

        int stock = itemService.getStock(id);
        assertEquals(3, stock); // 10 - 4 - 3
    }

    @Test
    void testGetStockAllNull() {
        Long id = 1L;

        when(inventoryRepository.sumQtyByItemIdAndType(anyLong(), eq(InventoryType.T))).thenReturn(null);
        when(inventoryRepository.sumQtyByItemIdAndType(anyLong(), eq(InventoryType.W))).thenReturn(null);
        when(orderRepository.sumQtyByItemId(anyLong())).thenReturn(null);

        int stock = itemService.getStock(id);
        assertEquals(0, stock);
    }

}
