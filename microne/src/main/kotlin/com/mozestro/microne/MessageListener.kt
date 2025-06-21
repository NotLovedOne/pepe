package com.mozestro.microne

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MessageListener {

    private val log = LoggerFactory.getLogger(MessageListener::class.java)

    @KafkaListener(topics = ["\${app.topic.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(message: String) {
        log.info("Received: {}", message)
    }
}
