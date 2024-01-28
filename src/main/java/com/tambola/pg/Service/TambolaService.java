package com.tambola.pg.Service;

import com.tambola.pg.Entity.Tambola;
import com.tambola.pg.CustomException.TambolaException;
import com.tambola.pg.repository.TambolaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class TambolaService {

    private static final Logger logger = LoggerFactory.getLogger(TambolaService.class);

    @Autowired
    private TambolaRepository tambolaRepository;

    public TambolaService(TambolaRepository tambolaRepository) {
        this.tambolaRepository = tambolaRepository;
    }

    public Map<Long, List<List<Integer>>> generateTambolaSets(int numberOfSets) {
        Map<Long, List<List<Integer>>> tickets = new HashMap<>();

        try {
            for (int i = 0; i < numberOfSets; i++) {
                long setNumber = i + 1;
                List<List<Integer>> setTickets = generateTambolaTickets(setNumber);
                tickets.put(setNumber, setTickets);

                saveTambolaTickets(setNumber, setTickets);

                logger.info("Tambola set {} generated and saved.", setNumber);
            }
        } catch (Exception e) {
            logger.error("Error generating Tambola sets", e);
            throw new TambolaException("Error generating Tambola sets", e);
        }

        return tickets;
    }

    private List<List<Integer>> generateTambolaTickets(long setNumber) {
        List<List<Integer>> tickets = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            List<Integer> ticket = generateTambolaTicketData();
            tickets.add(ticket);
        }

        return tickets;
    }

    private List<Integer> generateTambolaTicketData() {
        List<Integer> ticketData = new ArrayList<>();

        List<Integer> numbers = new ArrayList<>();
        for (int num = 1; num <= 90; num++) {
            numbers.add(num);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < 9; i++) {
            if (i > 0 && i % 2 == 0) {
                int num = new Random().nextInt(5);
                for (int j = 0; j < num; j++) {
                    ticketData.add(numbers.get(i * 10 + j));
                }
            }
        }

        return ticketData;
    }

    private void saveTambolaTickets(long setNumber, List<List<Integer>> setTickets) {
        try {
            for (int ticketNumber = 0; ticketNumber < setTickets.size(); ticketNumber++) {
                List<Integer> ticketData = setTickets.get(ticketNumber);
                for (int col = 0; col < ticketData.size(); col++) {
                    Tambola tambola = new Tambola();
                    tambola.setTicketSet(setNumber);
                    tambola.setTicketNumber((long) (ticketNumber + 1));
                    tambola.setCellValue(ticketData.get(col));

                    tambolaRepository.save(tambola);

                    logger.debug("Tambola ticket saved: set={}, ticket={}, value={}", setNumber, ticketNumber + 1, ticketData.get(col));
                }
            }
        } catch (Exception e) {
            logger.error("Error saving Tambola tickets", e);
            throw new TambolaException("Error saving Tambola tickets", e);
        }
    }

    //pagination included
    public List<Tambola> getTambolaTickets(int page, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Tambola> tambolaPage = tambolaRepository.findAll(pageable);
            List<Tambola> tambolaList = tambolaPage.getContent();

            logger.info("Retrieved {} Tambola tickets from page {} with pageSize {}.", tambolaList.size(), page, pageSize);

            return tambolaList;
        } catch (Exception e) {
            logger.error("Error retrieving Tambola tickets", e);
            throw new TambolaException("Error retrieving Tambola tickets", e);
        }
    }
}
