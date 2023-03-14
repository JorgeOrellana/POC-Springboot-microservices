package com.micserpoc.inventoryservice.controller;

import com.micserpoc.inventoryservice.dto.response.InventoryResponseDTO;
import com.micserpoc.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory")
@RequiredArgsConstructor
public class InventoryController
{
    private final InventoryService inventoryService;

    // Get Request (@PathVariable) has the format http://localhost:8082/inventory/iphone-13,iphone13-red
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public boolean isInStock(@PathVariable("sku-code") String skuCode)
//    {
//        return inventoryService.isInStock(skuCode);
//    }

    // Get Request (@RequestParameter) has the format http://localhost:8082/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDTO> isInStock(@RequestParam("skuCode") List<String> skuCode)
    {
        return inventoryService.isInStock(skuCode);
    }
}
