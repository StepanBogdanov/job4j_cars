package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class CarRepository {

    private CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public boolean update(Car car) {
        return crudRepository.bool("UPDATE Car SET name = :uName, engine = :uEngine WHERE id = :uId",
                Map.of("uName", car.getName(),
                        "uEngine", car.getEngine(),
                        "uId", car.getId()));
    }

    public boolean delete(int id) {
        return crudRepository.bool("DELETE FROM Car WHERE id = :id",
                Map.of("id", id));
    }

    public List<Car> findAll() {
        return crudRepository.query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.owners ORDER BY c.id",
                Car.class);
    }

    public Optional<Car> findById(int id) {
        return crudRepository.optional("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.owners WHERE c.id = :id",
                Car.class, Map.of("id", id));
    }
}
