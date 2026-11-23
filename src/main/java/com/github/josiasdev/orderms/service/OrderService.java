package com.github.josiasdev.orderms.service;

import com.github.josiasdev.orderms.controller.dto.CustomerOrdersResponse;
import com.github.josiasdev.orderms.controller.dto.OrderResponse;
import com.github.josiasdev.orderms.controller.dto.OrderSummary;
import com.github.josiasdev.orderms.controller.dto.OrdersPageResponse;
import com.github.josiasdev.orderms.entity.OrderEntity;
import com.github.josiasdev.orderms.entity.OrderItem;
import com.github.josiasdev.orderms.listener.dto.OrderCreatedEvent;
import com.github.josiasdev.orderms.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {
        var entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);
    }

    public CustomerOrdersResponse findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        Page<OrderResponse> page = orderRepository.findAllByCustomerId(customerId, pageRequest);
        BigDecimal totalOnOrders = getTotalOnOrders(customerId);

        return new CustomerOrdersResponse(
                new OrderSummary(totalOnOrders, page.getTotalElements()),
                new OrdersPageResponse<>(
                        page.getContent(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages()
                )
        );
    }

    private BigDecimal getTotalOnOrders(Long customerId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("totalOnOrders")
        );

        Document result = mongoTemplate
                .aggregate(aggregation, OrderEntity.class, Document.class)
                .getUniqueMappedResult();

        if (result == null || result.get("totalOnOrders") == null) {
            return BigDecimal.ZERO;
        }

        Object total = result.get("totalOnOrders");
        if (total instanceof BigDecimal value) {
            return value;
        }
        if (total instanceof org.bson.types.Decimal128 value) {
            return value.bigDecimalValue();
        }
        return new BigDecimal(total.toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
