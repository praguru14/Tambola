package com.tambola.pg.Controller;

import com.tambola.pg.CustomException.TambolaException;
import com.tambola.pg.Entity.Tambola;
import com.tambola.pg.Service.TambolaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tambola")
public class TambolaController {

    private static final Logger logger = LoggerFactory.getLogger(TambolaController.class);

    @Autowired
    private TambolaService tambolaService;

    @PostMapping("/generate-sets")
    public ResponseEntity<Map<Long, List<List<Integer>>>> generateTambolaSets(@RequestParam int numberOfSets) {
        try {
            Map<Long, List<List<Integer>>> tickets = tambolaService.generateTambolaSets(numberOfSets);
            logger.info("Generated Tambola sets successfully. Number of sets: {}", numberOfSets);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (TambolaException e) {
            logger.error("Error generating Tambola sets", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-tickets")
    public ResponseEntity<List<Tambola>> getTambolaTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<Tambola> tambolaTickets = tambolaService.getTambolaTickets(page, pageSize);
            logger.info("Retrieved Tambola tickets successfully. Page: {}, PageSize: {}", page, pageSize);
            return new ResponseEntity<>(tambolaTickets, HttpStatus.OK);
        } catch (TambolaException e) {
            logger.error("Error retrieving Tambola tickets", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
