package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostRepository {

    private CrudRepository crudRepository;

    private static final String FINDBYCONDITION = "SELECT DISTINCT p FROM Post p JOIN FETCH p.history "
            + "LEFT JOIN FETCH p.participates WHERE %s ORDER BY p.id";

    public List<Post> findNewPosts() {
        return crudRepository.query(String.format(FINDBYCONDITION, "p.created >= :oneDay"),
                Post.class, Map.of("oneDay", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findPostsWithImage() {
        return crudRepository.query(String.format(FINDBYCONDITION, "p.image IS MOT NULL"),
                Post.class);
    }

    public List<Post> findPostByCarName(String carName) {
        return crudRepository.query(String.format(FINDBYCONDITION, "lower(p.car.name) LIKE lower(:carName)"),
                Post.class, Map.of("carName", '%' + carName + '%'));
    }
}
