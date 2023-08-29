package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final UserRepository userRepository = new UserRepository(crudRepository);

    @AfterEach
    public void cleanTable() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    public void whenCreateNewUserAndFindByIdThenMustBeTheSameUser() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        userRepository.create(user1);
        var rsl = userRepository.findById(user1.getId()).get();
        assertThat(rsl).isEqualTo(user1);
    }

    @Test
    public void whenCreateNewUserAndFindByNameThenMustBeTheSameUser() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        userRepository.create(user1);
        var rsl = userRepository.findByLogin(user1.getLogin()).get();
        assertThat(rsl).isEqualTo(user1);
    }

    @Test
    public void whenCreateUsersAndFindByLikeNameThenMustBeListOfThisUsers() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        var user2 = new User();
        user2.setLogin("login2");
        user2.setPassword("pass2");
        userRepository.create(user1);
        userRepository.create(user2);
        var rsl = userRepository.findByLikeLogin("login");
        assertThat(rsl).isEqualTo(List.of(user1, user2));
    }

    @Test
    public void whenCreateUserAndFindByLikeWrongNameThenMustBeEmptyList() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        userRepository.create(user1);
        var rsl = userRepository.findByLikeLogin("login2");
        assertThat(rsl).isEqualTo(List.of());
    }

    @Test
    public void whenFindByLikeNameInEmptyRepoThenMustBeEmptyList() {
        assertThat(userRepository.findByLikeLogin("login")).isEqualTo(List.of());
    }

    @Test
    public void whenCreateUserAndUpdateItThenMustBeTheSecondName() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        userRepository.create(user1);
        user1.setLogin("user2");
        userRepository.update(user1);
        var rsl = userRepository.findById(user1.getId()).get();
        assertThat(rsl).isEqualTo(user1);
    }

    @Test
    public void whenCreateUserAndDeleteItThenMustBeEmptyOptional() {
        var user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("pass1");
        userRepository.create(user1);
        userRepository.delete(user1.getId());
        var rsl = userRepository.findById(user1.getId());
        assertThat(rsl).isEmpty();
    }
}