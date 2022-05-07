package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.lehasoldat.restaurant_voting.util.ValidationUtil.assureIdConsistent;
import static com.github.lehasoldat.restaurant_voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Admin restaurant controller")
public class AdminRestaurantController {

    public static final String API_URL = "/api/admin/restaurants";

    @Autowired
    RestaurantRepository restaurantRepository;

    @Operation(summary = "Get all restaurants")
    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Operation(summary = "Get restaurant")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("create with id = {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @Operation(summary = "Delete restaurant")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete with id = {}", id);
        restaurantRepository.deleteById(id);
    }

    @Operation(summary = "Update restaurant")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    public void update(@PathVariable int id, @RequestBody @Valid Restaurant restaurant) {
        log.info("update with id = {}", id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Create restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody @Valid Restaurant restaurant) {
        log.info("createWithLocation");
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

}
