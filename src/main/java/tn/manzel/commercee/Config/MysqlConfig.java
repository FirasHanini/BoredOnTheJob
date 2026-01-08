package tn.manzel.commercee.Config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "tn.manzel.commercee.DAO.Repositories.Mysql", // Dossier dédié aux dépôts Oracle
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager"
)
public class MysqlConfig {

    @Primary // Default DB
    @Bean(name = "oracleDataSource")
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("oracleDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("tn.manzel.commercee.DAO.Entities.Mysql") // Dossier dédié aux entités Oracle
                .persistenceUnit("mysql")
                .build();
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
