package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
