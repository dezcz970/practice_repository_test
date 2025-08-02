package com.yusuke.practicerepositorytest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yusuke.practicerepositorytest.entity.User;
import com.yusuke.practicerepositorytest.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 全てのユーザーを取得
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * IDでユーザーを取得
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * メールアドレスでユーザーを取得
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 名前で部分一致検索
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> users = userService.findUsersByNameContaining(name);
        return ResponseEntity.ok(users);
    }

    /**
     * 年齢で検索
     */
    @GetMapping("/search/age")
    public ResponseEntity<List<User>> searchUsersByAge(@RequestParam Integer age) {
        List<User> users = userService.findUsersByAge(age);
        return ResponseEntity.ok(users);
    }

    /**
     * Specificationを使用した動的クエリ - 名前で検索
     */
    @GetMapping("/spec/name")
    public ResponseEntity<List<User>> searchUsersByNameSpec(@RequestParam String name) {
        List<User> users = userService.findUsersByNameLike(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Specificationを使用した動的クエリ - メールアドレスで検索
     */
    @GetMapping("/spec/email")
    public ResponseEntity<List<User>> searchUsersByEmailSpec(@RequestParam String email) {
        List<User> users = userService.findUsersByEmailLike(email);
        return ResponseEntity.ok(users);
    }

    /**
     * Specificationを使用した動的クエリ - 年齢範囲で検索
     */
    @GetMapping("/spec/age-range")
    public ResponseEntity<List<User>> searchUsersByAgeRange(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        List<User> users = userService.findUsersByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    /**
     * Specificationを使用した動的クエリ - 複数条件で検索
     */
    @GetMapping("/spec/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        List<User> users = userService.searchUsers(name, email, minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    /**
     * Specificationを使用した動的クエリ - ページネーション付き
     */
    @GetMapping("/spec/search/paged")
    public ResponseEntity<Page<User>> searchUsersWithPagination(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userService.searchUsersWithPagination(name, email, minAge, maxAge, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 複数のSpecificationを組み合わせた検索
     */
    @GetMapping("/spec/combined")
    public ResponseEntity<List<User>> searchUsersWithMultipleSpecs(
            @RequestParam String name,
            @RequestParam Integer minAge) {
        List<User> users = userService.findUsersWithMultipleSpecifications(name, minAge);
        return ResponseEntity.ok(users);
    }

    /**
     * ユーザーを作成
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * ユーザーを更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ユーザーを削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}