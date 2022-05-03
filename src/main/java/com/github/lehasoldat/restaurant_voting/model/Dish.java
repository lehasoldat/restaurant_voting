package com.github.lehasoldat.restaurant_voting.model;

import lombok.Data;

import javax.persistence.*;

@Embeddable
@Data
public class Dish {
    String name;
    double price;
}
