package com.bankstar.recommendation.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean(name = "defaultDataSource")
    @Primary
    public DataSource defaultDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getProperty("spring.datasource.url",
                System.getenv("SPRING_DATASOURCE_URL")));
        config.setUsername(System.getProperty("spring.datasource.username",
                System.getenv("SPRING_DATASOURCE_USERNAME")));
        config.setPassword(System.getProperty("spring.datasource.password",
                System.getenv("SPRING_DATASOURCE_PASSWORD")));
        return new HikariDataSource(config);
    }

    @Bean(name = "rulesDataSource")
    public DataSource rulesDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getProperty("spring.rules.datasource.url",
                System.getenv("SPRING_RULES_DATASOURCE_URL")));
        config.setUsername(System.getProperty("spring.rules.datasource.username",
                System.getenv("SPRING_RULES_DATASOURCE_USERNAME")));
        config.setPassword(System.getProperty("spring.rules.datasource.password",
                System.getenv("SPRING_RULES_DATASOURCE_PASSWORD")));
        return new HikariDataSource(config);
    }

    @Bean(name = "defaultTransactionManager")
    @Primary
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("defaultDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "rulesTransactionManager")
    public PlatformTransactionManager rulesTransactionManager(
            @Qualifier("rulesDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}