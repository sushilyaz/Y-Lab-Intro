package org.example.in.controllers;

import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.dto.DateDTO;
import org.example.service.CounterReadingService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CounterReadingController {
    private CounterReadingService counterReadingService;

    private UserUtils userUtils;

    @Autowired
    public CounterReadingController(CounterReadingService counterReadingService, UserUtils userUtils) {
        this.counterReadingService = counterReadingService;
        this.userUtils = userUtils;
    }

    @PostMapping("/put-counter-reading")
    public ResponseEntity<String> create(@RequestBody List<CounterReadingCreateDTO> createDTO) {
        List<CounterReadingCreateDTO> validDTO = counterReadingService.validationCounter(userUtils.getCurrentUser(), createDTO);
        if (validDTO.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Data no valid");
        }
        List<CounterReadingDTO> listResult = counterReadingService.submitCounterReading(userUtils.getCurrentUser(), validDTO);
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Counter Reading failed submit");
        } else {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Counter Reading submit successfully");
        }
    }

    @GetMapping("/latest-data-for-current-user")
    public ResponseEntity<List<CounterReadingDTO>> getLatestData() {
        List<CounterReadingDTO> listResult = counterReadingService.getLastUserInfo(userUtils.getCurrentUser());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }

    }


    @GetMapping("/get-data-for-month")
    public ResponseEntity<List<CounterReadingDTO>> getDataForMonth(@RequestBody DateDTO dateDTO) {
        List<CounterReadingDTO> listResult = counterReadingService.getUserInfoForMonth(userUtils.getCurrentUser(), dateDTO.getDate());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }

    @GetMapping("/get-all-data")
    public ResponseEntity<List<CounterReadingDTO>> getAllData() {
        List<CounterReadingDTO> listResult = counterReadingService.getCRByUser(userUtils.getCurrentUser());
        if (listResult.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(listResult);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listResult);
        }
    }
}
