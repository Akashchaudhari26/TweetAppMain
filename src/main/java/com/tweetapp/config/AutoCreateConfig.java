package com.tweetapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(value = "spring.kafka.enable", havingValue = "true", matchIfMissing = true)
public class AutoCreateConfig {
	@Bean
	public NewTopic newTweet() {
		return TopicBuilder.name("new-tweet")
                .partitions(2)
                .replicas(2)
                .build();
	}
}
