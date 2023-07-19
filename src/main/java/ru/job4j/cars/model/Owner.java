package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "owners")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Owner {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
