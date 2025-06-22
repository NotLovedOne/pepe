package com.mozestro.microne

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Component
@RestController
class MessageListener {

    private val log = LoggerFactory.getLogger(MessageListener::class.java)

    @KafkaListener(
        topics = ["\${app.topic.name}"], 
        groupId = "\${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listen(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        acknowledgment: Acknowledgment
    ) {
        try {
            log.info("Received message from topic: {}, partition: {}, offset: {}", topic, partition, offset)
            log.info("Message content: {}", message)
            
            // Process the message here
            // Add your business logic
            
            // Acknowledge the message
            acknowledgment.acknowledge()
            log.info("Message processed and acknowledged successfully")
            
        } catch (e: Exception) {
            log.error("Error processing message: {}", e.message, e)
            // Don't acknowledge - message will be retried
            throw e
        }
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "microne"))
    }
}
