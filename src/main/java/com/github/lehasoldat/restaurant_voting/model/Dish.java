package com.github.lehasoldat.restaurant_voting.model;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Dish {
    private String name;
    private Long price;
}
