package com.yusuke.practicerepositorytest.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

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
public class UserRepositorySqlTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestSqlExecutor testSqlExecutor;

    @Before
    public void setUp() {
        // テーブル作成SQLを実行
        testSqlExecutor.executeSqlFile("create_table.sql");

        // テストデータ挿入SQLを実行
        testSqlExecutor.executeSqlFile("insert_test_data.sql");
    }

    @Test
    public void testFindAllWithSqlData() {
        // When
        List<User> allUsers = userRepository.findAll();

        // Then
        assertEquals(5, allUsers.size());

        // SQLファイルで挿入したデータが存在することを確認
        User tanakaUser = userRepository.findByEmail("tanaka@example.com");
        assertNotNull(tanakaUser);
        assertEquals("田中太郎", tanakaUser.getName());
        assertEquals(25, tanakaUser.getAge().intValue());
    }

    @Test
    public void testFindByAgeWithSqlData() {
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
    public void testFindByNameContainingIgnoreCaseWithSqlData() {
        // When
        List<User> tanakaUsers = userRepository.findByNameContainingIgnoreCase("田中");
        List<User> yamadaUsers = userRepository.findByNameContainingIgnoreCase("山田");

        // Then
        assertEquals(2, tanakaUsers.size());
        assertEquals(1, yamadaUsers.size());
        assertEquals("山田三郎", yamadaUsers.get(0).getName());
    }

    @Test
    public void testCountWithSqlData() {
        // When
        long count = userRepository.count();

        // Then
        assertEquals(5, count);
    }
}