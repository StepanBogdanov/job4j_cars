package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OwnerRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final OwnerRepository ownerRepository = new OwnerRepository(crudRepository);
    private final UserRepository userRepository = new UserRepository(crudRepository);
    private final User user = new User();

    @BeforeEach
    public void createUser() {
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
    }

    @AfterEach
    public void cleanDB() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    public void whenCreateNewOwnerAndFindByIdThenMustBeTheSameOwner() {
        var owner = new Owner();
        owner.setName("name");
        owner.setUser(user);
        ownerRepository.create(owner);
        var rsl = ownerRepository.findById(owner.getId()).get();
        assertThat(rsl).isEqualTo(owner);
    }

    @Test
    public void whenCreateOwnersAndFindAllThenMustBeListOfThisOwners() {
        var owner1 = new Owner();
        owner1.setName("name1");
        owner1.setUser(user);
        var owner2 = new Owner();
        owner2.setName("name2");
        owner2.setUser(user);
        ownerRepository.create(owner1);
        ownerRepository.create(owner2);
        assertThat(ownerRepository.findAll()).isEqualTo(List.of(owner1, owner2));
    }

    @Test
    public void whenFindAllInEmptyRepoThenMustBeEmptyList() {
        assertThat(ownerRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenCreateOwnerAndUpdateItThenMustBeTheSecondName() {
        var owner = new Owner();
        owner.setName("name1");
        owner.setUser(user);
        ownerRepository.create(owner);
        owner.setName("name2");
        ownerRepository.update(owner);
        var rsl = ownerRepository.findById(owner.getId()).get();
        assertThat(rsl).isEqualTo(owner);
    }

    @Test
    public void whenCreateOwnerAndDeleteItThenMustBeEmptyOptional() {
        var owner = new Owner();
        owner.setName("name");
        owner.setUser(user);
        ownerRepository.create(owner);
        ownerRepository.delete(owner.getId());
        var rsl = ownerRepository.findById(owner.getId());
        assertThat(rsl).isEmpty();
    }

}