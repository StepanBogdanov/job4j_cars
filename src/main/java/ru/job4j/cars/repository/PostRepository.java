package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PostRepository {

    private CrudRepository crudRepository;

    private static final String FINDBYCONDITION = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.history "
            + "LEFT JOIN FETCH p.participates WHERE %s ORDER BY p.id";

    public List<Post> findNewPosts() {
        return crudRepository.query(String.format(FINDBYCONDITION, "p.created >= :oneDay"),
                Post.class, Map.of("oneDay", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findPostsWithImage() {
        return crudRepository.query(String.format(FINDBYCONDITION, "p.image IS NOT NULL"),
                Post.class);
    }

    public List<Post> findPostByCarName(String carName) {
        return crudRepository.query(String.format(FINDBYCONDITION, "lower(p.car.name) LIKE lower(:carName)"),
                Post.class, Map.of("carName", '%' + carName + '%'));
    }

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public boolean update(Post post) {
        return crudRepository.bool("UPDATE Post SET description = :description, price = :price, image = :image WHERE id = :uId",
                Map.of("description", post.getDescription(),
                        "price", post.getPrice(),
                        "image", post.getImage(),
                        "uId", post.getId()));
    }

    public boolean delete(int id) {
        return crudRepository.bool("DELETE FROM Post WHERE id = :id",
                Map.of("id", id));
    }

    public List<Post> findAll() {
        return crudRepository.query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.history "
                + "LEFT JOIN FETCH p.participates ORDER BY p.id", Post.class);
    }

    public Optional<Post> findById(int id) {
        return crudRepository.optional("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.history "
                + "LEFT JOIN FETCH p.participates WHERE p.id = :id", Post.class, Map.of("id", id));
    }
}
