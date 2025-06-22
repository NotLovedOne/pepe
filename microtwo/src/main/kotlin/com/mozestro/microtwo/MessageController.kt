package com.mozestro.microtwo

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
class MessageController(private val kafkaTemplate: KafkaTemplate<String, String>) {

    private val log = LoggerFactory.getLogger(MessageController::class.java)

    @GetMapping("/send/{message}")
    fun send(@PathVariable message: String): ResponseEntity<Map<String, String>> {
        return try {
            log.info("Sending message: {}", message)
            
            val future: CompletableFuture<SendResult<String, String>> = kafkaTemplate.sendDefault(message)
            
            future.whenComplete { result, throwable ->
                if (throwable == null) {
                    log.info("Message sent successfully to topic: {}, partition: {}, offset: {}", 
                        result.recordMetadata.topic(),
                        result.recordMetadata.partition(),
                        result.recordMetadata.offset()
                    )
                } else {
                    log.error("Failed to send message: {}", throwable.message, throwable)
                }
            }
            
            ResponseEntity.ok(mapOf(
                "status" to "success",
                "message" to "Message sent to Kafka",
                "content" to message
            ))
            
        } catch (e: Exception) {
            log.error("Error sending message: {}", e.message, e)
            ResponseEntity.internalServerError().body(
                mapOf(
                    "status" to "error",
                    "message" to "Failed to send message",
                    "error" to (e.message ?: "Unknown error")
                )
            )
        }

    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "microtwo"))
    }
}
