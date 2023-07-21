package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @EqualsAndHashCode.Include
    private String path;
}
