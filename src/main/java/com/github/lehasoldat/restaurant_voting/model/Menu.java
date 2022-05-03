package com.github.lehasoldat.restaurant_voting.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "date")
    LocalDate localDate;
    @ManyToOne
    Restaurant restaurant;
    int votes;
    @CollectionTable(name = "menu_dishes",
            joinColumns = @JoinColumn(name = "menu_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Dish> dishes;
}
