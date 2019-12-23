package com.example.testbatch.infrastructure;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.testbatch.domain.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Bean
    @Primary
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("batch_test");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lfb = new LocalContainerEntityManagerFactoryBean();
        lfb.setDataSource(getDataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        lfb.setJpaVendorAdapter(vendorAdapter);
        lfb.setPackagesToScan("com.example.testbatch.domain.model");
        lfb.setJpaProperties(hibernateProps());
        return lfb;
    }

    @Bean
    @Primary
    Properties hibernateProps() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }

    @Bean
    @Primary
    JpaTransactionManager transactionManagerJPA() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    
}
