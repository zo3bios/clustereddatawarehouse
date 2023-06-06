package com.progresssoft.ClusteredDataWarehouse;

import com.progresssoft.ClusteredDataWarehouse.model.FXDeals;
import com.progresssoft.ClusteredDataWarehouse.repository.FXDealsRepository;
import com.progresssoft.ClusteredDataWarehouse.service.ClusteredDataWarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClustereddatawarehouseApplicationTests {

    @Mock
    private FXDealsRepository fxDealsRepository;

    @InjectMocks
    private ClusteredDataWarehouseService clusteredDataWarehouseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidRequest_WhenProcessData_then_returnOk() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("001", "USD", "EUR", new Date(), new BigDecimal("100")));
        testData.add(new FXDeals("002", "GBP", "CAD", new Date(), new BigDecimal("200")));

        when(fxDealsRepository.existsById("001")).thenReturn(false);
        when(fxDealsRepository.existsById("002")).thenReturn(false);

        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Processed 2 FX Deals. Successfully saved: 2, Errors: 0", response.getBody());
        verify(fxDealsRepository, times(2)).saveAndFlush(any(FXDeals.class));
    }

    @Test
    public void givenDealUniqueIdIsNull_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals(null, "USD", "EUR", new Date(), new BigDecimal("100")));
        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 1 FX Deals. Successfully saved: 0, Errors: 1", response.getBody());
    }

    @Test
    public void givenDealUniqueIdIsEmpty_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("", "USD", "EUR", new Date(), new BigDecimal("100")));
        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 1 FX Deals. Successfully saved: 0, Errors: 1", response.getBody());
    }

    @Test
    public void givenFromCurrenyISOCodeLengthLessThanThree_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("001", "US", "EUR", new Date(), new BigDecimal("100")));
        testData.add(new FXDeals("002", "G", "CAD", new Date(), new BigDecimal("200")));

        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 2 FX Deals. Successfully saved: 0, Errors: 2", response.getBody());
    }

    @Test
    public void giventoCurrenyISOCodeLengthLessThanThree_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("001", "USD", "EU", new Date(), new BigDecimal("100")));
        testData.add(new FXDeals("002", "GBP", "C", new Date(), new BigDecimal("200")));

        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 2 FX Deals. Successfully saved: 0, Errors: 2", response.getBody());
    }

    @Test
    public void givenTimestampGreaterThanCurrentTime_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("001", "USD", "EU", Date.from(LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant()), new BigDecimal("100")));
        testData.add(new FXDeals("002", "GBP", "C", Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()), new BigDecimal("-200")));

        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 2 FX Deals. Successfully saved: 0, Errors: 2", response.getBody());
    }

    @Test
    public void givenAmountLessThanZero_WhenProcessData_then_returnBadRequest() {
        List<FXDeals> testData = new ArrayList<>();
        testData.add(new FXDeals("001", "USD", "EU", new Date(), new BigDecimal("-30")));
        testData.add(new FXDeals("002", "GBP", "C", new Date(), new BigDecimal("-20")));

        ResponseEntity<String> response = clusteredDataWarehouseService.processData(testData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Processed 2 FX Deals. Successfully saved: 0, Errors: 2", response.getBody());
    }
}