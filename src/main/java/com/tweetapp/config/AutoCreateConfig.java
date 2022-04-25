package com.tweetapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {
	@Bean
	public NewTopic newTweet() {
		return TopicBuilder.name("new-tweet")
                .partitions(2)
                .replicas(2)
                .build();
	}
}
