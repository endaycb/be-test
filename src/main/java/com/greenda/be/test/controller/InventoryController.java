package com.greenda.be.test.controller;

import com.greenda.be.test.model.inventory.InventoryRequestDto;
import com.greenda.be.test.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("save")
    public BaseResponse<Object> save(@Valid @RequestBody InventoryRequestDto request) {
        return BaseResponse.ok("Inventory has been saved", inventoryService.save(null, request));
    }

    @PostMapping("edit/{id}")
    public BaseResponse<Object> edit(@PathVariable Long id,
                                     @Valid @RequestBody InventoryRequestDto request) {
        return BaseResponse.ok("Inventory has been saved", inventoryService.save(id, request));
    }

    @GetMapping("list")
    public BaseResponse<Object> list(Pageable pageable) {
        return BaseResponse.ok("Inventory found", inventoryService.list(pageable));
    }

    @GetMapping("get/{id}")
    public BaseResponse<Object> findById(@PathVariable Long id) {
        return BaseResponse.ok("Inventory found", inventoryService.get(id));
    }

    @GetMapping("delete/{id}")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return BaseResponse.ok("Inventory has been deleted");
    }

}
