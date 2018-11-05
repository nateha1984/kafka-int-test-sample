package com.example

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CustomListener(val service: SimpleService) {

    @KafkaListener(topics = ["my-topic"])
    fun receive(@Payload message: String) {
        service.handleMessage(message)
    }
}