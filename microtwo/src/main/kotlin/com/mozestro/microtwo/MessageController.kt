package com.mozestro.microtwo

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(private val kafkaTemplate: KafkaTemplate<String, String>) {

    @GetMapping("/send/{message}")
    fun send(@PathVariable message: String) {
        kafkaTemplate.sendDefault(message)
    }
}
