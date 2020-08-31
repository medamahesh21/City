package com.city.route.example.city;

import com.city.route.controller.CityRouteController;
import com.city.route.repositoryImpl.CityRouteRepoImpl;
import com.city.route.service.CityRouteService;
import com.city.route.serviceImpl.CityRouteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CityRouteServiceTest {

    public static final String ORIGIN = "BOSTON";
    public static final String DESTINATION = "NEWARK";

    @InjectMocks
    public CityRouteServiceImpl cityRouteService;

    @Mock
    public CityRouteRepoImpl cityRouteRepoImpl;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void testisConnectedPositive() throws IOException {

        Map<String, String> map = new HashMap<String, String>();
        map.put(ORIGIN, DESTINATION);
        String value = cityRouteService.isConnected(
                "Boston", "Newark");
        assertEquals("NO", value);
    }
}