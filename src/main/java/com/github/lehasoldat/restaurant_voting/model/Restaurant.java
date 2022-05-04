package com.github.lehasoldat.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "restaurants")
@Getter
@Setter
@ToString(exclude = {"menus"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private Set<Menu> menus;
}
