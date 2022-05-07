package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.model.Vote;
import com.github.lehasoldat.restaurant_voting.repository.MenuRepository;
import com.github.lehasoldat.restaurant_voting.repository.RestaurantRepository;
import com.github.lehasoldat.restaurant_voting.repository.VoteRepository;
import com.github.lehasoldat.restaurant_voting.util.DateTimeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = UserVotingController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "User voting controller")
public class UserVotingController {

    public static final String API_URL = "/api";
    public static final Map<String, String> SUCCESS_UPDATED = Map.of("votingResult", "Vote updated");
    public static final Map<String, String> SUCCESS_SAVED = Map.of("votingResult", "Vote saved");

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Operation(summary = "Get all restaurants with menu today")
    @GetMapping("/restaurants")
    @Cacheable(value = "restaurantsWithMenuToday", keyGenerator = "currentDateKeyGenerator")
    public List<Restaurant> getRestaurantsWithMenuToday() {
        log.info("getRestaurantsWithMenuToday");
        List<Menu> menus = menuRepository.findAllByMenuDate(LocalDate.now());
        return menus.stream().map(Menu::getRestaurant).toList();
    }

    @Operation(summary = "Get today's menu for the restaurant")
    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Menu> getRestaurantMenuToday(@PathVariable int restaurantId) {
        log.info("getRestaurantMenuToday with restaurantId = {}", restaurantId);
        return ResponseEntity.of(menuRepository.findByRestaurant_IdAndMenuDate(restaurantId, LocalDate.now()));
    }

    @Operation(summary = "Vote for the restaurant. User can save or update his vote only before 11:00")
    @PostMapping("/restaurants/{restaurantId}/vote")
    public Map<String, String> voteForRestaurant(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("voteForRestaurant with restaurantId = {}", restaurantId);
        int userId = authUser.getUser().getId();
        restaurantRepository.checkPresent(restaurantId);
        Optional<Vote> vote = voteRepository.findByVotingDateAndUser_Id(LocalDate.now(), userId);
        LocalTime votingTime = DateTimeUtil.getCurrentTime();
        if (vote.isPresent()) {
            int i = voteRepository.update(vote.get().getId(), restaurantId, votingTime);
            return SUCCESS_UPDATED;
        } else {
            voteRepository.save(userId, restaurantId, votingTime);
            return SUCCESS_SAVED;
        }
    }

    @Operation(summary = "Get all votes today")
    @GetMapping("/votes")
    public Map<Restaurant, Integer> getAllVotesToday() {
        log.info("getAllVotesToday");
        List<Vote> votes = voteRepository.findAllByVotingDate(LocalDate.now());
        return votes.stream().collect(Collectors.toMap(Vote::getRestaurant, vote -> 1, Integer::sum));
    }
}
