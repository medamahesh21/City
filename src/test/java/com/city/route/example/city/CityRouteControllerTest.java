package com.city.route.example.city;


import com.city.route.controller.CityRouteController;
import com.city.route.service.CityRouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CityRouteControllerTest {

    public static final String ORIGIN = "BOSTON";
    public static final String DESTINATION = "NEWARK";

    @InjectMocks
    public CityRouteController api;

    @Mock
    public CityRouteService cityRouteService;


    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testGetPayrollScheduleListNoOrigin() {
        ResponseEntity<String> responseEntity = api.isConnected(
                null, DESTINATION);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetPayrollScheduleListNoDestination() {
        ResponseEntity<String> responseEntity = api.isConnected(
                ORIGIN, null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void testisConnectedPositive() throws IOException {
        String result = "YES";
        when(cityRouteService.isConnected(ORIGIN, DESTINATION)).thenReturn(result);

        ResponseEntity<String> responseEntity = api.isConnected(
                "Boston", "Newark");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}