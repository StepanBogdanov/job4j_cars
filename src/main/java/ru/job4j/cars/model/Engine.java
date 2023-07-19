package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "engines")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Engine {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
