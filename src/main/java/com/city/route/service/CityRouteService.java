package com.city.route.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CityRouteService {

    String isConnected(String origin, String destination) throws IOException;
}
