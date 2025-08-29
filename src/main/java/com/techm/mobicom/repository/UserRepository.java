package com.techm.mobicom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techm.mobicom.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByEmail(String email);
}