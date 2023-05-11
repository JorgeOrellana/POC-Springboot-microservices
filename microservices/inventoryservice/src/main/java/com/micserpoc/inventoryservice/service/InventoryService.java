package com.micserpoc.inventoryservice.service;

import com.micserpoc.inventoryservice.dto.response.InventoryResponseDTO;
import com.micserpoc.inventoryservice.model.Inventory;
import com.micserpoc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService
{
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponseDTO> isInStock(List<String> skuCode)
    {
        log.info("Wait Started");
        Thread.sleep(10000);
        log.info("Wait Ended");

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
