package com.greenda.be.test.service;

import com.greenda.be.test.entity.Item;
import com.greenda.be.test.execption.BadRequestException;
import com.greenda.be.test.execption.NotFoundException;
import com.greenda.be.test.mapper.ItemMapper;
import com.greenda.be.test.model.inventory.InventoryType;
import com.greenda.be.test.model.item.ItemDto;
import com.greenda.be.test.model.item.ItemRequestDto;
import com.greenda.be.test.repository.InventoryRepository;
import com.greenda.be.test.repository.ItemRepository;
import com.greenda.be.test.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemDto save(Long id, ItemRequestDto request) {
        Item item = !ObjectUtils.isEmpty(id) ?
                itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found.")) :
                new Item();

        item.setName(request.getName());
        item.setPrice(request.getPrice());

        itemRepository.save(item);

        return itemMapper.toDto(item);
    }

    public void delete(Long id) {
        if (orderRepository.existsByItemId(id)) {
            throw new BadRequestException("Item can’t be deleted because an order related to this item has been created.");
        }

        itemRepository.deleteById(id);
    }

    public Page<ItemDto> list(Pageable pageable) {
        Page<Item> page = itemRepository.findAll(pageable);
        List<ItemDto> dtos = itemMapper.toDtos(page.getContent());
        dtos.forEach(dto -> {
            dto.setStock(getStock(dto.getId()));
        });

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public ItemDto get(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found."));
        ItemDto dto = itemMapper.toDto(item);
        dto.setStock(getStock(dto.getId()));
        return dto;
    }

    public int getStock(Long itemId) {
        Integer topUp = inventoryRepository.sumQtyByItemIdAndType(itemId, InventoryType.T);
        Integer withdrawal = inventoryRepository.sumQtyByItemIdAndType(itemId, InventoryType.W);
        Integer ordered = orderRepository.sumQtyByItemId(itemId);
        topUp = topUp == null ? 0 : topUp;
        withdrawal = withdrawal == null ? 0 : withdrawal;
        ordered = ordered == null ? 0 : ordered;

        return topUp - withdrawal - ordered;
    }

}
