package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.repository.MenuRepository;
import com.github.lehasoldat.restaurant_voting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import static com.github.lehasoldat.restaurant_voting.util.ValidationUtil.assureIdConsistent;
import static com.github.lehasoldat.restaurant_voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Admin menu controller")
public class AdminMenuController {

    public static final String API_URL = "/api/admin/restaurants/{restaurantId}/menus";

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Operation(summary = "Get all menus for the restaurant")
    @GetMapping()
    List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll with restaurantId = {}", restaurantId);
        restaurantRepository.checkPresent(restaurantId);
        return menuRepository.findAllByRestaurant_Id(restaurantId);
    }

    @Operation(summary = "Get all menus for the restaurant by date (use date in ISO format 'YYYY-MM-DD')")
    @GetMapping("/by-date")
    ResponseEntity<Menu> findMenuByDate(@PathVariable int restaurantId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("findMenuByDate with restaurantId = {}, menuDate = {}", restaurantId, menuDate);
        return ResponseEntity.of(menuRepository.findByRestaurant_IdAndMenuDate(restaurantId, menuDate));
    }

    @Operation(summary = "Delete menu")
    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("delete with restaurantId = {}, menuId = {}", restaurantId, menuId);
        menuRepository.checkBelong(menuId, restaurantId);
        menuRepository.deleteById(menuId);
    }

    @Operation(summary = "Update menu")
    @PutMapping(value = "/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int menuId, @RequestBody @Valid Menu menu) {
        log.info("update with restaurantId = {}, menuId = {}", restaurantId, menuId);
        assureIdConsistent(menu, menuId);
        Menu oldValue = menuRepository.checkBelong(menuId, restaurantId);
        oldValue.setMenuDate(menu.getMenuDate());
        oldValue.setDishes(menu.getDishes());
    }

    @Operation(summary = "Create menu")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurantsWithMenuToday", allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@PathVariable int restaurantId, @RequestBody @Valid Menu menu) {
        log.info("create with restaurantId = {}", restaurantId);
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.checkPresent(restaurantId);
        menu.setRestaurant(restaurant);
        Menu created = menuRepository.save(menu);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

}
