package com.arijit.kafka.producer;

import com.arijit.kafka.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {


    @Autowired
    private final KafkaTemplate<Integer, Customer> kafkaTemplate;

    @Autowired
    private final ObjectMapper mapper;

    @Value("${topic.name}")
    private String topic;

    public void send(Customer customer) throws JsonProcessingException {

        Integer key = customer.getCustomerId();
        //String value = mapper.writeValueAsString(customer);

        ListenableFuture<SendResult<Integer, Customer>> result =
                kafkaTemplate.send(new ProducerRecord(topic, null, key, customer, null));

        result.addCallback(new ListenableFutureCallback<SendResult<Integer, Customer>>() {
            @Override
            public void onFailure(Throwable ex) {
                 log.error(ex.getMessage());
                 //best approach: save in database and retry later to produce again
            }

            @Override
            public void onSuccess(SendResult<Integer, Customer> result) {
                log.info("Result Details - "+result.getRecordMetadata());

            }
        });
    }

}
