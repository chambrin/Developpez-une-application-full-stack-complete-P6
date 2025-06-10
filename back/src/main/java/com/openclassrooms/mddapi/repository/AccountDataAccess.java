package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDataAccess extends CrudRepository<User, Long> {

    Optional<User> findAccountByEmail(String emailAddress);
    Optional<User> findAccountByUsername(String userName);
    Optional<User> findAccountById(Long userId);
}