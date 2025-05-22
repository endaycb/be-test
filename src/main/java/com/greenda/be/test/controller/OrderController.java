package com.greenda.be.test.controller;

import com.greenda.be.test.model.order.OrderRequestDto;
import com.greenda.be.test.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("save")
    public BaseResponse<Object> save(@Valid @RequestBody OrderRequestDto request) {
        return BaseResponse.ok("Order has been saved", orderService.save(null, request));
    }

    @PostMapping("edit/{id}")
    public BaseResponse<Object> edit(@PathVariable String id,
                                     @Valid @RequestBody OrderRequestDto request) {
        return BaseResponse.ok("Order has been saved", orderService.save(id, request));
    }

    @GetMapping("list")
    public BaseResponse<Object> list(Pageable pageable) {
        return BaseResponse.ok("Order found", orderService.list(pageable));
    }

    @GetMapping("get/{id}")
    public BaseResponse<Object> findById(@PathVariable String id) {
        return BaseResponse.ok("Order found", orderService.get(id));
    }

    @GetMapping("delete/{id}")
    public BaseResponse<Object> delete(@PathVariable String id) {
        orderService.delete(id);
        return BaseResponse.ok("Order has been deleted");
    }

}
