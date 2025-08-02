package com.yusuke.practicerepositorytest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class TestSqlExecutor {

    @Autowired
    private DataSource dataSource;

    /**
     * SQLファイルを実行する
     * 
     * @param sqlFilePath クラスパス上のSQLファイルパス
     */
    public void executeSqlFile(String sqlFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(sqlFilePath);
            String sql = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

            try (Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()) {

                // SQLをセミコロンで分割して実行
                String[] sqlStatements = sql.split(";");
                for (String sqlStatement : sqlStatements) {
                    sqlStatement = sqlStatement.trim();
                    if (!sqlStatement.isEmpty()) {
                        statement.execute(sqlStatement);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("SQLファイルの実行に失敗しました: " + sqlFilePath, e);
        }
    }

    /**
     * ResourceDatabasePopulatorを使用してSQLファイルを実行する
     * 
     * @param sqlFilePath クラスパス上のSQLファイルパス
     */
    public void executeSqlFileWithPopulator(String sqlFilePath) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(sqlFilePath));
        populator.execute(dataSource);
    }
}