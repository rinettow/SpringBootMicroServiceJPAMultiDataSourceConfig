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

import com.shop.organic.entity.car.Car;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackageClasses = Car.class,
  entityManagerFactoryRef = "carEntityManagerFactory",
  transactionManagerRef = "carTransactionManager"
)
public class CarJPAConfiguration {
	
	@Primary
	@Bean(name = "carDataSourceProperties")
	@ConfigurationProperties("spring.datasource.car")
    public DataSourceProperties carDataSourceProperties() {
        return new DataSourceProperties();
    }
	
	@Primary
	@Bean(name = "carEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean carEntityManagerFactory(
			@Qualifier("carDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
        return builder
          .dataSource(dataSource)
          //.packages(Car.class)
          .packages("com.shop.organic.entity.car")
          .build();
    }
	
	 @Primary
	 @Bean(name = "carTransactionManager")
	    public PlatformTransactionManager carTransactionManager(
	      @Qualifier("carEntityManagerFactory") LocalContainerEntityManagerFactoryBean carEntityManagerFactory) {
	        return new JpaTransactionManager(carEntityManagerFactory.getObject());
	    }
	
	@Primary
	@Bean(name = "carDataSource")
	@ConfigurationProperties("spring.datasource.car.hikari")
	public DataSource carDataSource() {
	    return carDataSourceProperties()
	      .initializeDataSourceBuilder()
	      .build();
	}

}
