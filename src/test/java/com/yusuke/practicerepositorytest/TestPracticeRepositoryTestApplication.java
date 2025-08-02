package com.yusuke.practicerepositorytest;

import org.springframework.boot.SpringApplication;

public class TestPracticeRepositoryTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(PracticeRepositoryTestApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
