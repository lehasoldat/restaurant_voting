package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> getByRestaurant(Restaurant restaurant);
}
