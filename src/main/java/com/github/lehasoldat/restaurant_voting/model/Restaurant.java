package com.github.lehasoldat.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    @Size(max = 128)
    private String name;

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Menu> menus;

    public Restaurant(Integer id, String name, Set<Menu> menus) {
        super(id);
        this.name = name;
        this.menus = menus;
    }

    @Override
    public String toString() {
        return name;
    }
}
