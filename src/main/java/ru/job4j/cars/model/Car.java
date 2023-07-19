package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToMany
    @JoinTable(
            name = "history_owners",
            joinColumns = { @JoinColumn(name = "car_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "owner_id", nullable = false, updatable = false)}
    )
    private Set<Owner> owners = new HashSet<>();
}
