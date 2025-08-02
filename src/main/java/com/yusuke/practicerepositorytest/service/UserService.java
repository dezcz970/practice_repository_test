package com.yusuke.practicerepositorytest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yusuke.practicerepositorytest.entity.User;
import com.yusuke.practicerepositorytest.repository.UserRepository;
import com.yusuke.practicerepositorytest.specification.UserSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 全てのユーザーを取得
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * IDでユーザーを取得
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * メールアドレスでユーザーを取得
     */
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    /**
     * 名前で部分一致検索
     */
    public List<User> findUsersByNameContaining(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * 年齢で検索
     */
    public List<User> findUsersByAge(Integer age) {
        return userRepository.findByAge(age);
    }

    /**
     * Specificationを使用した動的クエリ - 名前で検索
     */
    public List<User> findUsersByNameLike(String name) {
        Specification<User> spec = UserSpecification.nameLike(name);
        return userRepository.findAll(spec);
    }

    /**
     * Specificationを使用した動的クエリ - メールアドレスで検索
     */
    public List<User> findUsersByEmailLike(String email) {
        Specification<User> spec = UserSpecification.emailLike(email);
        return userRepository.findAll(spec);
    }

    /**
     * Specificationを使用した動的クエリ - 年齢範囲で検索
     */
    public List<User> findUsersByAgeBetween(Integer minAge, Integer maxAge) {
        Specification<User> spec = UserSpecification.ageBetween(minAge, maxAge);
        return userRepository.findAll(spec);
    }

    /**
     * Specificationを使用した動的クエリ - 複数条件で検索
     */
    public List<User> searchUsers(String name, String email, Integer minAge, Integer maxAge) {
        Specification<User> spec = UserSpecification.searchUsers(name, email, minAge, maxAge);
        return userRepository.findAll(spec);
    }

    /**
     * Specificationを使用した動的クエリ - ページネーション付き
     */
    public Page<User> searchUsersWithPagination(String name, String email, Integer minAge, Integer maxAge,
            Pageable pageable) {
        Specification<User> spec = UserSpecification.searchUsers(name, email, minAge, maxAge);
        return userRepository.findAll(spec, pageable);
    }

    /**
     * Specificationを使用した動的クエリ - ソート付き
     */
    public List<User> searchUsersWithSort(String name, String email, Integer minAge, Integer maxAge, Sort sort) {
        Specification<User> spec = UserSpecification.searchUsers(name, email, minAge, maxAge);
        return userRepository.findAll(spec, sort);
    }

    /**
     * 複数のSpecificationを組み合わせた検索
     */
    public List<User> findUsersWithMultipleSpecifications(String name, Integer minAge) {
        Specification<User> nameSpec = UserSpecification.nameLike(name);
        Specification<User> ageSpec = UserSpecification.ageGreaterThanOrEqualTo(minAge);

        // AND条件で組み合わせ
        Specification<User> combinedSpec = nameSpec.and(ageSpec);

        return userRepository.findAll(combinedSpec);
    }

    /**
     * ユーザーを保存
     */
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * ユーザーを更新
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setAge(userDetails.getAge());

        return userRepository.save(user);
    }

    /**
     * ユーザーを削除
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}