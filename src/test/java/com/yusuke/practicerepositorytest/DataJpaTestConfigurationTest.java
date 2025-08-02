package com.yusuke.practicerepositorytest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.yusuke.practicerepositorytest.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ TestcontainersConfiguration.class })
@ActiveProfiles("test")
public class DataJpaTestConfigurationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testDataJpaTestConfiguration() {
        // 基本的なテスト - UserRepositoryが正しく注入されることを確認
        assertNotNull(userRepository);

        // データベース接続が正常に動作することを確認
        long count = userRepository.count();
        // count()が例外を投げずに実行されることを確認
        assertNotNull(count);
    }
}