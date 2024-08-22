package com.shop.organic.repo.JPAConfig;

import java.util.Objects;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
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

import com.shop.organic.entity.car.Builder;

//import com.shop.organic.entity.car.Car;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Builder.class, entityManagerFactoryRef = "builderEntityManagerFactory", transactionManagerRef = "builderTransactionManager")
//@ConfigurationProperties("application-dev")
public class BuilderJPAConfiguration {

	@Primary
	@Bean(name = "builderDataSourceProperties")
	@ConfigurationProperties("spring.datasource.car")
	public DataSourceProperties builderDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "builderEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean builderEntityManagerFactory(
			@Qualifier("builderDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource)
				// .packages(Car.class)
				.packages("com.shop.organic.entity.car").build();
	}

	@Primary
	@Bean(name = "builderTransactionManager")
	public PlatformTransactionManager builderTransactionManager(
			@Qualifier("builderEntityManagerFactory") LocalContainerEntityManagerFactoryBean builderEntityManagerFactory) {
		return new JpaTransactionManager(builderEntityManagerFactory.getObject());
	}

	@Primary
	@Bean(name = "builderDataSource")
	@ConfigurationProperties("spring.datasource.car.hikari")
	public DataSource builderDataSource() {
		return builderDataSourceProperties().initializeDataSourceBuilder().build();
	}

}
