package com.mozestro.microtwo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaProducerConfig {

    @Bean
    fun topic(@Value("\${app.topic.name}") name: String) =
        TopicBuilder.name(name).build()
}
