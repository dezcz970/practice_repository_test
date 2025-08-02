package com.yusuke.practicerepositorytest.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yusuke.practicerepositorytest.TestcontainersConfiguration;
import com.yusuke.practicerepositorytest.entity.User;
import com.yusuke.practicerepositorytest.specification.UserSpecification;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ TestcontainersConfiguration.class })
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
        "logging.level.org.hibernate.SQL=DEBUG",
        "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
public class UserRepositorySpecificationTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @Before
    public void setUp() {
        // テストデータの準備
        testUser1 = User.builder()
                .name("田中太郎")
                .email("tanaka@example.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        testUser2 = User.builder()
                .name("佐藤花子")
                .email("sato@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();

        testUser3 = User.builder()
                .name("田中次郎")
                .email("tanaka2@example.com")
                .age(28)
                .createdAt(LocalDateTime.now())
                .build();

        // テストデータを保存
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
    }

    @After
    public void tearDown() {
        // テストデータのクリーンアップ
        userRepository.deleteAll();
    }

    @Test
    public void testFindByAgeGreaterThan() {
        // Given
        Specification<User> spec = UserSpecification.ageGreaterThan(25);

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().allMatch(user -> user.getAge() > 25));
    }

    @Test
    public void testFindByAgeBetween() {
        // Given
        Specification<User> spec = UserSpecification.ageBetween(25, 30);

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(3, users.size());
        assertTrue(users.stream().allMatch(user -> user.getAge() >= 25 && user.getAge() <= 30));
    }

    @Test
    public void testFindByNameLike() {
        // Given
        Specification<User> spec = UserSpecification.nameLike("田中");

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().allMatch(user -> user.getName().contains("田中")));
    }

    @Test
    public void testFindByEmailLike() {
        // Given
        Specification<User> spec = UserSpecification.emailLike("tanaka");

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().allMatch(user -> user.getEmail().contains("tanaka")));
    }

    @Test
    public void testFindByComplexCriteria() {
        // Given
        Specification<User> spec = Specification.where(UserSpecification.ageGreaterThan(25))
                .and(UserSpecification.nameLike("田中"));

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(1, users.size());
        assertEquals("田中次郎", users.get(0).getName());
        assertTrue(users.get(0).getAge() > 25);
    }

    @Test
    public void testFindByAgeOrName() {
        // Given
        Specification<User> spec = Specification.where(UserSpecification.ageEquals(25))
                .or(UserSpecification.nameLike("佐藤"));

        // When
        List<User> users = userRepository.findAll(spec);

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getAge() == 25));
        assertTrue(users.stream().anyMatch(user -> user.getName().contains("佐藤")));
    }

    @Test
    public void testCountWithSpecification() {
        // Given
        Specification<User> spec = UserSpecification.ageGreaterThan(25);

        // When
        long count = userRepository.count(spec);

        // Then
        assertEquals(2, count);
    }

    @Test
    public void testFindOneWithSpecification() {
        // Given
        Specification<User> spec = UserSpecification.emailEquals("sato@example.com");

        // When
        var result = userRepository.findOne(spec);

        // Then
        assertTrue(result.isPresent());
        assertEquals("佐藤花子", result.get().getName());
    }
}