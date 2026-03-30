package com.github.josiasdev.orderms.controller.dto;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {
}
