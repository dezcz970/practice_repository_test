package com.yusuke.practicerepositorytest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yusuke.practicerepositorytest.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // 基本的なクエリメソッドも追加可能
    // 例: メールアドレスで検索
    User findByEmail(String email);

    // 例: 年齢で検索
    List<User> findByAge(Integer age);

    // 例: 名前の部分一致検索
    List<User> findByNameContainingIgnoreCase(String name);
}