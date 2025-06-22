package com.mozestro.microtwo

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${app.topic.name}")
    private lateinit var topicName: String

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mapOf(
            "bootstrap.servers" to bootstrapServers
        )
        return KafkaAdmin(configs)
    }

    @Bean
    fun topic(): NewTopic {
        return NewTopic(topicName, 1, 1)
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configs = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 3,
            ProducerConfig.LINGER_MS_CONFIG to 1,
            ProducerConfig.BUFFER_MEMORY_CONFIG to 33554432
        )
        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val template = KafkaTemplate(producerFactory())
        template.defaultTopic = topicName
        return template
    }

}
