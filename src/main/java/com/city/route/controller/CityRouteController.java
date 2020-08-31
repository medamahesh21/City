package com.city.route.controller;

import com.city.route.service.CityRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class CityRouteController {

    @Autowired
    private CityRouteService cityRouteService;
    
    
    @GetMapping("/connected")
    public ResponseEntity<String> isConnected(@Valid @RequestParam(value = "origin", required=true) String origin,

                                              @Valid @RequestParam(value = "destination", required = true) String destination){
        try {
            String result = cityRouteService.isConnected(origin, destination);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error Occurred",HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }

}
