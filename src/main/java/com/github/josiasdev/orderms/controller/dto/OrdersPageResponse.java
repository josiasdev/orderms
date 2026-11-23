package com.github.josiasdev.orderms.controller.dto;

import java.util.List;

public record OrdersPageResponse<T>(List<T> content,
                                    Integer page,
                                    Integer pageSize,
                                    Long totalElements,
                                    Integer totalPages) {
}
