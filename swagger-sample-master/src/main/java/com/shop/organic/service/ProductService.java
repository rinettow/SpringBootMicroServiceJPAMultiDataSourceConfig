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
import java.util.Calendar;
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
import javax.persistence.criteria.CriteriaUpdate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.MaterialRequirementDTO;
import com.shop.organic.dto.MaterialRequirementItemsDTO;
import com.shop.organic.dto.MaterialRequirementItemsEstimateDTO;
import com.shop.organic.dto.MaterialSupplierDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProductCategoryDTO;
import com.shop.organic.dto.ProductDTO;
import com.shop.organic.dto.ProductSubCategoryDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.dto.SuppliersEstimates;
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
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.MaterialRequirementItems;
import com.shop.organic.entity.car.MaterialRequirementItemsEstimate;
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
import java.sql.Date;
import java.sql.Timestamp;

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

import java.util.Comparator;
import java.util.Optional;

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

	@Autowired
	private BuilderService builderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MaterialSupplierService materialSupplierService;

	private List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate = new ArrayList<MaterialRequirementItemsEstimate>();

	private enum ResourceType {
		FILE_SYSTEM, CLASSPATH
	}

	@PreDestroy
	public void shutdonw() {
		// needed to avoid resource leak
		threadpoolToGtetAllStates.shutdown();
	}

	public List<ProductCategoryDTO> getAllProductsBasedOnCategory(int productCategorId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<ProductCategory> ProductCategory;
		List<ProductCategoryDTO> ProductCategoryDTO = new ArrayList<ProductCategoryDTO>();
		// ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
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
			ProductCategoryDTO = ProductCategory.stream().map(catg -> this.setProductCategoryDTO(catg))
					.collect(Collectors.toList());
			// productCategoryDTO = this.setProductCategoryDTO(ProductCategory.get(0));
		}
		return ProductCategoryDTO;
	}

	public MaterialRequirement createMaterialRequirement(String customerOrBuilderId, String isCustomerOrBuilder,
			String productCategoryId) {
		MaterialRequirement materialRequirement = new MaterialRequirement();

		if (isCustomerOrBuilder.equals("Customer")) {
			materialRequirement.setCustomerId(Integer.parseInt(customerOrBuilderId));
		} else if (isCustomerOrBuilder.equals("Builder")) {
			materialRequirement.setBuilderId(Integer.parseInt(customerOrBuilderId));
		}
		// materialRequirement.setCustomerId(1);
		materialRequirement.setProductCategoryId(Integer.parseInt(productCategoryId));
		materialRequirement.setRequirementStatus("IN_CART");
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(materialRequirement)) {
			// persist object - add to entity manager
			entityManager.persist(materialRequirement);
			// flush em - save to DB
			entityManager.flush();
		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();

		return materialRequirement;
	}

	public MaterialRequirementItems addItemsToMaterialRequirement(int materialRequirementId, String ProductId,
			String productSubCategoryId, String quantity) {
		MaterialRequirementItems materialRequirementItems = new MaterialRequirementItems();

		materialRequirementItems.setMaterialRequirementId(materialRequirementId);
		materialRequirementItems.setProductId(Integer.parseInt(ProductId));
		materialRequirementItems.setProductSubcategoryId(Integer.parseInt(productSubCategoryId));
		materialRequirementItems.setQuantity(Integer.parseInt(quantity));

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(materialRequirementItems)) {
			// persist object - add to entity manager
			entityManager.persist(materialRequirementItems);
			// flush em - save to DB
			entityManager.flush();
		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();

		return materialRequirementItems;
	}

	// public MaterialRequirement checkOut(MaterialRequirementDTO
	// materialRequirementDTO) {
	public MaterialRequirement checkOut(MaterialRequirementDTO materialRequirementDTO) {

		// String stateQuotesRemoved =state.replace("\"","");
		// String districtQuotesRemoved =district.replace("\"","");
		MaterialRequirement materialRequirementEntity = new MaterialRequirement();
		/*
		 * final Set<String> prop = new HashSet<>(Arrays.asList("materialRequirementId",
		 * "customerId", "builderId", "productCategoryId", "requirementStatus", "state",
		 * "district")); this.copyPicturesBasicDTOToEntity(materialRequirementDTO,
		 * materialRequirementEntity, prop);
		 */

		materialRequirementEntity.setMaterialRequirementId(materialRequirementDTO.getMaterialRequirementId());
		if (materialRequirementDTO.getBuilderId() != null) {
			materialRequirementEntity.setBuilderId(materialRequirementDTO.getBuilderId());
		}
		if (materialRequirementDTO.getCustomerId() != null) {
			materialRequirementEntity.setCustomerId(materialRequirementDTO.getCustomerId());
		}

		materialRequirementEntity.setProductCategoryId(materialRequirementDTO.getProductCategoryId());
		materialRequirementEntity.setState(materialRequirementDTO.getState());
		materialRequirementEntity.setDistrict(materialRequirementDTO.getDistrict());
		materialRequirementEntity.setDoorNumber(null);
		materialRequirementEntity.setStreetFirst(materialRequirementDTO.getStreetFirst());
		materialRequirementEntity.setStreetSecond(null);
		materialRequirementEntity.setLandmark(materialRequirementDTO.getLandmark());
		materialRequirementEntity.setCity(materialRequirementDTO.getCity());
		materialRequirementEntity.setPincode(materialRequirementDTO.getPincode());
		materialRequirementEntity.setCountry("India");
		materialRequirementEntity.setRequirementStatus("OPEN");

		// Create a java.sql.Date object (e.g., representing today's date)
		long currentTimeMillis = System.currentTimeMillis();
		Date sqlDate = new Date(currentTimeMillis);

		// Get the timestamp (long value representing milliseconds)
		long timestampMillis = sqlDate.getTime();

		// Optionally, create a java.sql.Timestamp object from the milliseconds
		Timestamp timestampObject = new Timestamp(timestampMillis);

		System.out.println("java.sql.Date: " + sqlDate);
		System.out.println("Timestamp (milliseconds): " + timestampMillis);
		System.out.println("java.sql.Timestamp object: " + timestampObject);

		materialRequirementEntity.setReqCreatedTimestamp(timestampObject);

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();

		entityManager.merge(materialRequirementEntity);
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();

		return materialRequirementEntity;
	}

	public static void copyPicturesBasicDTOToEntity(MaterialRequirementDTO materialRequirementDTO,
			MaterialRequirement materialRequirementEntity, Set<String> props) {
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(materialRequirementEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(materialRequirementDTO, materialRequirementEntity, excludedProperties);
	}

	public MaterialRequirementDTO fetchCart(String customerOrBuilderId, String isCustomerOrBuilder,
			String productCategoryId) {
		boolean isCartAlreadyAvailable = false;
		List<MaterialRequirementDTO> materialRequirementDTO = new ArrayList<MaterialRequirementDTO>();
		EntityManager entityManager = em.getEntityManager("builder");
		entityManager.getTransaction().begin();

		if (isCustomerOrBuilder.equals("Builder")) {
			List<Builder> LoginBuilder = new ArrayList<Builder>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
			Root<Builder> rootBuilder = criteria.from(Builder.class);
			criteria.select(rootBuilder);

			List<Predicate> restrictions = new ArrayList<Predicate>();
			restrictions.add(builder.equal(rootBuilder.get("builderId"), customerOrBuilderId));

			criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
			TypedQuery<Builder> query = entityManager.createQuery(criteria);
			query.setHint(QueryHints.HINT_CACHEABLE, true);
			query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");

			LoginBuilder = query.getResultList();

			materialRequirementDTO = LoginBuilder.get(0).getMaterialRequirement().stream()
					.filter(matReqremnt -> matReqremnt.getRequirementStatus().equals("IN_CART"))
					.map(matReqremnt -> productService.setMaterialRequirementDTO(matReqremnt))
					.collect(Collectors.toList());

		} else if (isCustomerOrBuilder.equals("Customer")) {
			List<Customer> LoginCustomer = new ArrayList<Customer>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
			Root<Customer> rootBuilder = criteria.from(Customer.class);
			criteria.select(rootBuilder);

			List<Predicate> restrictions = new ArrayList<Predicate>();
			restrictions.add(builder.equal(rootBuilder.get("customerId"), customerOrBuilderId));

			criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
			TypedQuery<Customer> query = entityManager.createQuery(criteria);
			query.setHint(QueryHints.HINT_CACHEABLE, true);
			query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");

			LoginCustomer = query.getResultList();

			materialRequirementDTO = LoginCustomer.get(0).getMaterialRequirement().stream()
					.filter(matReqremnt -> matReqremnt.getRequirementStatus().equals("IN_CART"))
					.map(matReqremnt -> productService.setMaterialRequirementDTO(matReqremnt))
					.collect(Collectors.toList());
		}

		/*
		 * List<MaterialRequirement> materialRequirement = new
		 * ArrayList<MaterialRequirement>();
		 * 
		 * CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		 * CriteriaQuery<MaterialRequirement> criteria =
		 * builder.createQuery(MaterialRequirement.class); Root<MaterialRequirement>
		 * rootBuilder = criteria.from(MaterialRequirement.class);
		 * criteria.select(rootBuilder);
		 * 
		 * List<Predicate> restrictions = new ArrayList<Predicate>(); if
		 * (isCustomerOrBuilder.equals("Customer")) {
		 * restrictions.add(builder.equal(rootBuilder.get("customerId"),
		 * Integer.parseInt(customerOrBuilderId))); } else if
		 * (isCustomerOrBuilder.equals("Builder")) {
		 * restrictions.add(builder.equal(rootBuilder.get("builderId"),
		 * Integer.parseInt(customerOrBuilderId))); }
		 * restrictions.add(builder.equal(rootBuilder.get("productCategoryId"),
		 * Integer.parseInt(productCategoryId)));
		 * restrictions.add(builder.equal(rootBuilder.get("requirementStatus"),
		 * "IN_CART"));
		 * 
		 * criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		 * TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		 * query.setHint(QueryHints.HINT_CACHEABLE, true);
		 * query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		 * materialRequirement = query.getResultList();
		 */

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementDTO.isEmpty()) {
			isCartAlreadyAvailable = true;
			return materialRequirementDTO.get(0);
		}

		return null;
	}

	public boolean CheckIfCartAvailableAlready(String customerOrBuilderId, String isCustomerOrBuilder,
			String productCategoryId) {
		boolean isCartAlreadyAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		if (isCustomerOrBuilder.equals("Customer")) {
			restrictions.add(builder.equal(rootBuilder.get("customerId"), Integer.parseInt(customerOrBuilderId)));
		} else if (isCustomerOrBuilder.equals("Builder")) {
			restrictions.add(builder.equal(rootBuilder.get("builderId"), Integer.parseInt(customerOrBuilderId)));
		}
		restrictions.add(builder.equal(rootBuilder.get("productCategoryId"), Integer.parseInt(productCategoryId)));
		restrictions.add(builder.equal(rootBuilder.get("requirementStatus"), "IN_CART"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirement.isEmpty()) {
			isCartAlreadyAvailable = true;
		}

		return isCartAlreadyAvailable;
	}

	public boolean checkIfOpenMaterialRequirementAvailable(String customerOrBuilderId, String isCustomerOrBuilder,
			String productCategoryId) {
		boolean isOpenRequirementAlreadyAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		if (isCustomerOrBuilder.equals("Customer")) {
			restrictions.add(builder.equal(rootBuilder.get("customerId"), Integer.parseInt(customerOrBuilderId)));
		} else if (isCustomerOrBuilder.equals("Builder")) {
			restrictions.add(builder.equal(rootBuilder.get("builderId"), Integer.parseInt(customerOrBuilderId)));
		}
		restrictions.add(builder.equal(rootBuilder.get("productCategoryId"), Integer.parseInt(productCategoryId)));
		restrictions.add(builder.equal(rootBuilder.get("requirementStatus"), "OPEN"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirement.isEmpty()) {
			isOpenRequirementAlreadyAvailable = true;
		}

		return isOpenRequirementAlreadyAvailable;
	}

	public MaterialRequirement getExistingCartMaterialRequirementId(String customerOrBuilderId,
			String isCustomerOrBuilder, String productCategoryId) {
		boolean isCartAlreadyAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		if (isCustomerOrBuilder.equals("Customer")) {
			restrictions.add(builder.equal(rootBuilder.get("customerId"), Integer.parseInt(customerOrBuilderId)));
		} else if (isCustomerOrBuilder.equals("Builder")) {
			restrictions.add(builder.equal(rootBuilder.get("builderId"), Integer.parseInt(customerOrBuilderId)));
		}
		restrictions.add(builder.equal(rootBuilder.get("productCategoryId"), Integer.parseInt(productCategoryId)));
		restrictions.add(builder.equal(rootBuilder.get("requirementStatus"), "IN_CART"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirement.isEmpty()) {
			isCartAlreadyAvailable = true;
		}

		return materialRequirement.get(0);
	}

	public List<MaterialRequirementItems> getAllItemsByRequirementId(int requirementId) {
		boolean isItemsAvailableForMaterialRequirementId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItems> materialRequirementItems = new ArrayList<MaterialRequirementItems>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItems> criteria = builder.createQuery(MaterialRequirementItems.class);
		Root<MaterialRequirementItems> rootBuilder = criteria.from(MaterialRequirementItems.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), requirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItems> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementItems = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementItems.isEmpty()) {
			isItemsAvailableForMaterialRequirementId = true;
		}

		return materialRequirementItems;
	}

	public List<MaterialRequirementItemsEstimate> getAllItemsEstimatesByRequirementId(int requirementId) {
		boolean isItemsEstimateAvailableForMaterialRequirementId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate = new ArrayList<MaterialRequirementItemsEstimate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItemsEstimate> criteria = builder
				.createQuery(MaterialRequirementItemsEstimate.class);
		Root<MaterialRequirementItemsEstimate> rootBuilder = criteria.from(MaterialRequirementItemsEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), requirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItemsEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementItemsEstimate = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementItemsEstimate.isEmpty()) {
			isItemsEstimateAvailableForMaterialRequirementId = true;
		}

		return materialRequirementItemsEstimate;
	}

	public List<ProductCategory> getCategoryByProductCategoryId(int productCategoryId) {
		boolean isCategoryAvailableForCatgId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<ProductCategory> productCategory = new ArrayList<ProductCategory>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> criteria = builder.createQuery(ProductCategory.class);
		Root<ProductCategory> rootBuilder = criteria.from(ProductCategory.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("productCategoryId"), productCategoryId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<ProductCategory> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		productCategory = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!productCategory.isEmpty()) {
			isCategoryAvailableForCatgId = true;
		}

		return productCategory;
	}

	public List<ProductSubCategory> getSubCategoryByProductSubCategoryId(int productSubCategoryId) {
		boolean isSubCategoryAvailableForSubCatgId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<ProductSubCategory> productSubCategory = new ArrayList<ProductSubCategory>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductSubCategory> criteria = builder.createQuery(ProductSubCategory.class);
		Root<ProductSubCategory> rootBuilder = criteria.from(ProductSubCategory.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("productSubCategoryId"), productSubCategoryId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<ProductSubCategory> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		productSubCategory = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!productSubCategory.isEmpty()) {
			isSubCategoryAvailableForSubCatgId = true;
		}

		return productSubCategory;
	}

	public List<Product> getProductInfoByProducttId(int productId) {
		boolean isProductAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Product> product = new ArrayList<Product>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
		Root<Product> rootBuilder = criteria.from(Product.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("productId"), productId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Product> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		product = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!product.isEmpty()) {
			isProductAvailable = true;
		}

		return product;
	}

	public List<MaterialRequirementItems> getMaterialRequirementItemByItemId(int itemId) {
		boolean ismaterialRequirementItemAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItems> materialRequirementItem = new ArrayList<MaterialRequirementItems>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItems> criteria = builder.createQuery(MaterialRequirementItems.class);
		Root<MaterialRequirementItems> rootBuilder = criteria.from(MaterialRequirementItems.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("materialRequirementItemsId"), itemId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItems> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementItem = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementItem.isEmpty()) {
			ismaterialRequirementItemAvailable = true;
		}

		return materialRequirementItem;
	}

	public MaterialRequirementDTO setMaterialRequirementDTO(MaterialRequirement materialRequirement) {
		MaterialRequirementDTO materialRequirementDTO = new MaterialRequirementDTO();
		List<MaterialRequirementItems> materialRequirementItems = new ArrayList<MaterialRequirementItems>();

		ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
		final Set<String> propCatg = new HashSet<>(Arrays.asList("productCategoryId", "productCategoryName"));
		this.copyProductCategoryBasicEntityToDTO(
				this.getCategoryByProductCategoryId(materialRequirement.getProductCategoryId()).get(0),
				productCategoryDTO, propCatg);
		// this.copyProductCategoryBasicEntityToDTO(materialRequirement.getCategoryForMaterialRequirement(),
		// productCategoryDTO, propCatg);
		materialRequirementDTO.setCategoryForMaterialRequirement(productCategoryDTO);

		materialRequirementItems = this.getAllItemsByRequirementId(materialRequirement.getMaterialRequirementId());
		if (materialRequirementItems != null) {
			// System.out.println("Test"
			// +materialRequirementItems.get(0).getMaterialRequirementId());
			materialRequirementDTO.setMaterialRequirementItems(materialRequirementItems.stream()
					.map(item -> this.setMaterialRequirementItemsDTO(item)).collect(Collectors.toList()));
		}

		materialRequirementItemsEstimate = this
				.getAllItemsEstimatesByRequirementId(materialRequirement.getMaterialRequirementId());
		if (materialRequirementItemsEstimate != null) {
			List<String> supplierIds = new ArrayList<String>();

			materialRequirementItemsEstimate.stream()
					.filter(estimate -> supplierIds.add(String.valueOf(estimate.getMaterialSupplierId())))
					.collect(Collectors.toList());

			List<String> distinctsupplierIds = supplierIds.stream().distinct().collect(Collectors.toList());

			// Map<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>>
			// suppliersEstimate = (Map<MaterialSupplierDTO,
			// List<MaterialRequirementItemsEstimateDTO>>)
			// distinctsupplierIds.stream().map(supplierId->
			// this.setSuppliersEstimate(Integer.valueOf(supplierId),
			// materialRequirementItemsEstimate)).collect(Collectors.toList());

			Map<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>> suppliersEstimate1 = new HashMap<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>>();
			List<SuppliersEstimates> allSuppliersWithEstimate = new ArrayList<SuppliersEstimates>();
			distinctsupplierIds.stream().forEach(supplierId -> {
				List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimateDTO = materialRequirementItemsEstimate
						.stream()
						.filter(estimateEntity -> estimateEntity.getMaterialSupplierId() == Integer.valueOf(supplierId))
						.map(estimateEntity -> materialSupplierService.copyMaterialRequirementItemsEstimateEntityToDTO(
								estimateEntity, new MaterialRequirementItemsEstimateDTO()))
						.collect(Collectors.toList());

				MaterialSupplierDTO materialSupplierDTO = materialSupplierService.setMaterialSupplierDTOWithoutEstimate(
						materialSupplierService.getMaterialSupplierById(Integer.valueOf(supplierId)));
				SuppliersEstimates suppliersEstimates = new SuppliersEstimates();
				suppliersEstimates.setMaterialSupplier(materialSupplierDTO);
				suppliersEstimates.setMaterialRequirementItemsEstimate(materialRequirementItemsEstimateDTO);
				allSuppliersWithEstimate.add(suppliersEstimates);
				suppliersEstimate1.put(materialSupplierDTO, materialRequirementItemsEstimateDTO);
			});

			// materialRequirementDTO.setSuppliersEstimate(suppliersEstimate1);
			materialRequirementDTO.setSuppliersEstimates(allSuppliersWithEstimate);
		}

		if(materialRequirementDTO.getSuppliersEstimates() != null && !materialRequirementDTO.getSuppliersEstimates().isEmpty()) {
			List<MaterialRequirementItemsDTO> materialRequirementItemsBestpriceAdded = materialRequirementDTO.getMaterialRequirementItems()
					.stream().map(item -> this.setBestPriceForItem(item, materialRequirementDTO)).collect(Collectors.toList());
			materialRequirementDTO.setMaterialRequirementItems(materialRequirementItemsBestpriceAdded);
		}
		
		
		Set<String> prop = null;
		if (materialRequirement.getBuilderId() != null) {
			List<Builder> builderEntity = getBuilderByBuilderId(materialRequirement.getBuilderId());
			materialRequirementDTO
					.setBuilderForMaterialRequirement(builderService.setBuilderDTOWithoutProject(builderEntity.get(0)));
			prop = new HashSet<>(Arrays.asList("materialRequirementId", "builderId", "productCategoryId",
					"requirementStatus", "state", "district", "doorNumber", "streetFirst", "streetSecond", "landmark",
					"city", "pincode", "country"));
		} else if (materialRequirement.getCustomerId() != null) {
			List<Customer> customerEntity = getCustomerByCustomerId(materialRequirement.getCustomerId());
			materialRequirementDTO.setCustomerForMaterialRequirement(
					customerService.setCustomerDTOWithoutRequirement(customerEntity.get(0)));
			prop = new HashSet<>(Arrays.asList("materialRequirementId", "customerId", "productCategoryId",
					"requirementStatus", "state", "district", "doorNumber", "streetFirst", "streetSecond", "landmark",
					"city", "pincode", "country"));
		}

		this.copyMaterialRequirementBasicEntityToDTO(materialRequirement, materialRequirementDTO, prop);
		// carDTOList.add(carDTO);
		return materialRequirementDTO;
	}
	
	public MaterialRequirementItemsDTO setBestPriceForItem(MaterialRequirementItemsDTO materialRequirementItemsDTO, MaterialRequirementDTO materialRequirementDTO) {


		List<MaterialRequirementItemsEstimateDTO> eachItemEstimatebyDifferentSupplier = null;
		eachItemEstimatebyDifferentSupplier = materialRequirementDTO.getSuppliersEstimates().stream()
				.map(supplier -> this.getEachItemEstimatebyDifferentSupplier(supplier, materialRequirementItemsDTO))
				.collect(Collectors.toList());
		
		if(eachItemEstimatebyDifferentSupplier != null && !eachItemEstimatebyDifferentSupplier.isEmpty()) {
			Optional<MaterialRequirementItemsEstimateDTO> lowEstimatedPrice = eachItemEstimatebyDifferentSupplier
					.stream().filter(echItmEst->Optional.ofNullable(echItmEst).isPresent() && echItmEst.getTotalPrice() > 0.0).min(Comparator.comparing(MaterialRequirementItemsEstimateDTO::getTotalPrice));
			
			if(lowEstimatedPrice.isPresent()) {
				List<SuppliersEstimates> supplierInfo = materialRequirementDTO.getSuppliersEstimates().stream()
						.filter(supplier -> supplier.getMaterialSupplier().getMaterialSupplierBuilderId() == lowEstimatedPrice.get().getMaterialSupplierId())
						.collect(Collectors.toList());
						materialRequirementItemsDTO.setBestPrice(lowEstimatedPrice.get().getTotalPrice());
						materialRequirementItemsDTO.setBestPriceMaterialSupplier(supplierInfo.get(0).getMaterialSupplier());
			}
			
		}
		
		
        return materialRequirementItemsDTO;
	
	}
	
	public MaterialRequirementItemsEstimateDTO getEachItemEstimatebyDifferentSupplier( SuppliersEstimates supplier, MaterialRequirementItemsDTO materialRequirementItemsDTO) {
		List<MaterialRequirementItemsEstimateDTO> supplierItemFiltered = supplier
				.getMaterialRequirementItemsEstimate().stream().filter(estimate -> estimate
						.getMaterialRequirementItemId() == materialRequirementItemsDTO.getMaterialRequirementItemsId())
				.collect(Collectors.toList());
		if(supplierItemFiltered != null && !supplierItemFiltered.isEmpty()) {
			return supplierItemFiltered.get(0);
		}
		return new MaterialRequirementItemsEstimateDTO();
	}

	public List<Builder> getBuilderByBuilderId(int builderId) {
		boolean isItemsEstimateAvailableForMaterialRequirementId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Builder> builderEntity = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderEntity = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return builderEntity;
	}

	public List<Customer> getCustomerByCustomerId(int customerId) {
		boolean isItemsEstimateAvailableForMaterialRequirementId = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Customer> customerEntity = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("customerId"), customerId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Customer> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerEntity = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return customerEntity;
	}

	public Map<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>> setSuppliersEstimate(int SupplierId,
			List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate) {
		Map<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>> suppliersEstimate = new HashMap<MaterialSupplierDTO, List<MaterialRequirementItemsEstimateDTO>>();

		List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimateDTO = materialRequirementItemsEstimate
				.stream().filter(estimateEntity -> estimateEntity.getMaterialSupplierId() == SupplierId)
				.map(estimateEntity -> materialSupplierService.copyMaterialRequirementItemsEstimateEntityToDTO(
						estimateEntity, new MaterialRequirementItemsEstimateDTO()))
				.collect(Collectors.toList());

		MaterialSupplierDTO materialSupplierDTO = materialSupplierService
				.setMaterialSupplierDTOWithoutEstimate(materialSupplierService.getMaterialSupplierById(SupplierId));
		suppliersEstimate.put(materialSupplierDTO, materialRequirementItemsEstimateDTO);

		return suppliersEstimate;
	}

	public MaterialRequirementItemsDTO setMaterialRequirementItemsDTO(
			MaterialRequirementItems materialRequirementItems) {
		MaterialRequirementItemsDTO materialRequirementItemsDTO = new MaterialRequirementItemsDTO();
		List<Product> product = new ArrayList<Product>();

		product = this.getProductInfoByProducttId(materialRequirementItems.getProductId());
		materialRequirementItemsDTO.setProductForMaterialRequirementItems(this.setProductDTO(product.get(0)));

		final Set<String> prop = new HashSet<>(Arrays.asList("materialRequirementItemsId", "materialRequirementId",
				"productId", "productSubcategoryId", "quantity"));
		this.copyMaterialRequirementItemsBasicEntityToDTO(materialRequirementItems, materialRequirementItemsDTO, prop);
		// carDTOList.add(carDTO);
		return materialRequirementItemsDTO;
	}

	public static void copyMaterialRequirementBasicEntityToDTO(MaterialRequirement materialRequirement,
			MaterialRequirementDTO materialRequirementDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(materialRequirement.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(materialRequirement, materialRequirementDTO, excludedProperties);
	}

	public static void copyProductCategoryBasicEntityToDTO(ProductCategory productCategory,
			ProductCategoryDTO productCategoryDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(productCategory.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(productCategory, productCategoryDTO, excludedProperties);
	}

	public static void copyMaterialRequirementItemsBasicEntityToDTO(MaterialRequirementItems materialRequirementItems,
			MaterialRequirementItemsDTO materialRequirementItemsDTO, Set<String> props) {
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(materialRequirementItems.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(materialRequirementItems, materialRequirementItemsDTO, excludedProperties);
	}

	public ProductCategoryDTO setProductCategoryDTO(ProductCategory productCategory) {
		ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();

		if (productCategory.getProductSubCategory() != null && !productCategory.getProductSubCategory().isEmpty()) {
			productCategoryDTO.setProductSubCategory(productCategory.getProductSubCategory().stream()
					.map(productSubCategories -> setProductSubCategoryDTO(productSubCategories))
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
			productSubCategoryDTO.setProduct(productSubCategory.getProduct().stream().map(product -> {

				ProductDTO ProductDTO = new ProductDTO();
				HttpServletResponse response = null;
				ProductDTO.setSubCategoryForProduct(
						setProductSubCategoryDTOWithoutProduct(product.getSubCategoryForProduct()));
				;
				final Set<String> prop = new HashSet<>(Arrays.asList("productId", "productSubcategoryId", "productName",
						"productDescription", "measuremmentUnit", "quantity", "brandName", "productImagePath"));
				this.copyProductBasicEntityToDTO(product, ProductDTO, prop);

				if (product.getProductImagePath() != null) {
					// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
					// response));
					ServletContext sc = null;
					// InputStream in =
					// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
					InputStream in = null;
					try {
						//Resource resource = this.getFileSystem(product.getProductImagePath(), response);
						Resource resource = null;
						if (resource != null) {
							in = this.getFileSystem(product.getProductImagePath(), response).getInputStream();
							byte[] media = IOUtils.toByteArray(in);
							ProductDTO.setProductImage(media);

						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					 * try { byte[] media = IOUtils.toByteArray(in);
					 * ProductDTO.setProductImage(media); } catch (IOException e) {
					 * e.printStackTrace(); }
					 */

				}
				/*
				 * if(product.getSubCategoryForProduct() != null) {
				 * ProductDTO.setSubCategoryForProduct(this.
				 * setProductSubCategoryDTOWithoutProduct(product.getSubCategoryForProduct()));
				 * }
				 */

				// carDTOList.add(carDTO);
				return ProductDTO;

			}).collect(Collectors.toList()));
		}

		// productSubCategoryDTO.getProduct().subList(1,
		// productSubCategoryDTO.getProduct().size()).clear();

		final Set<String> prop = new HashSet<>(Arrays.asList("productSubCategoryId", "productSubCategoryName"));
		this.copyProductSubCategoryBasicEntityToDTO(productSubCategory, productSubCategoryDTO, prop);
		// carDTOList.add(carDTO);
		return productSubCategoryDTO;
	}
	
	public List<ProductDTO> getProductImageForProduct(List<ProductDTO> productDTO) {
		
		List<ProductDTO> productDTOWithImage = productDTO.stream().map(product -> {

			
			if (product.getProductImagePath() != null) {
				// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
				// response));
				ServletContext sc = null;
				HttpServletResponse response = null;
				// InputStream in =
				// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
				InputStream in = null;
				try {
					Resource resource = this.getFileSystem(product.getProductImagePath(), response);
					if (resource != null) {
						in = this.getFileSystem(product.getProductImagePath(), response).getInputStream();
						byte[] media = IOUtils.toByteArray(in);
						product.setProductImage(media);

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			return product;
			

		}).collect(Collectors.toList());
		
		return productDTOWithImage;
		
	}

	public ProductSubCategoryDTO setProductSubCategoryDTOWithoutProduct(ProductSubCategory productSubCategory) {
		ProductSubCategoryDTO productSubCategoryDTO = new ProductSubCategoryDTO();

		// productSubCategoryDTO.getProduct().subList(1,
		// productSubCategoryDTO.getProduct().size()).clear();

		final Set<String> prop = new HashSet<>(Arrays.asList("productSubCategoryId", "productSubCategoryName"));
		this.copyProductSubCategoryBasicEntityToDTO(productSubCategory, productSubCategoryDTO, prop);
		// carDTOList.add(carDTO);
		return productSubCategoryDTO;
	}

	public ProductDTO setProductDTO(Product product) {
		ProductDTO ProductDTO = new ProductDTO();
		HttpServletResponse response = null;
		ProductDTO.setSubCategoryForProduct(setProductSubCategoryDTOWithoutProduct(
				getSubCategoryByProductSubCategoryId(product.getProductSubcategoryId()).get(0)));
		;
		final Set<String> prop = new HashSet<>(Arrays.asList("productId", "productSubcategoryId", "productName",
				"productDescription", "measuremmentUnit", "quantity", "brandName", "productImagePath"));
		this.copyProductBasicEntityToDTO(product, ProductDTO, prop);

		if (product.getProductImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				Resource resource = this.getFileSystem(product.getProductImagePath(), response);
				if (resource != null) {
					in = this.getFileSystem(product.getProductImagePath(), response).getInputStream();
					byte[] media = IOUtils.toByteArray(in);
					ProductDTO.setProductImage(media);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * try { byte[] media = IOUtils.toByteArray(in);
			 * ProductDTO.setProductImage(media); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */

		}
		/*
		 * if(product.getSubCategoryForProduct() != null) {
		 * ProductDTO.setSubCategoryForProduct(this.
		 * setProductSubCategoryDTOWithoutProduct(product.getSubCategoryForProduct()));
		 * }
		 */

		// carDTOList.add(carDTO);
		return ProductDTO;
	}

	public static void copyProductSubCategoryBasicEntityToDTO(ProductSubCategory productSubCategoryEntity,
			ProductSubCategoryDTO productSubCategoryDTO, Set<String> props) {
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(productSubCategoryEntity.getClass()))
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
			File f = new File(filename);
			if (f.exists() && !f.isDirectory()) {
				resource = new FileSystemResource(filename);
				// System.out.println("ceateImageDirectoryForBuilder2" + resource.exists());
				break;

			}
			break;
		case CLASSPATH:
			resource = new ClassPathResource("data/" + filename);
			break;
		}

		return resource;
	}

}
