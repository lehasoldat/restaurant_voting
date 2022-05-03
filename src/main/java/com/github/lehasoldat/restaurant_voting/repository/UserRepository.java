package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
