package com.shop.organic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.organic.dto.CategoryDTO;
import com.shop.organic.dto.priceDTO;
import com.shop.organic.dto.productDTO;
import com.shop.organic.entity.car.Car;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.repo.categoryRepository;
import com.shop.organic.util.CreateEntityManager;

@Service
@Transactional
public class CategoryService {
	
	//@Autowired
	//private categoryRepository categoryRepository;
	
	@Autowired
	private CreateEntityManager em;
	
	private List<category> categoryEntityList;
	private List<CategoryDTO> catgDTOList=new ArrayList<CategoryDTO>();
	//private List<CategoryDTO> catgDTOList;
	
	public List<CategoryDTO> findCategoryList() {
		//return categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		//categoryEntityList=categoryRepository.findAll();
		
		EntityManager entityManager = em.getEntityManager("category");
		Query q = entityManager.createQuery("SELECT catg FROM category catg", category.class);
	    //q.setParameter("keyword", keyword); //etc
		categoryEntityList= q.getResultList();
		
		catgDTOList.clear();
		int i=0;
		for(category catgEntity: categoryEntityList) {
			i=i+1;
			CategoryDTO catgDTO= new CategoryDTO();
			priceDTO priceDTO= new priceDTO();
			catgDTO.setRowId(i);
			catgDTO.setCategoryId(catgEntity.getCategoryId());
			catgDTO.setCategoryName(catgEntity.getCategoryName());
			catgDTO.setCategoryDesc(catgEntity.getCategoryDesc());
			catgDTO.setPriceId(catgEntity.getPriceId());
			
			priceDTO.setPriceId(catgEntity.getPrice().getPriceId());
			priceDTO.setCHF(catgEntity.getPrice().getCHF());
			priceDTO.setDOLLAR(catgEntity.getPrice().getDOLLAR());
			priceDTO.setEURO(catgEntity.getPrice().getEURO());
			
			catgDTO.setPrice(priceDTO);
			List<productDTO> prodDTOList=new ArrayList<productDTO>();;
			for(product prodEntity: catgEntity.getProducts()) {
				productDTO prodDTO=new productDTO();
				prodDTO.setProductId(prodEntity.getProductId());
				prodDTO.setProductName(prodEntity.getProductName());
				prodDTO.setProductDesc(prodEntity.getProductDesc());
				prodDTO.setQuantity(prodEntity.getQuantity());
				prodDTO.setMeasurementUnit(prodEntity.getMeasurementUnit());
				prodDTO.setCategoryId(prodEntity.getCategoryId());
				prodDTOList.add(prodDTO);
				
			}
			catgDTO.setProducts(prodDTOList);
			catgDTOList.add(catgDTO);
			
		}
		return catgDTOList;
	}
	
	private CategoryDTO copyCategoryEntityToDto(category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		BeanUtils.copyProperties(category, categoryDTO);
		return categoryDTO;
	}

}
