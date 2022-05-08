package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    default Restaurant checkPresent(int restaurantId) {
        return findById(restaurantId).orElseThrow(() -> {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "There is no restaurant with id = " + restaurantId);
        });
    }
}
