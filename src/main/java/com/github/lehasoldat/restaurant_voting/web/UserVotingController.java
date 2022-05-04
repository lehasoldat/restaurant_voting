package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.model.Vote;
import com.github.lehasoldat.restaurant_voting.repository.MenuRepository;
import com.github.lehasoldat.restaurant_voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserVotingController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurantsWithMenuToday() {
        List<Menu> menus = menuRepository.findAllByMenuDate(LocalDate.now());
        return menus.stream().map(Menu::getRestaurant).toList();
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Menu> getRestaurantMenuToday(@PathVariable int restaurantId) {
        return ResponseEntity.of(menuRepository.findByRestaurant_IdAndMenuDate(restaurantId, LocalDate.now()));
    }

    @PostMapping("/restaurants/{restaurantId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voteForRestaurant(@PathVariable int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            throw new AppException(HttpStatus.METHOD_NOT_ALLOWED, "You can not vote after 11:00");
        }
        int userId = authUser.getUser().getId();
        Optional<Vote> vote = voteRepository.findByVotingDateAndUser_Id(LocalDate.now(), userId);
        if (vote.isPresent()) {
            voteRepository.update(vote.get().getId(), restaurantId);
        } else {
            voteRepository.save(LocalDate.now(), restaurantId, userId);
        }
    }

    @GetMapping("/votes")
    public Map<Restaurant, Integer> getAllVotesToday() {
        List<Vote> votes = voteRepository.findAllByVotingDate(LocalDate.now());
        return votes.stream().collect(Collectors.toMap(Vote::getRestaurant, vote -> 1, Integer::sum));
    }
}
