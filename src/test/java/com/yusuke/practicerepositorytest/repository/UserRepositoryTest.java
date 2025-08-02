package com.yusuke.practicerepositorytest.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yusuke.practicerepositorytest.TestSqlExecutor;
import com.yusuke.practicerepositorytest.TestcontainersConfiguration;
import com.yusuke.practicerepositorytest.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestcontainersConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestSqlExecutor testSqlExecutor;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @Before
    public void setUp() {
        // テーブル作成SQLを実行
        testSqlExecutor.executeSqlFile("create_table.sql");

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
    }

    @After
    public void tearDown() {
        // テストデータのクリーンアップ
        userRepository.deleteAll();
    }

    @Test
    public void testSaveUser() {
        // Given
        User user = testUser1;

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getAge(), savedUser.getAge());
        assertNotNull(savedUser.getCreatedAt());
    }

    @Test
    public void testFindById() {
        // Given
        User savedUser = userRepository.save(testUser1);

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getName(), foundUser.get().getName());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testFindAll() {
        // Given
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        // When
        List<User> allUsers = userRepository.findAll();

        // Then
        assertEquals(3, allUsers.size());
    }

    @Test
    public void testFindByEmail() {
        // Given
        userRepository.save(testUser1);

        // When
        User foundUser = userRepository.findByEmail(testUser1.getEmail());

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser1.getName(), foundUser.getName());
        assertEquals(testUser1.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testFindByAge() {
        // Given
        userRepository.save(testUser1); // age: 25
        userRepository.save(testUser2); // age: 30
        userRepository.save(testUser3); // age: 28

        // When
        List<User> usersAge25 = userRepository.findByAge(25);
        List<User> usersAge30 = userRepository.findByAge(30);

        // Then
        assertEquals(1, usersAge25.size());
        assertEquals("田中太郎", usersAge25.get(0).getName());

        assertEquals(1, usersAge30.size());
        assertEquals("佐藤花子", usersAge30.get(0).getName());
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        // Given
        userRepository.save(testUser1); // 田中太郎
        userRepository.save(testUser2); // 佐藤花子
        userRepository.save(testUser3); // 田中次郎

        // When
        List<User> tanakaUsers = userRepository.findByNameContainingIgnoreCase("田中");
        List<User> satoUsers = userRepository.findByNameContainingIgnoreCase("佐藤");

        // Then
        assertEquals(2, tanakaUsers.size());
        assertTrue(tanakaUsers.stream().allMatch(user -> user.getName().contains("田中")));

        assertEquals(1, satoUsers.size());
        assertEquals("佐藤花子", satoUsers.get(0).getName());
    }

    @Test
    public void testUpdateUser() {
        // Given
        User savedUser = userRepository.save(testUser1);
        String newName = "田中太郎（更新）";

        // When
        savedUser.setName(newName);
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertEquals(newName, updatedUser.getName());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }

    @Test
    public void testDeleteUser() {
        // Given
        User savedUser = userRepository.save(testUser1);
        Long userId = savedUser.getId();

        // When
        userRepository.deleteById(userId);

        // Then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testCountUsers() {
        // Given
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        // When
        long count = userRepository.count();

        // Then
        assertEquals(2, count);
    }

    @Test
    public void testExistsById() {
        // Given
        User savedUser = userRepository.save(testUser1);
        Long userId = savedUser.getId();

        // When & Then
        assertTrue(userRepository.existsById(userId));
        assertFalse(userRepository.existsById(999L));
    }
}