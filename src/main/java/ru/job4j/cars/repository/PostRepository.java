package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostRepository {

    private CrudRepository crudRepository;

    public List<Post> findNewPosts() {
        return crudRepository.query("SELECT DISTINCT p FROM Post p JOIN FETCH p.history LEFT JOIN FETCH p.participates WHERE p.created >= oneDay ORDER BY p.id",
                Post.class, Map.of("oneDay", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findPostsWithImage() {
        return crudRepository.query("SELECT DISTINCT p FROM Post p JOIN FETCH p.history LEFT JOIN FETCH p.participates WHERE p.image IS MOT NULL ORDER BY p.id",
                Post.class);
    }

    public List<Post> findPostByCarName(String carName) {
        return crudRepository.query("SELECT DISTINCT p FROM Post p JOIN FETCH p.history LEFT JOIN FETCH p.participates WHERE lower(p.car.name) LIKE lower(:carName) ORDER BY p.id",
                Post.class, Map.of("carName", '%' + carName + '%'));
    }
}
