package com.shop.organic.repo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.organic.entity.car.Builder;
//import com.shop.organic.entity.car.Car;

@Configuration
@ConfigurationProperties("application-dev")
public interface BuilderRepository {// extends JpaRepository<Builder, Long>{
	Builder findByBuilderId(String builderId);
}
