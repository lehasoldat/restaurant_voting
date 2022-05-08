package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "INSERT INTO VOTES(VOTING_DATE, VOTING_TIME, RESTAURANT_ID, USER_ID)" +
                    "VALUES (current_date, :votingTime, :restaurantId, :userId)")
    int save(int userId, int restaurantId, LocalTime votingTime);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE votes v SET v.restaurant.id = :restaurantId, v.votingTime = :votingTime WHERE v.id = :voteId")
    int update(int voteId, int restaurantId, LocalTime votingTime);

    Optional<Vote> findByVotingDateAndUser_Id(LocalDate votingDate, int userId);
    List<Vote> findAllByVotingDate(LocalDate votingDate);
}
