package com.tweetapp.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
public class TweetConsumerConfig {
	@Autowired
	KafkaProperties kafkaProperties;

	@Bean
	ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory.getIfAvailable(
				() -> new DefaultKafkaConsumerFactory<>(this.kafkaProperties.buildConsumerProperties())));
		factory.setConcurrency(3);
		factory.setCommonErrorHandler(errorhandler());
		return factory;
	}

	public DefaultErrorHandler errorhandler() {
		// exceptionsToIgnoreList
		List<Class<IllegalArgumentException>> exceptionsToIgnoreList = Arrays.asList(IllegalArgumentException.class);

		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2);

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(fixedBackOff);

		exceptionsToIgnoreList.forEach(errorHandler::addNotRetryableExceptions);

		errorHandler.setRetryListeners((record, ex, deleveryAttemps) -> {
			log.info("Failed Record in Retry Listener, Exception: {} , deleveryAttemps: {} ", ex.getMessage(),
					deleveryAttemps);
		});
		return errorHandler;

	}

}
