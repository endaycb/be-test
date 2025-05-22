package com.greenda.be.test;

import com.greenda.be.test.entity.Inventory;
import com.greenda.be.test.entity.Item;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.execption.NotFoundException;
import com.greenda.be.test.mapper.InventoryMapper;
import com.greenda.be.test.model.inventory.InventoryDto;
import com.greenda.be.test.model.inventory.InventoryRequestDto;
import com.greenda.be.test.model.inventory.InventoryType;
import com.greenda.be.test.repository.InventoryRepository;
import com.greenda.be.test.repository.ItemRepository;
import com.greenda.be.test.service.InventoryService;
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
public class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private ItemService itemService;

    @Test
    void testSaveTopUp() {
        Long id = 1L;
        InventoryRequestDto request = new InventoryRequestDto(1L, 5, InventoryType.T);
        Item item = new Item(id, "Pen", 10);
        Inventory inventory = new Inventory();

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toDto(any(Inventory.class))).thenReturn(new InventoryDto());

        InventoryDto result = inventoryService.save(null, request);

        assertNotNull(result);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testSaveWithdrawalAndSufficientStock() {
        Long id = 1L;
        InventoryRequestDto request = new InventoryRequestDto(id, 3, InventoryType.W);
        Item item = new Item(id, "Pen", 10);
        Inventory inventory = new Inventory();

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemService.getStock(id)).thenReturn(5); // sufficient stock, current stock is 5 and the withdrawal is 3
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toDto(any(Inventory.class))).thenReturn(new InventoryDto());

        InventoryDto result = inventoryService.save(null, request);

        assertNotNull(result);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void testSaveWithdrawalAndInsufficientStock() {
        Long id = 1L;
        InventoryRequestDto request = new InventoryRequestDto(id, 10, InventoryType.W);
        Item item = new Item(id, "Pen", 10);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemService.getStock(id)).thenReturn(5); // insufficient stock, try to withdrawal 10, but the stock is 5

        assertThrows(BadRequestException.class, () -> inventoryService.save(null, request));
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void testSaveInvalidItemId() {
        Long id = 99L;

        InventoryRequestDto request = new InventoryRequestDto(id, 5, InventoryType.T);
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> inventoryService.save(null, request));
    }

    @Test
    void testEdit() {
        Long id = 1L;
        InventoryRequestDto request = new InventoryRequestDto(id, 5, InventoryType.T);
        Inventory save = new Inventory();
        Item item = new Item(id, "Pen", 10);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(save));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(save)).thenReturn(save);
        when(inventoryMapper.toDto(save)).thenReturn(new InventoryDto());

        InventoryDto result = inventoryService.save(id, request);

        assertNotNull(result);
        verify(inventoryRepository).save(save);
    }

    @Test
    void testDelete() {
        Long id = 1L;

        doNothing().when(inventoryRepository).deleteById(id);
        inventoryService.delete(id);
        verify(inventoryRepository).deleteById(id);
    }

    @Test
    void testList() {
        Inventory inventory = new Inventory();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Inventory> page = new PageImpl<>(List.of(inventory));

        when(inventoryRepository.findAll(pageable)).thenReturn(page);
        when(inventoryMapper.toDtos(List.of(inventory))).thenReturn(List.of(new InventoryDto()));

        Page<InventoryDto> result = inventoryService.list(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGet() {
        Long id = 1L;
        Inventory inventory = new Inventory();
        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.toDto(inventory)).thenReturn(new InventoryDto());

        InventoryDto result = inventoryService.get(id);
        assertNotNull(result);
    }

    @Test
    void testGetNotFound() {
        Long id = 1L;

        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> inventoryService.get(id));
    }
}
