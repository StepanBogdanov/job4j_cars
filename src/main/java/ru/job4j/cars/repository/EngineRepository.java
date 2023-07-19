package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class EngineRepository {

    private CrudRepository crudRepository;

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public boolean update(Engine engine) {
        return crudRepository.bool("UPDATE Engine SET name = :uName WHERE id = :uId",
                Map.of("uName", engine.getName(),
                        "uId", engine.getId()));
    }

    public boolean delete(int id) {
        return crudRepository.bool("DELETE FROM Engine WHERE id = :id",
                Map.of("id", id));
    }

    public List<Engine> findAll() {
        return crudRepository.query("FROM Engine ORDER BY id", Engine.class);
    }

    public Optional<Engine> findById(int id) {
        return crudRepository.optional("FROM Engine WHERE id = :id", Engine.class, Map.of("id", id));
    }
}
