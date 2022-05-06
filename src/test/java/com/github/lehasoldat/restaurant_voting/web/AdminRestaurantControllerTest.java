package com.github.lehasoldat.restaurant_voting.web;
import com.github.lehasoldat.restaurant_voting.model.Restaurant;
import com.github.lehasoldat.restaurant_voting.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Set;
import static com.github.lehasoldat.restaurant_voting.web.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String API_URL = AdminRestaurantController.API_URL + "/";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(TestData.ADMIN_MAIL)
    void getAll() throws Exception {
        String expected = mapper.writeValueAsString(Set.of(REST_1, REST_2));
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(TestData.USER_MAIL)
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(TestData.ADMIN_MAIL)
    void get() throws Exception {
        String expected = mapper.writeValueAsString(REST_1);
        perform(MockMvcRequestBuilders.get(API_URL + REST1_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + NOT_FOUND_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL + REST1_ID))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(restaurantRepository.findById(REST1_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL + NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Restaurant rest1Updated = getRest1Updated();
        perform(MockMvcRequestBuilders.put(API_URL + REST1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rest1Updated)))
                .andExpect(status().isNoContent());
        Restaurant actual = restaurantRepository.getById(REST1_ID);
        TestData.assertMatch(actual, rest1Updated);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateWithNotConsistentId() throws Exception {
        Restaurant rest1Updated = getRest1Updated();
        perform(MockMvcRequestBuilders.put(API_URL + REST2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rest1Updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void create() throws Exception {
        Restaurant rest3New = getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rest3New)))
                .andExpect(status().isCreated());
        Restaurant actual = mapper.readValue(actions.andReturn().getResponse().getContentAsString(), Restaurant.class);
        int newId = actual.getId();
        rest3New.setId(newId);
        TestData.assertMatch(actual, rest3New);
        actual = restaurantRepository.getById(newId);
        TestData.assertMatch(actual, rest3New);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createNotNew() throws Exception {
        Restaurant rest3New = getNew();
        rest3New.setId(2);
        perform(MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rest3New)))
                .andExpect(status().isUnprocessableEntity());
    }
}