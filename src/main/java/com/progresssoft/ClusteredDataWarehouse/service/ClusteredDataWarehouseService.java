package com.progresssoft.ClusteredDataWarehouse.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.progresssoft.ClusteredDataWarehouse.model.FXDeals;
import com.progresssoft.ClusteredDataWarehouse.repository.FXDealsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {})
public class ClusteredDataWarehouseService {
    @Autowired
    private FXDealsRepository fxDealsRepository;
    private static final Logger log = LogManager.getLogger(ClusteredDataWarehouseService.class);

    public ResponseEntity<String> processData(List<FXDeals> data) {
        int totalProcessed = data.size();
        int successCount = 0;
        int errorCount = 0;
        for (FXDeals fxDeal : data) {
            try {
                log.info("Received:\n" + new GsonBuilder().setPrettyPrinting().create().toJson(fxDeal));
                log.info("Date: " + new Date());
                if (
                        fxDeal.getDealUniqueId() == null ||
                                fxDeal.getFromCurrencyISOCode() == null ||
                                fxDeal.getToCurrencyISOCode() == null ||
                                fxDeal.getDealUniqueId().isEmpty() ||
                                fxDeal.getFromCurrencyISOCode().isEmpty() ||
                                fxDeal.getToCurrencyISOCode().isEmpty() ||
                                fxDeal.getDealAmount().compareTo(BigDecimal.ZERO) < 0 ||
                                fxDeal.getFromCurrencyISOCode().length() != 3 ||
                                fxDeal.getToCurrencyISOCode().length() != 3 ||
                                fxDeal.getDealTimestamp().after(new Date())
                ) {
                    errorCount++;
                    log.error("Missing or malformed input data.");
                } else if (fxDealsRepository.existsById(fxDeal.getDealUniqueId())) {
                    errorCount++;
                    log.error("Deal Unique ID {" + fxDeal.getDealUniqueId() + "} already exists.");
                } else {
                    successCount++;
                    fxDealsRepository.saveAndFlush(fxDeal);
                }

            } catch (Exception e) {
                log.error("Error saving FXDeal: \n", new GsonBuilder().setPrettyPrinting().create().toJson(fxDeal), e);
            }
        }
        String responseMessage = String.format("Processed %d FX Deals. Successfully saved: %d, Errors: %d",
                totalProcessed, successCount, errorCount);

        if (errorCount > 0) {
            log.info(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage).toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        } else {
            log.info(ResponseEntity.ok(responseMessage).toString());
            return ResponseEntity.ok(responseMessage);
        }
    }
}
