package com.yusuke.practicerepositorytest.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.yusuke.practicerepositorytest.TestcontainersConfiguration;
import com.yusuke.practicerepositorytest.entity.User;
import com.yusuke.practicerepositorytest.repository.UserRepository;

@SpringBootTest
@Import({ TestcontainersConfiguration.class })
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // テストデータを作成
        User user1 = User.builder()
                .name("田中太郎")
                .email("tanaka@example.com")
                .age(25)
                .build();

        User user2 = User.builder()
                .name("佐藤花子")
                .email("sato@example.com")
                .age(30)
                .build();

        User user3 = User.builder()
                .name("田中次郎")
                .email("tanaka2@example.com")
                .age(35)
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void testFindUsersByNameLike() {
        // 名前で部分一致検索
        List<User> users = userService.findUsersByNameLike("田中");

        assertThat(users).hasSize(2);
        assertThat(users).allMatch(user -> user.getName().contains("田中"));
    }

    @Test
    void testFindUsersByEmailLike() {
        // メールアドレスで部分一致検索
        List<User> users = userService.findUsersByEmailLike("tanaka");

        assertThat(users).hasSize(2);
        assertThat(users).allMatch(user -> user.getEmail().contains("tanaka"));
    }

    @Test
    void testFindUsersByAgeBetween() {
        // 年齢範囲で検索
        List<User> users = userService.findUsersByAgeBetween(25, 32);

        assertThat(users).hasSize(2);
        assertThat(users).allMatch(user -> user.getAge() >= 25 && user.getAge() <= 32);
    }

    @Test
    void testSearchUsers() {
        // 複数条件で検索
        List<User> users = userService.searchUsers("田中", null, 25, 40);

        assertThat(users).hasSize(2);
        assertThat(users).allMatch(user -> user.getName().contains("田中") &&
                user.getAge() >= 25 &&
                user.getAge() <= 40);
    }

    @Test
    void testSearchUsersWithPagination() {
        // ページネーション付き検索
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("name"));
        Page<User> userPage = userService.searchUsersWithPagination("田中", null, null, null, pageable);

        assertThat(userPage.getContent()).hasSize(2);
        assertThat(userPage.getTotalElements()).isEqualTo(2);
        assertThat(userPage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void testSearchUsersWithSort() {
        // ソート付き検索
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        List<User> users = userService.searchUsersWithSort("田中", null, null, null, sort);

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getAge()).isGreaterThan(users.get(1).getAge());
    }

    @Test
    void testFindUsersWithMultipleSpecifications() {
        // 複数のSpecificationを組み合わせた検索
        List<User> users = userService.findUsersWithMultipleSpecifications("田中", 30);

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).contains("田中");
        assertThat(users.get(0).getAge()).isGreaterThanOrEqualTo(30);
    }

    @Test
    void testSearchUsersWithNullParameters() {
        // nullパラメータでの検索（全てのユーザーが返される）
        List<User> users = userService.searchUsers(null, null, null, null);

        assertThat(users).hasSize(3);
    }

    @Test
    void testSearchUsersWithEmptyStringParameters() {
        // 空文字パラメータでの検索（全てのユーザーが返される）
        List<User> users = userService.searchUsers("", "", null, null);

        assertThat(users).hasSize(3);
    }
}