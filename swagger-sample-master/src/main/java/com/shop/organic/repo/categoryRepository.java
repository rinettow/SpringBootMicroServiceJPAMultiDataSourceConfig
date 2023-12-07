package com.shop.organic.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.organic.entity.category.category;

public interface categoryRepository extends JpaRepository<category, Long>{
	
	category findByCategoryId(String categoryId);

}
