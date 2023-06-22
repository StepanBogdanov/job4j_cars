package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE User SET login = :ulogin, password = :upassword WHERE id = :uid")
                    .setParameter("ulogin", user.getLogin())
                    .setParameter("upassword", user.getPassword())
                    .setParameter("uid", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :userId")
                    .setParameter("userId", userId).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        List<User> users = new ArrayList<>();
        try {
            session.beginTransaction();
            users.addAll(session.createQuery("FROM User ORDER BY id").list());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return users;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Optional<User> userOptional;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE id = :userId")
                    .setParameter("userId", userId);
            userOptional = Optional.of(query.uniqueResult());
            session.getTransaction().commit();
        } catch (Exception e) {
            userOptional = Optional.empty();
            session.getTransaction().rollback();
        }
        session.close();
        return userOptional;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        List<User> users = new ArrayList<>();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE login LIKE :key")
                    .setParameter("key", "%" + key + "%");
            users.addAll(query.getResultList());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return users;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Optional<User> userOptional;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE login = :login")
                    .setParameter("login", login);
            userOptional = Optional.of(query.uniqueResult());
            session.getTransaction().commit();
        } catch (Exception e) {
            userOptional = Optional.empty();
            session.getTransaction().rollback();
        }
        session.close();
        return userOptional;
    }
}