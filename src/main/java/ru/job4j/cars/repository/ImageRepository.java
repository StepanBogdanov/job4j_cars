package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Image;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class ImageRepository {

    private CrudRepository crudRepository;

    public Image save(Image image) {
        crudRepository.run(session -> session.persist(image));
        return image;
    }

    public Optional<Image> findById(int id) {
        return crudRepository.optional("FROM Image WHERE id = :id", Image.class, Map.of("id", id));
    }
}
