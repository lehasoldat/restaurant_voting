package com.github.lehasoldat.restaurant_voting.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "restaurants")
@Getter
@Setter
@ToString(callSuper = true, exclude = {"menus"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "restaurant")
    private Set<Menu> menus;
}
