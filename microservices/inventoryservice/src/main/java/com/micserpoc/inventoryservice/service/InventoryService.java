package com.micserpoc.inventoryservice.service;

import com.micserpoc.inventoryservice.dto.response.InventoryResponseDTO;
import com.micserpoc.inventoryservice.model.Inventory;
import com.micserpoc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService
{
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> isInStock(List<String> skuCode)
    {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> mapToInventoryResponse(inventory))
                .toList();
    }

    private InventoryResponseDTO mapToInventoryResponse(Inventory inventory)
    {
        return InventoryResponseDTO.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
