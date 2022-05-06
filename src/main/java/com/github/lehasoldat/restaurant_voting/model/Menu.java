package com.github.lehasoldat.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"menu_date", "restaurant_id"}))
@Entity(name = "menus")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    LocalDate menuDate;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    Restaurant restaurant;

    @Valid
    @CollectionTable(name = "menu_dishes",
            joinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "name"}))
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Dish> dishes;

    public Menu(Integer id, LocalDate menuDate, Set<Dish> dishes) {
        super(id);
        this.menuDate = menuDate;
        this.dishes = dishes;
    }
}
