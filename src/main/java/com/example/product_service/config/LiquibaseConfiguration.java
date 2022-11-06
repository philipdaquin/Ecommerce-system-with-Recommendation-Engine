package com.example.product_service.config;

import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.Liquibase;
import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class LiquibaseConfiguration {
    
    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private Environment environment;

    @Bean
    public SpringLiquibase liquibase(
        @Qualifier("taskExecutor") Executor executor,
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseProp,
        ObjectProvider<DataSource> dataSource,
        DataSourceProperties dataSourceProperties
    ) {
        
    }
}
