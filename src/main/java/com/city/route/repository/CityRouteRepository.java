package com.city.route.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;
@Repository
public interface CityRouteRepository {
    Map<String, String> getCities(String s) throws IOException;
}
