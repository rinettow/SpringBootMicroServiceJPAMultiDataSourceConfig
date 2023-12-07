package com.shop.organic.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.organic.entity.car.Car;

public interface CarRepository extends JpaRepository<Car, Long>{
	Car findByCarId(String carId);
}
