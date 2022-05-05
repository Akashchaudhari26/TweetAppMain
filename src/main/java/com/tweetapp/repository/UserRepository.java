package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	@Query(value = "{}", fields = "{loginId : 1}")
	List<String> findName();

	List<User> findByLoginId(String loginId);
	
	List<User> findByEmail(String email);
}
