package com.github.josiasdev.orderms.controller.dto;

public record CustomerOrdersResponse(OrderSummary summary, OrdersPageResponse<OrderResponse> orders) {
}
