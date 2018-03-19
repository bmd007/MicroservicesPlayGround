package ir.tiroon.microservices.personcommand.configiration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class kafkaConfig {

    @Value('${kafka.bootstrap-servers}')
    private String bootstrapServers


    @Bean
    KafkaTemplate kafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>()
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)

        new KafkaTemplate(
                new DefaultKafkaProducerFactory<>(configProps)
        )
    }

}
