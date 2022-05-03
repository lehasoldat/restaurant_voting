package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Optional<Restaurant> getByName(String name);
}
