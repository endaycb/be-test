package com.greenda.be.test.controller;

import com.greenda.be.test.model.item.ItemRequestDto;
import com.greenda.be.test.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("save")
    public BaseResponse<Object> save(@Valid @RequestBody ItemRequestDto request) {
        return BaseResponse.ok("Item has been saved", itemService.save(null, request));
    }

    @PostMapping("edit/{id}")
    public BaseResponse<Object> edit(@PathVariable Long id,
                                     @Valid @RequestBody ItemRequestDto request) {
        return BaseResponse.ok("Item has been saved", itemService.save(id, request));
    }

    @GetMapping("list")
    public BaseResponse<Object> list(Pageable pageable) {
        return BaseResponse.ok("Item found", itemService.list(pageable));
    }

    @GetMapping("get/{id}")
    public BaseResponse<Object> findById(@PathVariable Long id) {
        return BaseResponse.ok("Item found", itemService.get(id));
    }

    @GetMapping("delete/{id}")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        itemService.delete(id);
        return BaseResponse.ok("Item has been deleted");
    }

}
