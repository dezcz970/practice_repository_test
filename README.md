# Spring Data JPA Specification サンプル

このプロジェクトは、Spring Data JPAのSpecificationを利用した動的クエリの実装例です。

## 概要

Specificationを使用することで、実行時に動的にクエリ条件を組み立てることができます。これにより、複雑な検索条件やフィルタリング機能を柔軟に実装できます。

## 主な機能

### 1. 基本的なSpecification
- 名前での部分一致検索
- メールアドレスでの部分一致検索
- 年齢範囲での検索
- 複数条件の組み合わせ検索

### 2. 高度な機能
- ページネーション対応
- ソート機能
- 複数のSpecificationの組み合わせ

## プロジェクト構造

```
src/main/java/com/yusuke/practicerepositorytest/
├── entity/
│   └── User.java                    # ユーザーエンティティ
├── repository/
│   └── UserRepository.java          # JpaRepository + JpaSpecificationExecutor
├── specification/
│   └── UserSpecification.java       # Specificationクラス
├── service/
│   └── UserService.java             # ビジネスロジック
├── controller/
│   └── UserController.java          # REST API
└── config/
    └── JpaConfig.java               # JPA設定
```

## 使用方法

### 1. 基本的な検索

```java
// 名前で検索
List<User> users = userService.findUsersByNameLike("田中");

// 年齢範囲で検索
List<User> users = userService.findUsersByAgeBetween(20, 30);

// 複数条件で検索
List<User> users = userService.searchUsers("田中", "example.com", 20, 30);
```

### 2. ページネーション付き検索

```java
PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));
Page<User> userPage = userService.searchUsersWithPagination("田中", null, null, null, pageable);
```

### 3. 複数のSpecificationを組み合わせ

```java
Specification<User> nameSpec = UserSpecification.nameLike("田中");
Specification<User> ageSpec = UserSpecification.ageGreaterThanOrEqualTo(25);
Specification<User> combinedSpec = nameSpec.and(ageSpec);

List<User> users = userRepository.findAll(combinedSpec);
```

## REST API エンドポイント

### 基本的な検索
- `GET /api/users` - 全ユーザー取得
- `GET /api/users/{id}` - IDでユーザー取得
- `GET /api/users/email/{email}` - メールアドレスでユーザー取得

### Specificationを使用した検索
- `GET /api/users/spec/name?name=田中` - 名前で検索
- `GET /api/users/spec/email?email=example` - メールアドレスで検索
- `GET /api/users/spec/age-range?minAge=20&maxAge=30` - 年齢範囲で検索
- `GET /api/users/spec/search?name=田中&minAge=20&maxAge=30` - 複数条件で検索

### ページネーション付き検索
- `GET /api/users/spec/search/paged?name=田中&page=0&size=10&sortBy=name&sortDir=asc`

### CRUD操作
- `POST /api/users` - ユーザー作成
- `PUT /api/users/{id}` - ユーザー更新
- `DELETE /api/users/{id}` - ユーザー削除

## テスト

```bash
# テストの実行
./gradlew test

# 特定のテストクラスの実行
./gradlew test --tests UserServiceTest
```

## データベース

開発環境ではH2インメモリデータベースを使用しています。

- H2コンソール: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- ユーザー名: `sa`
- パスワード: (空)

## 設定

### application.properties
```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
```

## Specificationの利点

1. **動的クエリ**: 実行時に条件を組み立て可能
2. **型安全性**: コンパイル時にエラーを検出
3. **再利用性**: 複数のSpecificationを組み合わせ可能
4. **保守性**: クエリロジックを分離して管理
5. **テスト容易性**: 個別のSpecificationをテスト可能

## 注意事項

- Specificationは複雑なクエリには適していますが、シンプルなクエリには通常のクエリメソッドの方が適しています
- パフォーマンスを考慮する場合は、適切なインデックスを設定してください
- 大量のデータを扱う場合は、ページネーションを必ず使用してください 