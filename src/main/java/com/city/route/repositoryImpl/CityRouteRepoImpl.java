package com.city.route.repositoryImpl;

import com.city.route.repository.CityRouteRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
@Repository
public class CityRouteRepoImpl implements CityRouteRepository {


    @Override
    public Map<String, String> getCities(String fileName) throws IOException {

        Map<String,String> cities = new HashMap<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII))
        {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                cities.put(attributes[0].trim(),attributes[1].trim());
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }
        return cities;
    }
}
