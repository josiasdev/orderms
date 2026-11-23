package com.github.josiasdev.orderms.controller;

import com.github.josiasdev.orderms.controller.dto.CustomerOrdersResponse;
import com.github.josiasdev.orderms.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<CustomerOrdersResponse> listOrders(@PathVariable("customerId") Long customerId,
                                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var response = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(response);
    }
}
