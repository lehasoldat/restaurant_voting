package com.github.lehasoldat.restaurant_voting.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Dish {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 128)
    private String name;

    @Column(name = "price", nullable = false)
    @Range(min = 0, max = 10000)
    @NotNull
    private Long price;
}
