package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.Menu;
import com.github.lehasoldat.restaurant_voting.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import java.util.Set;
import static com.github.lehasoldat.restaurant_voting.web.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String API_URL = AdminMenuController.API_URL + "/";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(TestData.ADMIN_MAIL)
    void getAll() throws Exception {
        String expected = mapper.writeValueAsString(Set.of(MENU_REST_1_YESTERDAY, MENU_REST_1_TODAY));
        perform(MockMvcRequestBuilders.get(API_URL, REST1_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(TestData.USER_MAIL)
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL, REST1_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL, REST1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void findMenuByDate() throws Exception {
        String expected = mapper.writeValueAsString(MENU_REST_1_TODAY);
        perform(MockMvcRequestBuilders.get(API_URL + "by-date?menuDate={menuDate}", REST1_ID, LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void findMenuByDateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "by-date?menuDate={menuDate}", REST1_ID, LocalDate.EPOCH))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL + "{menuId}", REST1_ID, MENU_REST_1_TODAY_ID))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(menuRepository.findById(MENU_REST_1_TODAY_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL + "{menuId}", REST1_ID, NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteWithWrongRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL + "{menuId}", REST2_ID, MENU_REST_1_TODAY_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Menu menuRest1TodayUpdated = getMenuRest1TodayUpdated();
        perform(MockMvcRequestBuilders.put(API_URL + "{menuId}", REST1_ID, MENU_REST_1_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest1TodayUpdated)))
                .andExpect(status().isNoContent());
        Menu actual = menuRepository.getById(MENU_REST_1_TODAY_ID);
        TestData.assertMenuMatch(actual, menuRest1TodayUpdated);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateWithWrongRestaurantId() throws Exception {
        Menu menuRest1TodayUpdated = getMenuRest1TodayUpdated();
        perform(MockMvcRequestBuilders.put(API_URL + "{menuId}", REST2_ID, MENU_REST_1_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest1TodayUpdated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateWithInvalidDish() throws Exception {
        Menu menuRest1TodayUpdated = getMenuRest1TodayUpdated();
        menuRest1TodayUpdated.setDishes(Set.of(DISH_NEW, DISH_INVALID));
        perform(MockMvcRequestBuilders.put(API_URL + "{menuId}", REST1_ID, MENU_REST_1_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest1TodayUpdated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateWithInvalidDate() throws Exception {
        Menu menuRest1TodayUpdated = getMenuRest1TodayUpdated();
        menuRest1TodayUpdated.setMenuDate(null);
        perform(MockMvcRequestBuilders.put(API_URL + "{menuId}", REST1_ID, MENU_REST_1_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest1TodayUpdated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void create() throws Exception {
        Menu menuRest2TodayNew = getMenuRest2TodayNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(API_URL, REST2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest2TodayNew)))
                .andExpect(status().isCreated());
        Menu actual = mapper.readValue(actions.andReturn().getResponse().getContentAsString(), Menu.class);
        int newId = actual.getId();
        menuRest2TodayNew.setId(newId);
        TestData.assertMenuMatch(actual, menuRest2TodayNew);
        actual = menuRepository.getById(newId);
        TestData.assertMenuMatch(actual, menuRest2TodayNew);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithWrongRestaurantId() throws Exception {
        Menu menuRest2TodayNew = getMenuRest2TodayNew();
        perform(MockMvcRequestBuilders.post(API_URL, NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest2TodayNew)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithInvalidDish() throws Exception {
        Menu menuRest2TodayNew = getMenuRest2TodayNew();
        menuRest2TodayNew.setDishes(Set.of(DISH_NEW, DISH_INVALID));
        perform(MockMvcRequestBuilders.post(API_URL, REST2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest2TodayNew)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithInvalidDate() throws Exception {
        Menu menuRest2TodayNew = getMenuRest2TodayNew();
        menuRest2TodayNew.setDishes(Set.of(DISH_NEW, DISH_INVALID));
        perform(MockMvcRequestBuilders.post(API_URL, REST2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuRest2TodayNew)))
                .andExpect(status().isUnprocessableEntity());
    }

}