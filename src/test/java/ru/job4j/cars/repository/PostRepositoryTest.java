package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PostRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final PostRepository postRepository = new PostRepository(crudRepository);
    private final UserRepository userRepository = new UserRepository(crudRepository);
    private final CarRepository carRepository = new CarRepository(crudRepository);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);
    private final ImageRepository imageRepository = new ImageRepository(crudRepository);
    private final User user = new User();
    private final Car car1 = new Car();
    private final Car car2 = new Car();
    private final Engine engine = new Engine();
    private final Image image1 = new Image();
    private final Image image2 = new Image();

    @BeforeEach
    public void createUser() {
        user.setLogin("login");
        user.setPassword("pass");
        userRepository.create(user);
    }

    @BeforeEach
    public void createCars() {
        engine.setName("engine");
        engineRepository.create(engine);
        car1.setName("car1");
        car1.setEngine(engine);
        carRepository.create(car1);
        car2.setName("car2");
        car2.setEngine(engine);
        carRepository.create(car2);
    }

    @BeforeEach
    public void createImages() {
        image1.setName("image1");
        image1.setPath("path1");
        imageRepository.save(image1);
        image2.setName("image2");
        image2.setPath("path2");
        imageRepository.save(image2);
    }

    @AfterEach
    public void cleanDB() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Image").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    public void whenCreateNewPostAndFindByIdThenMustBeTheSamePost() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        var rsl = postRepository.findById(post.getId()).get();
        assertThat(rsl).isEqualTo(post);
    }

    @Test
    public void whenCreatePostsAndFindAllThenMustBeListOfThisPosts() {
        var post1 = new Post();
        post1.setDescription("description");
        post1.setPrice(100);
        post1.setUser(user);
        post1.setCar(car1);
        post1.setImage(image1);
        postRepository.create(post1);
        var post2 = new Post();
        post2.setDescription("description");
        post2.setPrice(100);
        post2.setUser(user);
        post2.setCar(car1);
        post2.setImage(image1);
        postRepository.create(post2);
        assertThat(postRepository.findAll()).isEqualTo(List.of(post1, post2));
    }

    @Test
    public void whenFindAllInEmptyRepoThenMustBeEmptyList() {
        assertThat(postRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenCreatePostAndUpdateItThenMustBeTheSecondDescription() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        post.setDescription("description2");
        postRepository.update(post);
        var rsl = postRepository.findById(post.getId()).get();
        assertThat(rsl).isEqualTo(post);
    }

    @Test
    public void whenCreateCarAndDeleteItThenMustBeEmptyOptional() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        postRepository.delete(post.getId());
        var rsl = postRepository.findById(post.getId());
        assertThat(rsl).isEmpty();
    }

    @Test
    public void whenCreateNewPostAndFindNewPostsThenMustBeListWithThisPost() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        var rsl = postRepository.findNewPosts();
        assertThat(rsl).isEqualTo(List.of(post));
    }

    @Test
    public void whenCreatePostWithOldDateAndFindNewPostsThenMustBeEmptyList() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setCreated(LocalDateTime.now().minusDays(3));
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        assertThat(postRepository.findNewPosts()).isEmpty();
    }

    @Test
    public void whenCreateNewPostWithImageAndFindPostsWithImageThenMustBeTheSamePost() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        var rsl = postRepository.findPostsWithImage();
        assertThat(rsl).isEqualTo(List.of(post));
    }

    @Test
    public void whenCreateNewPostWithoutImageAndFindPostsWithImageThenMustBeTheSamePost() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        postRepository.create(post);
        assertThat(postRepository.findPostsWithImage()).isEmpty();
    }

    @Test
    public void whenCreateNewPostAndFindByItsCarNameThenMustBeTheSamePost() {
        var post = new Post();
        post.setDescription("description");
        post.setPrice(100);
        post.setUser(user);
        post.setCar(car1);
        post.setImage(image1);
        postRepository.create(post);
        var rsl = postRepository.findPostByCarName("car");
        assertThat(rsl).isEqualTo(List.of(post));
    }

    @Test
    public void whenFindByIncorrectCarNameThenMustBeEmptyList() {
        assertThat(postRepository.findPostByCarName("auto")).isEmpty();
    }
}