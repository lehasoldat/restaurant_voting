package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import com.github.lehasoldat.restaurant_voting.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByMenuDate(LocalDate menuDate);

    Optional<Menu> findByRestaurant_IdAndMenuDate(int restaurantId, LocalDate menuDate);

    Optional<Menu> findByIdAndRestaurant_Id(int id, int restaurantId);

    List<Menu> findAllByRestaurant_Id(int restaurantId);

    default Menu checkBelong(int menuId, int restaurantId) {
        return findByIdAndRestaurant_Id(menuId, restaurantId).orElseThrow(() -> {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "Menu with id = "
                    + menuId + " does not belong to restaurant with id = " + restaurantId);
        });
    }

}
