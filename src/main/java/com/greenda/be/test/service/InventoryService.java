package com.greenda.be.test.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final InventoryMapper inventoryMapper;
    private final ItemService itemService;

    public InventoryDto save(Long id, InventoryRequestDto request) {
        Inventory inventory = !ObjectUtils.isEmpty(id) ?
                inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory not found.")) :
                new Inventory();

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundException("Item not found."));
        if (InventoryType.W.equals(request.getType())) {
            int stock = itemService.getStock(item.getId()) - request.getQty();

            if (stock < 0) throw new BadRequestException("Stock must not be negative");
        }

        inventory.setItem(item);
        inventory.setQty(request.getQty());
        inventory.setType(request.getType());

        inventoryRepository.save(inventory);

        return inventoryMapper.toDto(inventory);
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

    public Page<InventoryDto> list(Pageable pageable) {
        Page<Inventory> page = inventoryRepository.findAll(pageable);

        return new PageImpl<>(inventoryMapper.toDtos(page.getContent()), pageable, page.getTotalElements());
    }

    public InventoryDto get(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory not found."));
        return inventoryMapper.toDto(inventory);
    }

}
