package com.github.lehasoldat.restaurant_voting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"voting_date", "user_id"}))
@Entity(name = "votes")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "voting_date", nullable = false)
    @NotNull
    private LocalDate votingDate;

    @Column(name = "voting_time", columnDefinition = "TIME NOT NULL CHECK(voting_time < '11:00')")
    private LocalTime votingTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;
}
