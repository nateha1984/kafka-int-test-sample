package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class KafkaIntTestApplication()

fun main(args: Array<String>) {
    runApplication<KafkaIntTestApplication>(*args)
}