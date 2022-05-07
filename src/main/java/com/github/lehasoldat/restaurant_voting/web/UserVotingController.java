package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.model.Vote;
import com.github.lehasoldat.restaurant_voting.repository.MenuRepository;
import com.github.lehasoldat.restaurant_voting.repository.RestaurantRepository;
import com.github.lehasoldat.restaurant_voting.repository.VoteRepository;
import com.github.lehasoldat.restaurant_voting.util.DateTimeUtil;
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
public class UserVotingController {

    public static final String API_URL = "/api";
    public static final Map<String, String> SUCCESS_UPDATED = Map.of("votingResult", "Vote updated");
    public static final Map<String, String> SUCCESS_SAVED = Map.of("votingResult", "Vote saved");

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/restaurants")
    @Cacheable(value = "restaurantsWithMenuToday", keyGenerator = "currentDateKeyGenerator")
    public List<Restaurant> getRestaurantsWithMenuToday() {
        log.info("getRestaurantsWithMenuToday");
        List<Menu> menus = menuRepository.findAllByMenuDate(LocalDate.now());
        return menus.stream().map(Menu::getRestaurant).toList();
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Menu> getRestaurantMenuToday(@PathVariable int restaurantId) {
        log.info("getRestaurantMenuToday with restaurantId = {}", restaurantId);
        return ResponseEntity.of(menuRepository.findByRestaurant_IdAndMenuDate(restaurantId, LocalDate.now()));
    }

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

    @GetMapping("/votes")
    public Map<Restaurant, Integer> getAllVotesToday() {
        log.info("getAllVotesToday");
        List<Vote> votes = voteRepository.findAllByVotingDate(LocalDate.now());
        return votes.stream().collect(Collectors.toMap(Vote::getRestaurant, vote -> 1, Integer::sum));
    }
}
