package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EngineRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);

    @AfterEach
    public void cleanTable() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    public void whenCreateNewEngineAndFindByIdThenMustBeTheSameEngine() {
        var engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);
        var rsl = engineRepository.findById(engine.getId()).get();
        assertThat(rsl).isEqualTo(engine);
    }

    @Test
    public void whenCreateEnginesAndFindAllThenMustBeListOfThisEngines() {
        var engine1 = new Engine();
        engine1.setName("engine1");
        var engine2 = new Engine();
        engine2.setName("engine2");
        engineRepository.create(engine1);
        engineRepository.create(engine2);
        assertThat(engineRepository.findAll()).isEqualTo(List.of(engine1, engine2));
    }

    @Test
    public void whenFindAllInEmptyRepoThenMustBeEmptyList() {
        assertThat(engineRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenCreateEngineAndUpdateItThenMustBeTheSecondName() {
        var engine = new Engine();
        engine.setName("engine1");
        engineRepository.create(engine);
        engine.setName("engine2");
        engineRepository.update(engine);
        var rsl = engineRepository.findById(engine.getId()).get();
        assertThat(rsl).isEqualTo(engine);
    }

    @Test
    public void whenCreateEngineAndDeleteItThenMustBeEmptyOptional() {
        var engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);
        engineRepository.delete(engine.getId());
        var rsl = engineRepository.findById(engine.getId());
        assertThat(rsl).isEmpty();
    }

}