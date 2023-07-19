package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class OwnerRepository {

    private CrudRepository crudRepository;

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    public boolean update(Owner owner) {
        return crudRepository.bool("UPDATE Owner SET name = :uName, user = :uUser WHERE id = :uId",
                Map.of("uName", owner.getName(),
                        "uUser", owner.getUser(),
                        "uId", owner.getId()));
    }

    public boolean delete(int id) {
        return crudRepository.bool("DELETE FROM Owner WHERE id = :id",
                Map.of("id", id));
    }

    public List<Owner> findAll() {
        return crudRepository.query("FROM Owner ORDER BY id", Owner.class);
    }

    public Optional<Owner> findById(int id) {
        return crudRepository.optional("FROM Owner WHERE id = :id", Owner.class, Map.of("id", id));
    }
}
