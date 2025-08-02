package com.yusuke.practicerepositorytest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PracticeRepositoryTestApplicationTests {

    @Test
    void contextLoads() {
    }

}
