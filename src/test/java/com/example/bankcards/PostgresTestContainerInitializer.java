package com.example.bankcards;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine");

    static {
        postgres.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url" + postgres.getJdbcUrl(),
                "spring.datasource.username" + postgres.getUsername(),
                "spring.datasource.password"+ postgres.getPassword());
    }

}
