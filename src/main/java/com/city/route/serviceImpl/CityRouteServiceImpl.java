package com.city.route.serviceImpl;

import com.city.route.repositoryImpl.CityRouteRepoImpl;
import com.city.route.service.CityRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CityRouteServiceImpl implements CityRouteService {

    @Override
    public String isConnected(String origin, String destination) throws NullPointerException, IOException {
        try {
            Map<String, String> cities = getCities("city.txt");
            return cities.get(origin).equalsIgnoreCase(destination) ? "YES" : "NO";
        } catch (NullPointerException | IOException ex) {
            throw ex;
        }
    }

    public Map<String, String> getCities(String fileName) throws IOException {

        Map<String, String> cities = new HashMap<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                cities.put(attributes[0].trim(), attributes[1].trim());
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }
        return cities;
    }

}
