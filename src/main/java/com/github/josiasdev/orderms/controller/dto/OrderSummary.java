package com.github.josiasdev.orderms.controller.dto;

import java.math.BigDecimal;

public record OrderSummary(BigDecimal totalOnOrders, Long totalQuantityOnOrders) {
}
