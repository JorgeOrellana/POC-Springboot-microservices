package com.micserpoc.orderservice.service;

import com.micserpoc.orderservice.dto.OrderLineItemsDTO;
import com.micserpoc.orderservice.dto.OrderRequestDTO;
import com.micserpoc.orderservice.model.Order;
import com.micserpoc.orderservice.model.OrderLineItems;
import com.micserpoc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService
{
    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequestDTO orderRequestDTO)
    {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItems().stream()
                .map(orderLineItemsDTO -> mapToDTO(orderLineItemsDTO))
                .toList();

        order.setOrderLineItems(orderLineItems);

        orderRepository.save(order);
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO)
    {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
