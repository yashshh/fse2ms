package com.auth.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.auth.model.UserData;

@Repository
public interface UserDAO extends MongoRepository<UserData, String> {

}
