package com.github.josiasdev.orderms.controller;

import com.github.josiasdev.orderms.controller.dto.ApiResponse;
import com.github.josiasdev.orderms.controller.dto.OrderResponse;
import com.github.josiasdev.orderms.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        return ResponseEntity.ok(null);
    }
}
