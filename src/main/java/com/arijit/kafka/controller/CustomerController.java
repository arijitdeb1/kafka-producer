package com.arijit.kafka.controller;

import com.arijit.kafka.model.Customer;
import com.arijit.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    @Autowired
    private final KafkaProducer producer;

    @PostMapping("/customer")
    public ResponseEntity<Customer> persistCustomer(@RequestBody Customer customer) throws JsonProcessingException {

        log.info("Producing message for "+customer.getCustomerId());
        producer.send(customer);
        log.info("Produced message for "+customer.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
}
