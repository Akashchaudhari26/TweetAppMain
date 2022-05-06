package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Tweet;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {
	@Aggregation(pipeline = { "{'$sort': {'lastModifiedDate': -1}}" })
	public List<Tweet> findAll();

	@Aggregation(pipeline = { "{'$match': { 'loginId' : ?0 }}", "{'$sort': {'lastModifiedDate': -1}}" })
	public List<Tweet> findByLoginId(String loginId);
}
