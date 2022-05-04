package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByMenuDate(LocalDate menuDate);
    Optional<Menu> findByRestaurant_IdAndMenuDate(int id, LocalDate menuDate);
}
