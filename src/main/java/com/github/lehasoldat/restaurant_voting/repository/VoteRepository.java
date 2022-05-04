package com.github.lehasoldat.restaurant_voting.repository;

import com.github.lehasoldat.restaurant_voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "INSERT INTO VOTES(VOTING_DATE, RESTAURANT_ID, USER_ID)" +
                    "VALUES (:votingDate, :restaurantId, :userId)")
    void save(LocalDate votingDate, int restaurantId, int userId);

    @Modifying
    @Transactional
    @Query("UPDATE votes v SET v.restaurant.id = :restaurantId WHERE v.id = :voteId")
    void update(int voteId, int restaurantId);

    Optional<Vote> findByVotingDateAndUser_Id(LocalDate localDate, int userId);

    List<Vote> findAllByVotingDate(LocalDate localDate);
}
