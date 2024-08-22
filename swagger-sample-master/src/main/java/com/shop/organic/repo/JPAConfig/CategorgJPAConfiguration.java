package com.shop.organic.repo.JPAConfig;

import java.util.Objects;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import com.shop.organic.entity.car.Car;
import com.shop.organic.entity.category.category;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = category.class, entityManagerFactoryRef = "categoryEntityManagerFactory", transactionManagerRef = "categoryTransactionManager")
public class CategorgJPAConfiguration {

	@Bean(name = "categoryDataSourceProperties")
	@ConfigurationProperties("spring.datasource.category")
	public DataSourceProperties categoryDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "categoryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean categoryEntityManagerFactory(
			@Qualifier("categoryDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource)
				// .packages(category.class)
				.packages("com.shop.organic.entity.category").build();
	}

	@Bean(name = "categoryTransactionManager")
	public PlatformTransactionManager categoryTransactionManager(
			@Qualifier("categoryEntityManagerFactory") LocalContainerEntityManagerFactoryBean categoryEntityManagerFactory) {
		return new JpaTransactionManager(categoryEntityManagerFactory.getObject());
	}

	@Bean(name = "categoryDataSource")
	@ConfigurationProperties("spring.datasource.category.hikari")
	public DataSource categoryDataSource() {
		return categoryDataSourceProperties().initializeDataSourceBuilder().build();
	}

}
