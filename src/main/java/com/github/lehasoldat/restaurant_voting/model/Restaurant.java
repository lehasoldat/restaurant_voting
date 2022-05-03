package com.github.lehasoldat.restaurant_voting.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@ToString(exclude = {"menus"})
@Entity(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "restaurant")
    private Set<Menu> menus;
}
