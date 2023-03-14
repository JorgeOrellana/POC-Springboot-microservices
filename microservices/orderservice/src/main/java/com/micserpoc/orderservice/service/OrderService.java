package com.micserpoc.orderservice.service;

import com.micserpoc.orderservice.dto.OrderLineItemsDTO;
import com.micserpoc.orderservice.dto.OrderRequestDTO;
import com.micserpoc.orderservice.dto.response.InventoryResponseDTO;
import com.micserpoc.orderservice.model.Order;
import com.micserpoc.orderservice.model.OrderLineItems;
import com.micserpoc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService
{
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequestDTO orderRequestDTO)
    {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItems().stream()
                .map(orderLineItemsDTO -> mapToDTO(orderLineItemsDTO))
                .toList();

        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream().map(orderLineItemsParam -> orderLineItemsParam.getSkuCode()).toList();

        // call Inventory Service and place order if product is on stock
        InventoryResponseDTO[] inventoryResponseArray = webClientBuilder.build().get()
            .uri("http://inventory-service/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
            .retrieve()
            .bodyToMono(InventoryResponseDTO[].class)
            .block();

//        boolean testt = Arrays.stream(inventoryResponseArray).toList().stream().allMatch(InventoryResponseDTO::isInStock);
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponseDTO -> inventoryResponseDTO.isInStock());

        if (inventoryResponseArray.length > 0 && allProductsInStock)
        {
            orderRepository.save(order);
        } else
        {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }


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
