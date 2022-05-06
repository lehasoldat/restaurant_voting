package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.repository.VoteRepository;
import com.github.lehasoldat.restaurant_voting.util.DateTimeUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import static com.github.lehasoldat.restaurant_voting.web.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVotingControllerTest extends AbstractControllerTest {

    private static final String API_URL = UserVotingController.API_URL + "/";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER_MAIL)
    void getRestaurantsWithMenuToday() throws Exception {
        String expected = mapper.writeValueAsString(Set.of(REST_1));
        perform(MockMvcRequestBuilders.get(API_URL + "restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getRestaurantMenuToday() throws Exception {
        String expected = mapper.writeValueAsString(MENU_REST_1_TODAY);
        perform(MockMvcRequestBuilders.get(API_URL + "/restaurants/{restaurantId}/menus", REST1_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getRestaurantMenuTodayWithWrongRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/restaurants/{restaurantId}/menus", NOT_FOUND_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void saveNewVoteForRestaurant() throws Exception {
        try (MockedStatic<DateTimeUtil> mock = mockDateTimeUtil(LocalTime.MIN)) {
            String expectedResponseBody = mapper.writeValueAsString(UserVotingController.SUCCESS_SAVED);
            perform(MockMvcRequestBuilders.post(API_URL + "/restaurants/{restaurantId}/vote", REST2_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponseBody));
            int actual = voteRepository.findByVotingDateAndUser_Id(LocalDate.now(), ADMIN_ID).get().getRestaurant().getId();
            Assertions.assertEquals(REST2_ID, actual);
        }
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateVoteForRestaurant() throws Exception {
        try (MockedStatic<DateTimeUtil> mock = mockDateTimeUtil(LocalTime.MIN)) {
            String expected = mapper.writeValueAsString(UserVotingController.SUCCESS_UPDATED);
            perform(MockMvcRequestBuilders.post(API_URL + "/restaurants/{restaurantId}/vote", REST2_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expected));
            int actual = voteRepository.findByVotingDateAndUser_Id(LocalDate.now(), USER_ID).get().getRestaurant().getId();
            Assertions.assertEquals(REST2_ID, actual);
        }
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void saveVoteForRestaurantAfterStopTime() throws Exception {
        try (MockedStatic<DateTimeUtil> mock = mockDateTimeUtil(LocalTime.of(12, 0))) {
            perform(MockMvcRequestBuilders.post(API_URL + "/restaurants/{restaurantId}/vote", REST2_ID))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateVoteForRestaurantAfterStopTime() throws Exception {
        try (MockedStatic<DateTimeUtil> mock = mockDateTimeUtil(LocalTime.of(12, 0))) {
            perform(MockMvcRequestBuilders.post(API_URL + "/restaurants/{restaurantId}/vote", REST2_ID))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateOrSaveNewVoteForRestaurantWithWrongRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/restaurants/{restaurantId}/vote", NOT_FOUND_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    private MockedStatic<DateTimeUtil> mockDateTimeUtil(LocalTime newLocalTime) {
        MockedStatic<DateTimeUtil> dateTimeUtilMockedStatic = Mockito.mockStatic(DateTimeUtil.class);
        dateTimeUtilMockedStatic.when(DateTimeUtil::getCurrentTime).thenReturn(newLocalTime);
        return dateTimeUtilMockedStatic;
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getAllVotesToday() throws Exception {
        String expected = mapper.writeValueAsString(Map.of(REST_1, 1));
        perform(MockMvcRequestBuilders.get(API_URL + "votes"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

}


