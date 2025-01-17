package com.shop.organic.service;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.jpa.QueryHints;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProductCategoryDTO;
import com.shop.organic.dto.ProductDTO;
import com.shop.organic.dto.ProductSubCategoryDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuilderOtp;
import com.shop.organic.entity.car.BuilderRedRequirements;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.BuildersEstimate;
import com.shop.organic.entity.car.Customer;
import com.shop.organic.entity.car.CustomerOtp;
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.District;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Product;
import com.shop.organic.entity.car.ProductCategory;
import com.shop.organic.entity.car.ProductSubCategory;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.entity.car.Test;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
//import com.shop.organic.service.BuilderService.ResourceType;
import com.shop.organic.util.CreateEntityManager;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileSystemUtils;

import java.net.MalformedURLException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

@Service
@Transactional
//@ConfigurationProperties("application-dev")
public class ProductService {

	private String port;

	@Autowired
	private CreateEntityManager em;

	// @Autowired
	private ExecutorService threadpoolToGtetAllStates;

	@Autowired
	private CustomerService customerService;

	private enum ResourceType {
		FILE_SYSTEM, CLASSPATH
	}

	@PreDestroy
	public void shutdonw() {
		// needed to avoid resource leak
		threadpoolToGtetAllStates.shutdown();
	}

	
	public ProductCategoryDTO getAllProductsBasedOnCategory(int productCategorId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<ProductCategory> ProductCategory;
		ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> criteria = builder.createQuery(ProductCategory.class);
		Root<ProductCategory> rootBuilder = criteria.from(ProductCategory.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("productCategoryId"), productCategorId));
		
		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<ProductCategory> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		ProductCategory = query.getResultList();

		if (!ProductCategory.isEmpty()) {
			productCategoryDTO = this.setProductCategoryDTO(ProductCategory.get(0));
		}
		return productCategoryDTO;
	}
	
	
	public ProductCategoryDTO setProductCategoryDTO(ProductCategory productCategory) {
		ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
		
		if (productCategory.getProductSubCategory() != null && !productCategory.getProductSubCategory().isEmpty()) {
			productCategoryDTO.setProductSubCategory(productCategory.getProductSubCategory().stream().map(productSubCategories -> setProductSubCategoryDTO(productSubCategories))
					.collect(Collectors.toList()));
		}
		
		final Set<String> prop = new HashSet<>(Arrays.asList("productCategoryId", "productCategoryName"));
		this.copyProductCategoryBasicEntityToDTO(productCategory, productCategoryDTO, prop);
		// carDTOList.add(carDTO);
		return productCategoryDTO;
	}
	
	
	public ProductSubCategoryDTO setProductSubCategoryDTO(ProductSubCategory productSubCategory) {
		ProductSubCategoryDTO productSubCategoryDTO = new ProductSubCategoryDTO();
		
		if (productSubCategory.getProduct() != null && !productSubCategory.getProduct().isEmpty()) {
			productSubCategoryDTO.setProduct(productSubCategory.getProduct().stream().map(product -> setProductDTO(product))
					.collect(Collectors.toList()));
		}
		
		productSubCategoryDTO.getProduct().subList(1, productSubCategoryDTO.getProduct().size()).clear();
		
		final Set<String> prop = new HashSet<>(Arrays.asList("productSubCategoryId", "productSubCategoryName"));
		this.copyProductSubCategoryBasicEntityToDTO(productSubCategory, productSubCategoryDTO, prop);
		// carDTOList.add(carDTO);
		return productSubCategoryDTO;
	}
	
	public ProductDTO setProductDTO(Product product) {
		ProductDTO ProductDTO = new ProductDTO();
		HttpServletResponse response = null;
		final Set<String> prop = new HashSet<>(Arrays.asList("productId", "productName", "productDescription", "measuremmentUnit", "quantity", "brandName", "productImagePath"));
		this.copyProductBasicEntityToDTO(product, ProductDTO, prop);
		
		/*if (product.getProductImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(product.getProductImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				ProductDTO.setProductImage(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}*/
		// carDTOList.add(carDTO);
		return ProductDTO;
	}
	
	
	public static void copyProductCategoryBasicEntityToDTO(ProductCategory productCategoryEntity, ProductCategoryDTO productCategoryDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(productCategoryEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(productCategoryEntity, productCategoryDTO, excludedProperties);
	}
	
	public static void copyProductSubCategoryBasicEntityToDTO(ProductSubCategory productSubCategoryEntity, ProductSubCategoryDTO productSubCategoryDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(productSubCategoryEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(productSubCategoryEntity, productSubCategoryDTO, excludedProperties);
	}
	
	public static void copyProductBasicEntityToDTO(Product productEntity, ProductDTO productDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(productEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(productEntity, productDTO, excludedProperties);
	}
	
	
	public Resource getFileSystem(String filename, HttpServletResponse response) {
		return getResource(filename, response, ResourceType.FILE_SYSTEM);
	}
	
	private Resource getResource(String filename, HttpServletResponse response, ResourceType resourceType) {
		// response.setContentType("text/csv; charset=utf-8");
		// response.setHeader("Content-Disposition", "attachment; filename=" +
		// filename);
		// response.setHeader("filename", filename);

		Resource resource = null;
		final String FILE_DIRECTORY = "C:/Users/User/GitHub Repository/BuildersImage/Builder23/Project29/";
		switch (resourceType) {
		case FILE_SYSTEM:
			resource = new FileSystemResource(filename);
			System.out.println("ceateImageDirectoryForBuilder2" + resource.exists());
			break;
		case CLASSPATH:
			resource = new ClassPathResource("data/" + filename);
			break;
		}

		return resource;
	}

}
