package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TestData {
    //IDs
    public static final int START_ID = 1;
    public static final int NOT_FOUND_ID = 100;
    public static final int USER_ID = START_ID;
    public static final int ADMIN_ID = START_ID + 1;
    public static final int MENU_REST_1_YESTERDAY_ID = START_ID;
    public static final int MENU_REST_2_YESTERDAY_ID = START_ID + 1;
    public static final int MENU_REST_1_TODAY_ID = START_ID + 2;
    public static final int MENU_REST_2_TODAY_ID = START_ID + 3;
    public static final int REST1_ID = START_ID;
    public static final int REST2_ID = START_ID + 1;
    public static final int REST3_ID = START_ID + 2;
    // users
    public static final String USER_MAIL = "user@mail.ru";
    public static final String ADMIN_MAIL = "admin@mail.ru";
    // dishes
    public static final Dish DISH_1_REST_1_YESTERDAY = new Dish("dish1_rest1_yesterday", 100);
    public static final Dish DISH_2_REST_1_YESTERDAY = new Dish("dish2_rest1_yesterday", 50);
    public static final Dish DISH_1_REST_2_YESTERDAY = new Dish("dish1_rest2_yesterday", 70);
    public static final Dish DISH_2_REST_2_YESTERDAY = new Dish("dish2_rest2_yesterday", 30);
    public static final Dish DISH_1_REST_1_TODAY = new Dish("dish1_rest1_today", 150);
    public static final Dish DISH_2_REST_1_TODAY = new Dish("dish2_rest1_today", 75);
    public static final Dish DISH_1_REST_2_TODAY = new Dish("dish1_rest2_today", 120);
    public static final Dish DISH_2_REST_2_TODAY = new Dish("dish2_rest2_today", 90);
    public static final Dish DISH_NEW = new Dish("new_dish", 100);
    public static final Dish DISH_INVALID = new Dish("", -1);
    // menus
    public static final Menu MENU_REST_1_YESTERDAY = new Menu(MENU_REST_1_YESTERDAY_ID, LocalDate.now().minusDays(1), Set.of(DISH_1_REST_1_YESTERDAY, DISH_2_REST_1_YESTERDAY));
    public static final Menu MENU_REST_2_YESTERDAY = new Menu(MENU_REST_2_YESTERDAY_ID, LocalDate.now().minusDays(1), Set.of(DISH_1_REST_2_YESTERDAY, DISH_2_REST_2_YESTERDAY));
    public static final Menu MENU_REST_1_TODAY = new Menu(MENU_REST_1_TODAY_ID, LocalDate.now(), Set.of(DISH_1_REST_1_TODAY, DISH_2_REST_1_TODAY));
    public static final Menu MENU_REST_2_TODAY = new Menu(MENU_REST_2_TODAY_ID, LocalDate.now(), Set.of(DISH_1_REST_2_TODAY, DISH_2_REST_2_TODAY));

    public static Menu getMenuRest1TodayUpdated() {
        return new Menu(MENU_REST_1_TODAY_ID, LocalDate.now(), Set.of(DISH_1_REST_1_TODAY, DISH_NEW));
    }

    public static Menu getMenuRest3TodayNew() {
        return new Menu(null, LocalDate.now(), Set.of(DISH_NEW));
    }

    // restaurants
    public static final Restaurant REST_1 = new Restaurant(REST1_ID, "rest1", Set.of(MENU_REST_1_YESTERDAY, MENU_REST_1_TODAY));
    public static final Restaurant REST_2 = new Restaurant(REST2_ID, "rest2", Set.of(MENU_REST_2_YESTERDAY, MENU_REST_2_TODAY));
    public static final Restaurant REST_3 = new Restaurant(REST3_ID, "rest3", Collections.emptySet());

    public static Restaurant getRest1Updated() {
        return new Restaurant(REST1_ID, "rest1_updated", null);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "rest3_new", null);
    }

    // matchers
    public static <T> void assertMatch(T actual, T expected, String... fieldNamesToIgnore) {
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(fieldNamesToIgnore)
                .isEqualTo(expected);
    }

    public static void assertMenuMatch(Menu actual, Menu expected) {
        assertMatch(actual, expected, "restaurant");
    }

    public static void assertRestaurantMatch(Restaurant actual, Restaurant expected) {
        assertMatch(actual, expected, "menus");
    }
}
