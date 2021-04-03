package com.notificationservice.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.domain.event.NewRateCreatedEvent;
import com.notificationservice.domain.event.UserUpdatedEvent;
import com.notificationservice.domain.Alert;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  private String kafkaServer = "localhost:9092";
  private String groupID = "app.2";

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);

    return props;
  }

  @Bean
  public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<Long, Alert> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<Long, Alert> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(),  new LongDeserializer(),
        new JsonDeserializer<>(Alert.class,false));
  }

  @Bean
  public ConsumerFactory<Long, UserUpdatedEvent> userUpdatedEventConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new LongDeserializer(),
        new JsonDeserializer<>(UserUpdatedEvent.class, false));
  }

  @Bean
  public KafkaListenerContainerFactory<?>userUpdatedEventContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<Long, UserUpdatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(userUpdatedEventConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<Long, String> deleteEventConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new LongDeserializer(),
        new StringDeserializer());
  }

  @Bean
  public KafkaListenerContainerFactory<?> deleteEventContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<Long, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(deleteEventConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<Long, List<NewRateCreatedEvent>> rateEventConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new LongDeserializer(),
        kafkaDeserializer());
  }

  @Bean
  public KafkaListenerContainerFactory<?> rateEventContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<Long, List<NewRateCreatedEvent>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(rateEventConsumerFactory());
    return factory;
  }

  protected JsonDeserializer<List<NewRateCreatedEvent>> kafkaDeserializer() {
    ObjectMapper om = new ObjectMapper();
    JavaType type = om.getTypeFactory().constructCollectionType(List.class, NewRateCreatedEvent.class);
    return new JsonDeserializer<>(type, om, false);
  }
}
