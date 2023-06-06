package com.progresssoft.ClusteredDataWarehouse.controller;

import com.progresssoft.ClusteredDataWarehouse.model.FXDeals;
import com.progresssoft.ClusteredDataWarehouse.service.ClusteredDataWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/FXDeals")
public class ClusteredDataWarehouseController {
    private final ClusteredDataWarehouseService service;

    public ClusteredDataWarehouseController(ClusteredDataWarehouseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addDeal(@RequestBody List<FXDeals> deals) {
        return service.processData(deals);
    }
}