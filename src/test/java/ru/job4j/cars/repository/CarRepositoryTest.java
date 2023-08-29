package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CarRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final CarRepository carRepository = new CarRepository(crudRepository);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);
    private final Engine engine = new Engine();

    @BeforeEach
    public void createEngine() {
        engine.setName("engine");
        engineRepository.create(engine);
    }

    @AfterEach
    public void cleanDB() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    public void whenCreateNewCarAndFindByIdThenMustBeTheSameCar() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        carRepository.create(car);
        var rsl = carRepository.findById(car.getId()).get();
        assertThat(rsl).isEqualTo(car);
    }

    @Test
    public void whenCreateCarsAndFindAllThenMustBeListOfThisCars() {
        var car1 = new Car();
        car1.setName("car1");
        car1.setEngine(engine);
        var car2 = new Car();
        car2.setName("car2");
        car2.setEngine(engine);
        carRepository.create(car1);
        carRepository.create(car2);
        assertThat(carRepository.findAll()).isEqualTo(List.of(car1, car2));
    }

    @Test
    public void whenFindAllInEmptyRepoThenMustBeEmptyList() {
        assertThat(carRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenCreateCarAndUpdateItThenMustBeTheSecondName() {
        var car = new Car();
        car.setName("car1");
        car.setEngine(engine);
        carRepository.create(car);
        car.setName("car2");
        carRepository.update(car);
        var rsl = carRepository.findById(car.getId()).get();
        assertThat(rsl).isEqualTo(car);
    }

    @Test
    public void whenCreateCarAndDeleteItThenMustBeEmptyOptional() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        carRepository.create(car);
        carRepository.delete(car.getId());
        var rsl = carRepository.findById(car.getId());
        assertThat(rsl).isEmpty();
    }


}