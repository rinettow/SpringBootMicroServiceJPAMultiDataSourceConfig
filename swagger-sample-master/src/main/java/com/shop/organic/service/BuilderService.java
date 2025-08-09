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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.MaterialRequirementDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProductBrandDTO;
import com.shop.organic.dto.ProductCategoryDTO;
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
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.MaterialRequirementItemsEstimate;
import com.shop.organic.entity.car.MaterialSupplier;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.ProductBrand;
import com.shop.organic.entity.car.ProductCategory;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.entity.car.Test;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
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
public class BuilderService {

	private String port;

	@Autowired
	private CreateEntityManager em;

	// @Autowired
	private ExecutorService threadpoolToGtetAllStates;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private MaterialSupplierService materialSupplierService;

	@Autowired
	private ProductService productService;

	private BuilderDTO builderDTO1;

	private enum ResourceType {
		FILE_SYSTEM, CLASSPATH
	}

	@PreDestroy
	public void shutdonw() {
		// needed to avoid resource leak
		threadpoolToGtetAllStates.shutdown();
	}

	private List<Builder> builderEntityList;
	private List<BuildersAvailableAmenities> buildersAvailableAmenities;
	// private List<BuilderDTO> builderDTOList = new ArrayList<BuilderDTO>();

	List<String> builderViewedCustomerRequirementIds = null;

	// @Async
	public List<BuilderDTO> findBuildersList(int amenitiesAndSpecificationsId) {
		List<BuilderDTO> response = null;
		EntityManager entityManager = em.getEntityManager("builder");
		/*
		 * builderDTOList.clear(); CriteriaBuilder builder =
		 * entityManager.getCriteriaBuilder(); CriteriaQuery<BuildersAvailableAmenities>
		 * criteria = builder.createQuery(BuildersAvailableAmenities.class);
		 * Root<BuildersAvailableAmenities> rootBuilder =
		 * criteria.from(BuildersAvailableAmenities.class);
		 * criteria.select(rootBuilder);
		 * 
		 * List<Predicate> restrictions = new ArrayList<Predicate>();
		 * restrictions.add(builder.equal(rootBuilder.get("amenitiesAndSpecificationsId"
		 * ), amenitiesAndSpecificationsId));
		 * 
		 * criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		 * TypedQuery<BuildersAvailableAmenities> query =
		 * entityManager.createQuery(criteria); query.setHint(QueryHints.HINT_CACHEABLE,
		 * true); query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		 * buildersAvailableAmenities = query.getResultList();
		 */
		Query q = entityManager.createQuery(
				"SELECT b FROM BuildersAvailableAmenities b WHERE b.amenitiesAndSpecificationsId = :amenitiesAndSpecificationsId",
				BuildersAvailableAmenities.class);
		// q.setParameter(1, builderId);
		q.setParameter("amenitiesAndSpecificationsId", amenitiesAndSpecificationsId);
		buildersAvailableAmenities = q.getResultList();

		if (buildersAvailableAmenities.isEmpty()) {
			throw new ResourceNotFoundException("Builder: " + amenitiesAndSpecificationsId + " not Found...");
		}
		int i = 0;
		/*
		 * response = buildersAvailableAmenities.stream()
		 * .map(buildersAvailableAmenities ->
		 * getBuildersById(buildersAvailableAmenities.getBuilderId()))
		 * .collect(Collectors.toList());
		 */

		response = buildersAvailableAmenities.stream()
				.map(buildersAvailableAmenities -> this.getBuildersById(buildersAvailableAmenities.getBuilderId()))
				// .map(buildersAvailableAmenities ->
				// this.setBuilderDTO(buildersAvailableAmenities.getBuilderForAmenity()))
				.collect(Collectors.toList());

		entityManager.close();

		return response;
	}

	public List<StateDTO> getAllStates() {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<State> allStates = new ArrayList<State>();
		List<StateDTO> allStatesDTO = new ArrayList<StateDTO>();
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<State> cq = cb.createQuery(State.class);
		Root<State> rootEntry = cq.from(State.class);
		CriteriaQuery<State> all = cq.select(rootEntry);
		TypedQuery<State> allQuery = entityManager.createQuery(all);
		allStates = allQuery.getResultList();

		// commit transaction at all
		// entityManager.getTransaction().commit();

		allStatesDTO = allStates.stream().map(state -> setStateDTO(state)).collect(Collectors.toList());
		entityManager.close();
		return allStatesDTO;

	}
	
	public List<ProductCategoryDTO> getAllCategoriesWithBrands() {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<ProductCategory> allProductCategory = new ArrayList<ProductCategory>();
		List<ProductCategoryDTO> allProductCategoryDTO = new ArrayList<ProductCategoryDTO>();
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> cq = cb.createQuery(ProductCategory.class);
		Root<ProductCategory> rootEntry = cq.from(ProductCategory.class);
		CriteriaQuery<ProductCategory> all = cq.select(rootEntry);
		TypedQuery<ProductCategory> allQuery = entityManager.createQuery(all);
		allProductCategory = allQuery.getResultList();

		// commit transaction at all
		// entityManager.getTransaction().commit();

		allProductCategoryDTO = allProductCategory.stream().map(catg -> setProductCategoryWithBrand(catg)).collect(Collectors.toList());
		entityManager.close();
		return allProductCategoryDTO;

	}

	public BuilderDTO registerBuilder(BuilderDTO builderDTO) {
		BuilderDTO responseBuilderDTO = new BuilderDTO();

		Builder builderEntity = setBuilderEntity(builderDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(builderEntity)) {
			// persist object - add to entity manager
			entityManager.persist(builderEntity);
			// flush em - save to DB
			entityManager.flush();
		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseBuilderDTO = this.setBuilderDTO(builderEntity);
		entityManager.close();

		return responseBuilderDTO;
	}

	public void ResetBuilderPassword(BuilderDTO builderDTO) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Builder> builderEntity = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phone"), builderDTO.getPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderEntity = query.getResultList();

		// commit transaction at all
		// entityManager.getTransaction().commit();

		Builder builerToChangePasword = builderEntity.get(0);
		builerToChangePasword.setPassword(builderDTO.getPassword());

		if (!entityManager.contains(builerToChangePasword)) {
			// persist object - add to entity manager
			entityManager.persist(builerToChangePasword);
			// flush em - save to DB
			entityManager.flush();
		} else {
			entityManager.merge(builerToChangePasword);
			entityManager.flush();
		}

		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public void registerBuildersAvailableAminities(BuildersAvailableAmenities buildersAvailableAmenities) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		// if (!entityManager.contains(builderEntity)) {
		// persist object - add to entity manager
		entityManager.persist(buildersAvailableAmenities);
		// flush em - save to DB
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public void registerProjectsAvailableAminities(ProjectsAvailableAmenities projectsAvailableAmenities) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		// if (!entityManager.contains(builderEntity)) {
		// persist object - add to entity manager
		entityManager.persist(projectsAvailableAmenities);
		// flush em - save to DB
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public ProjectsDTO addNewProject(ProjectsDTO projectDTO) {
		ProjectsDTO responseProjectDTO = new ProjectsDTO();

		Projects projectEntity = setProjectEntity(projectDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(projectEntity)) {
			Projects entityAvailableOrNot = entityManager.find(Projects.class, projectEntity.getProjectId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(projectEntity);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(projectEntity);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseProjectDTO = this.setProjectDTO(projectEntity);
		entityManager.close();
		return responseProjectDTO;
	}

	public ProjectsDTO getProjectDetailsById(ProjectsDTO projectDTO) {
		ProjectsDTO projectsDTO = new ProjectsDTO();
		Projects projectEntityResponse = new Projects();

		Projects projectEntity = setProjectEntity(projectDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		projectEntityResponse = entityManager.find(Projects.class, projectEntity.getProjectId());
		entityManager.getTransaction().commit();

		System.out.println("builderEntity.getBuilderId()::::" + projectEntity.getProjectId());
		System.out.println("builderEntity.getAddress().getAddressId())::::" + projectEntity.getBuilderId());

		projectsDTO = setProjectDTO(projectEntityResponse);
		entityManager.close();
		return projectsDTO;
	}

	public PictureDTO addNewPicture(PictureDTO pictureDTO) {
		PictureDTO responsePictureDTO = new PictureDTO();

		Picture pictureEntity = setPictureEntity(pictureDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(pictureEntity)) {
			Picture entityAvailableOrNot = entityManager.find(Picture.class, pictureEntity.getPictureId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(pictureEntity);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(pictureEntity);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responsePictureDTO = this.setPictureDTO(pictureEntity);
		entityManager.close();

		return responsePictureDTO;
	}

	public BuildersEstimateDTO uploadBuildersEstimatePDF(BuildersEstimateDTO buildersEstimateDTO) {
		BuildersEstimate buildersEstimate = new BuildersEstimate();
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimate.setBuildersEstimateId(buildersEstimateDTO.getBuildersEstimateId());
		buildersEstimate.setCustomerRequirementId(buildersEstimateDTO.getCustomerRequirementId());
		buildersEstimate.setBuilderId(buildersEstimateDTO.getBuilderId());
		buildersEstimate.setPerSquareFeetCost(buildersEstimateDTO.getPerSquareFeetCost());
		buildersEstimate.setDetailedEstimateFilePath(buildersEstimateDTO.getDetailedEstimateFilePath());
		buildersEstimate.setCustomerAcceptedDeclined("ON_HOLD");
		buildersEstimate.setProjectCompletionDurationInDays(buildersEstimateDTO.getProjectCompletionDurationInDays());
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(buildersEstimate)) {
			BuildersEstimate entityAvailableOrNot = entityManager.find(BuildersEstimate.class,
					buildersEstimate.getBuildersEstimateId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(buildersEstimate);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(buildersEstimate);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		// responseBuildersEstimateDTO =
		// customerService.setBuilderEstimateDTO(buildersEstimate);
		entityManager.close();
		return responseBuildersEstimateDTO;

	}

	public BuildersEstimateDTO AcceptDeclineQuotation(BuildersEstimateDTO buildersEstimateDTO) {
		BuildersEstimate buildersEstimate = new BuildersEstimate();
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimate.setBuildersEstimateId(buildersEstimateDTO.getBuildersEstimateId());
		buildersEstimate.setCustomerRequirementId(buildersEstimateDTO.getCustomerRequirementId());
		buildersEstimate.setBuilderId(buildersEstimateDTO.getBuilderId());
		buildersEstimate.setPerSquareFeetCost(buildersEstimateDTO.getPerSquareFeetCost());
		buildersEstimate.setDetailedEstimateFilePath(buildersEstimateDTO.getDetailedEstimateFilePath());
		buildersEstimate.setCustomerAcceptedDeclined(buildersEstimateDTO.getCustomerAcceptedDeclined());
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(buildersEstimate)) {
			BuildersEstimate entityAvailableOrNot = entityManager.find(BuildersEstimate.class,
					buildersEstimate.getBuildersEstimateId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(buildersEstimate);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(buildersEstimate);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseBuildersEstimateDTO = customerService.setBuilderEstimateDTO(buildersEstimate);
		entityManager.close();
		return responseBuildersEstimateDTO;

	}

	public void AcceptMaterialQuotation(String supplierId, String materialRequirementId) {
		EntityManager entityManager = em.getEntityManager("builder");
		entityManager.getTransaction().begin();
		List<MaterialRequirementItemsEstimate> materialRequirementEstimate = new ArrayList<MaterialRequirementItemsEstimate>();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItemsEstimate> criteria = builder
				.createQuery(MaterialRequirementItemsEstimate.class);
		Root<MaterialRequirementItemsEstimate> rootBuilder = criteria.from(MaterialRequirementItemsEstimate.class);
		criteria.select(rootBuilder);
		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirementId));
		restrictions.add(builder.equal(rootBuilder.get("materialSupplierId"), supplierId));
		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItemsEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementEstimate = query.getResultList();
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementEstimate.isEmpty()) {
			List<MaterialRequirementItemsEstimate> materialRequirementEstimateApproved = materialRequirementEstimate
					.stream().peek(estimate -> estimate.setCustomerBuilderAceptedDeclined("ACCEPT"))
					.collect(Collectors.toList());
			materialRequirementEstimateApproved.stream()
					.map(estimate -> UpdateMaterialEstimateWithApproveAndDeclinedStatus(estimate))
					.collect(Collectors.toList());
		}

	}

	public MaterialRequirementItemsEstimate UpdateMaterialEstimateWithApproveAndDeclinedStatus(
			MaterialRequirementItemsEstimate materialRequirementItemsEstimate) {
		EntityManager entityManager1 = em.getEntityManager("builder");
		entityManager1.getTransaction().begin();
		if (!entityManager1.contains(materialRequirementItemsEstimate)) {
			MaterialRequirementItemsEstimate entityAvailableOrNot = entityManager1.find(
					MaterialRequirementItemsEstimate.class,
					materialRequirementItemsEstimate.getMaterialRequirementItemEstmtimateId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager1.persist(materialRequirementItemsEstimate);
				// flush em - save to DB
				entityManager1.flush();
			} else {
				entityManager1.merge(materialRequirementItemsEstimate);
			}

		}
		// commit transaction at all
		entityManager1.getTransaction().commit();

		entityManager1.close();
		return materialRequirementItemsEstimate;
	}

	public void deleteBuildersAccount(String builderId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Builder> builderToDelete = new ArrayList<Builder>();

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
		builderToDelete = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!builderToDelete.isEmpty()) {
			List<Builder> deletedBuilder = builderToDelete.stream()
					.peek(delbuilder -> delbuilder.setAccountStatus("DELETED")).collect(Collectors.toList());

			deletedBuilder.stream().map(delbuilder -> deleteBuilder(delbuilder)).collect(Collectors.toList());

		}

	}

	public Builder deleteBuilder(Builder builder) {
		EntityManager entityManager1 = em.getEntityManager("builder");
		entityManager1.getTransaction().begin();
		if (!entityManager1.contains(builder)) {
			Builder entityAvailableOrNot = entityManager1.find(Builder.class, builder.getBuilderId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager1.persist(builder);
				// flush em - save to DB
				entityManager1.flush();
			} else {
				entityManager1.merge(builder);
			}

		}
		// commit transaction at all
		entityManager1.getTransaction().commit();

		entityManager1.close();
		return builder;
	}
	
	
	
	
	
	
	
	
	
	
	
	public void deleteCustomersAccount(String customerId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Customer> customerToDelete = new ArrayList<Customer>();

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
		customerToDelete = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!customerToDelete.isEmpty()) {
			List<Customer> deletedCustomer = customerToDelete.stream()
					.peek(delCustomer -> delCustomer.setAccountStatus("DELETED")).collect(Collectors.toList());

			deletedCustomer.stream().map(delCustomer -> deleteCustomer(delCustomer)).collect(Collectors.toList());

		}

	}

	public Customer deleteCustomer(Customer customer) {
		EntityManager entityManager1 = em.getEntityManager("builder");
		entityManager1.getTransaction().begin();
		if (!entityManager1.contains(customer)) {
			Customer entityAvailableOrNot = entityManager1.find(Customer.class, customer.getCustomerId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager1.persist(customer);
				// flush em - save to DB
				entityManager1.flush();
			} else {
				entityManager1.merge(customer);
			}

		}
		// commit transaction at all
		entityManager1.getTransaction().commit();

		entityManager1.close();
		return customer;
	}
	
	
	public void deleteMaterialSupplierAccount(String suplierId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialSupplier> materialSupplierToDelete = new ArrayList<MaterialSupplier>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialSupplier> criteria = builder.createQuery(MaterialSupplier.class);
		Root<MaterialSupplier> rootBuilder = criteria.from(MaterialSupplier.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialSupplierBuilderId"), suplierId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialSupplier> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialSupplierToDelete = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialSupplierToDelete.isEmpty()) {
			List<MaterialSupplier> deletedSupplier = materialSupplierToDelete.stream()
					.peek(delbuilder -> delbuilder.setAccountStatus("DELETED")).collect(Collectors.toList());

			deletedSupplier.stream().map(delSupplier -> deleteSuplier(delSupplier)).collect(Collectors.toList());

		}

	}

	public MaterialSupplier deleteSuplier(MaterialSupplier supplier) {
		EntityManager entityManager1 = em.getEntityManager("builder");
		entityManager1.getTransaction().begin();
		if (!entityManager1.contains(supplier)) {
			MaterialSupplier entityAvailableOrNot = entityManager1.find(MaterialSupplier.class, supplier.getMaterialSupplierBuilderId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager1.persist(supplier);
				// flush em - save to DB
				entityManager1.flush();
			} else {
				entityManager1.merge(supplier);
			}

		}
		// commit transaction at all
		entityManager1.getTransaction().commit();

		entityManager1.close();
		return supplier;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void DeclineRestAllMaterialQuotation(String supplierId, String materialRequirementId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItemsEstimate> materialRequirementEstimate = new ArrayList<MaterialRequirementItemsEstimate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItemsEstimate> criteria = builder
				.createQuery(MaterialRequirementItemsEstimate.class);
		Root<MaterialRequirementItemsEstimate> rootBuilder = criteria.from(MaterialRequirementItemsEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirementId));
		restrictions.add(builder.notEqual(rootBuilder.get("materialSupplierId"), supplierId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItemsEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementEstimate = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementEstimate.isEmpty()) {
			List<MaterialRequirementItemsEstimate> materialRequirementEstimateDeclined = materialRequirementEstimate
					.stream().peek(estimate -> estimate.setCustomerBuilderAceptedDeclined("DECLINE"))
					.collect(Collectors.toList());

			materialRequirementEstimateDeclined.stream()
					.map(estimate -> UpdateMaterialEstimateWithApproveAndDeclinedStatus(estimate))
					.collect(Collectors.toList());

		}

	}

	public void DeclineAllMaterialQuotationOnCancellationRequest(String materialRequirementId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItemsEstimate> materialRequirementEstimate = new ArrayList<MaterialRequirementItemsEstimate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItemsEstimate> criteria = builder
				.createQuery(MaterialRequirementItemsEstimate.class);
		Root<MaterialRequirementItemsEstimate> rootBuilder = criteria.from(MaterialRequirementItemsEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirementId));
		// restrictions.add(builder.notEqual(rootBuilder.get("materialSupplierId"),
		// supplierId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItemsEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementEstimate = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirementEstimate.isEmpty()) {
			List<MaterialRequirementItemsEstimate> materialRequirementEstimateDeclined = materialRequirementEstimate
					.stream().peek(estimate -> estimate.setCustomerBuilderAceptedDeclined("DECLINE"))
					.collect(Collectors.toList());

			materialRequirementEstimateDeclined.stream()
					.map(estimate -> UpdateMaterialEstimateWithApproveAndDeclinedStatus(estimate))
					.collect(Collectors.toList());

		}

	}

	public void CloseMaterialRequirement(String materialRequirementId) {

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirement.isEmpty()) {
			List<MaterialRequirement> materialRequirementClosed = materialRequirement.stream()
					.peek(req -> req.setRequirementStatus("CLOSED")).collect(Collectors.toList());

			materialRequirementClosed.stream()
					.map(requirementToClose -> CloseOrCancelMaterialRequirement(requirementToClose))
					.collect(Collectors.toList());

		}

	}

	public void CancelMaterialRequirement(String materialRequirementId) {

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		if (!materialRequirement.isEmpty()) {
			List<MaterialRequirement> materialRequirementCancelled = materialRequirement.stream()
					.peek(req -> req.setRequirementStatus("CANCELLED")).collect(Collectors.toList());

			materialRequirementCancelled.stream()
					.map(requirementToCancel -> CloseOrCancelMaterialRequirement(requirementToCancel))
					.collect(Collectors.toList());

		}

	}

	public MaterialRequirement CloseOrCancelMaterialRequirement(MaterialRequirement materialRequirement) {
		EntityManager entityManager1 = em.getEntityManager("builder");

		entityManager1.getTransaction().begin();
		if (!entityManager1.contains(materialRequirement)) {
			MaterialRequirement entityAvailableOrNot = entityManager1.find(MaterialRequirement.class,
					materialRequirement.getMaterialRequirementId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager1.persist(materialRequirement);
				// flush em - save to DB
				entityManager1.flush();
			} else {
				entityManager1.merge(materialRequirement);
			}

		}
		// commit transaction at all
		entityManager1.getTransaction().commit();

		entityManager1.close();
		return materialRequirement;
	}

	public BuildersEstimateDTO SubmitReview(BuildersEstimateDTO buildersEstimateDTO) {
		BuildersEstimate buildersEstimate = new BuildersEstimate();
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimate.setBuildersEstimateId(buildersEstimateDTO.getBuildersEstimateId());
		buildersEstimate.setCustomerRequirementId(buildersEstimateDTO.getCustomerRequirementId());
		buildersEstimate.setBuilderId(buildersEstimateDTO.getBuilderId());
		buildersEstimate.setPerSquareFeetCost(buildersEstimateDTO.getPerSquareFeetCost());
		buildersEstimate.setDetailedEstimateFilePath(buildersEstimateDTO.getDetailedEstimateFilePath());
		buildersEstimate.setCustomerAcceptedDeclined(buildersEstimateDTO.getCustomerAcceptedDeclined());
		buildersEstimate.setCustomerReview(buildersEstimateDTO.getCustomerReview());
		buildersEstimate.setCustomerReviewStarRating(buildersEstimateDTO.getCustomerReviewStarRating());
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(buildersEstimate)) {
			BuildersEstimate entityAvailableOrNot = entityManager.find(BuildersEstimate.class,
					buildersEstimate.getBuildersEstimateId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(buildersEstimate);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(buildersEstimate);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseBuildersEstimateDTO = customerService.setBuilderEstimateDTO(buildersEstimate);
		entityManager.close();
		return responseBuildersEstimateDTO;

	}

	public boolean VerifyIfAnyQuotationAcceptedForRequirement(BuildersEstimateDTO buildersEstimateDTO) {
		List<BuildersEstimate> buildersEstimate;
		List<BuildersEstimate> AcceptedBuildersEstimate = null;
		boolean isAnyQuotationAcceptedForRequirement = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersEstimate> criteria = builder.createQuery(BuildersEstimate.class);
		Root<BuildersEstimate> rootBuilder = criteria.from(BuildersEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"),
				buildersEstimateDTO.getCustomerRequirementId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersEstimate = query.getResultList();

		if (buildersEstimate != null && !buildersEstimate.isEmpty()) {
			AcceptedBuildersEstimate = buildersEstimate.stream()
					.filter(est -> est.getCustomerAcceptedDeclined().equals("ACCEPT")).collect(Collectors.toList());
		}

		if (AcceptedBuildersEstimate.size() > 0) {
			isAnyQuotationAcceptedForRequirement = true;
		}

		entityManager.close();
		return isAnyQuotationAcceptedForRequirement;

	}

	public void DeclineRestAllQuotationsExceptApprovedQuote(BuildersEstimateDTO buildersEstimateDTO) {
		List<BuildersEstimate> buildersEstimate;
		List<BuildersEstimate> onholdBuildersEstimate = null;
		List<BuildersEstimate> toDesclinedBuildersEstimate = null;
		List<BuildersEstimateDTO> declinedBuildersEstimate = null;
		boolean isAnyQuotationAcceptedForRequirement = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersEstimate> criteria = builder.createQuery(BuildersEstimate.class);
		Root<BuildersEstimate> rootBuilder = criteria.from(BuildersEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"),
				buildersEstimateDTO.getCustomerRequirementId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersEstimate = query.getResultList();

		if (buildersEstimate != null && !buildersEstimate.isEmpty()) {
			onholdBuildersEstimate = buildersEstimate.stream()
					.filter(est -> est.getCustomerAcceptedDeclined().equals("ON_HOLD")).collect(Collectors.toList());
		}

		if (onholdBuildersEstimate != null && !onholdBuildersEstimate.isEmpty()) {
			toDesclinedBuildersEstimate = onholdBuildersEstimate.stream()
					.peek(onHoldEst -> onHoldEst.setCustomerAcceptedDeclined("DECLINE")).collect(Collectors.toList());
		}

		if (toDesclinedBuildersEstimate != null && !toDesclinedBuildersEstimate.isEmpty()) {
			declinedBuildersEstimate = toDesclinedBuildersEstimate.stream()
					.map(declineEst -> this.declineRestAllQuotation(declineEst)).collect(Collectors.toList());
		}

		entityManager.close();

	}

	public void closeCustomerRequirement(BuildersEstimateDTO buildersEstimateDTO) {
		List<CustomerRequirement> customerRequirement;
		List<CustomerRequirement> toCloseCustomerRequirement = null;
		List<CustomerRequirement> closedCustomerRequirement;

		boolean isAnyQuotationAcceptedForRequirement = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRequirement> criteria = builder.createQuery(CustomerRequirement.class);
		Root<CustomerRequirement> rootBuilder = criteria.from(CustomerRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"),
				buildersEstimateDTO.getCustomerRequirementId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRequirement = query.getResultList();

		if (customerRequirement != null && !customerRequirement.isEmpty()) {
			toCloseCustomerRequirement = customerRequirement.stream()
					.peek(custReq -> custReq.setRequirementStatus("CLOSED")).collect(Collectors.toList());
		}

		if (toCloseCustomerRequirement != null && !toCloseCustomerRequirement.isEmpty()) {
			closedCustomerRequirement = toCloseCustomerRequirement.stream()
					.map(closeReq -> this.closeCustRequirement(closeReq)).collect(Collectors.toList());
		}

		entityManager.close();

	}

	public CustomerRequirement closeCustRequirement(CustomerRequirement customerRequirement) {
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(customerRequirement)) {
			CustomerRequirement customerRequirementAvailOrNot = entityManager.find(CustomerRequirement.class,
					customerRequirement.getCustomerRequirementId());
			if (customerRequirementAvailOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(customerRequirement);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(customerRequirement);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();
		return customerRequirement;

	}

	public BuildersEstimateDTO declineRestAllQuotation(BuildersEstimate buildersEstimate) {
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(buildersEstimate)) {
			BuildersEstimate entityAvailableOrNot = entityManager.find(BuildersEstimate.class,
					buildersEstimate.getBuildersEstimateId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(buildersEstimate);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(buildersEstimate);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseBuildersEstimateDTO = customerService.setBuilderEstimateDTO(buildersEstimate);
		entityManager.close();
		return responseBuildersEstimateDTO;

	}

	public boolean VerifyIfAnyQuotationAcceptedForRequirement(int customerRequirementId) {
		List<BuildersEstimate> buildersEstimate;
		List<BuildersEstimate> AcceptedBuildersEstimate = null;
		boolean isAnyQuotationAcceptedForRequirement = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersEstimate> criteria = builder.createQuery(BuildersEstimate.class);
		Root<BuildersEstimate> rootBuilder = criteria.from(BuildersEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"), customerRequirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersEstimate = query.getResultList();

		if (buildersEstimate != null && !buildersEstimate.isEmpty()) {
			AcceptedBuildersEstimate = buildersEstimate.stream()
					.filter(est -> est.getCustomerAcceptedDeclined().equals("ACCEPT")).collect(Collectors.toList());
		}

		if (AcceptedBuildersEstimate != null && !AcceptedBuildersEstimate.isEmpty()) {
			if (AcceptedBuildersEstimate.size() > 0) {
				isAnyQuotationAcceptedForRequirement = true;
			}
		}

		entityManager.close();
		return isAnyQuotationAcceptedForRequirement;

	}

	public List<CustomerRequirementDTO> getAllOpenTenders(BuilderDTO builderDTO) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<CustomerRequirement> allOpenRequirements;
		List<CustomerRequirement> allOpenRequirementsFilteredBasedLocation = null;
		List<CustomerRequirement> allOpenRequirementsEstimateNotYetProvided = null;
		List<CustomerRequirement> allOpenRequirementsEstimateNotYetApproved = null;
		List<CustomerRequirementDTO> allOpenRequirementsDTO = null;
		List<CustomerRequirementDTO> allViewedAndUnViewedOpenRequirementsDTO = null;
		boolean isQouteAlreadyRequestedToBuilder = false;

		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRequirement> criteria = builder.createQuery(CustomerRequirement.class);
		Root<CustomerRequirement> rootBuilder = criteria.from(CustomerRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		List<Long> amenityIds = new ArrayList<>();
		for (BuildersAvailableAmenitiesDTO availAmenity : builderDTO.getBuildersAvailableAmenities()) {
			amenityIds.add(Long.valueOf(availAmenity.getAmenitiesAndSpecificationsId()));

		}
		restrictions.add(rootBuilder.get("amenityAndSpecifiactionId").in(amenityIds));
		restrictions.add(builder.equal(rootBuilder.get("requirementStatus"), "OPEN"));
		restrictions.add(builder.equal(rootBuilder.get("state"), builderDTO.getAddress().getState()));
		restrictions.add(builder.equal(rootBuilder.get("district"), builderDTO.getAddress().getDistrict()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		allOpenRequirements = query.getResultList();

		String buildersState = builderDTO.getAddress().getState();
		if (allOpenRequirements != null && !allOpenRequirements.isEmpty()) {
			allOpenRequirementsFilteredBasedLocation = allOpenRequirements.stream()
					.filter(opnReq -> opnReq.getState().equals(buildersState)).collect(Collectors.toList());
		}

		// Verify if Builder provided estimate for this requirement

		if (allOpenRequirementsFilteredBasedLocation != null && !allOpenRequirementsFilteredBasedLocation.isEmpty()) {
			allOpenRequirementsEstimateNotYetProvided = allOpenRequirementsFilteredBasedLocation.stream()
					.filter(requirement -> !validateIfEstimateAlreadyProvidedByBuilder(
							requirement.getCustomerRequirementId(), builderDTO.getBuilderId()))
					.collect(Collectors.toList());

		}

		// Verify if any esimate approved for this requirement...

		if (allOpenRequirementsEstimateNotYetProvided != null && !allOpenRequirementsEstimateNotYetProvided.isEmpty()) {
			allOpenRequirementsEstimateNotYetApproved = allOpenRequirementsEstimateNotYetProvided.stream().filter(
					estNotProv -> !VerifyIfAnyQuotationAcceptedForRequirement(estNotProv.getCustomerRequirementId()))
					.collect(Collectors.toList());
		}

		if (allOpenRequirementsEstimateNotYetApproved != null && !allOpenRequirementsEstimateNotYetApproved.isEmpty()) {
			allOpenRequirementsDTO = allOpenRequirementsEstimateNotYetApproved.stream()
					.map(req -> customerService.setCustomerRequirementDTO(req)).collect(Collectors.toList());
		}

		if (allOpenRequirementsDTO != null && !allOpenRequirementsDTO.isEmpty()) {
			int buildersId = builderDTO.getBuilderId();
			List<BuilderRedRequirements> builderRedRequirements = this.getAllViewedRequirementsForBuilder(builderDTO);

			builderViewedCustomerRequirementIds = builderRedRequirements.stream()
					.map(buldRedReq -> this.getBuilderViewedCustomerRequirementId(buldRedReq))
					.collect(Collectors.toList());

			allViewedAndUnViewedOpenRequirementsDTO = allOpenRequirementsDTO.stream().map(opnReqDTO -> this
					.markCustomerRequirementViewedOrnot(opnReqDTO, builderViewedCustomerRequirementIds))
					.collect(Collectors.toList());

		}

		entityManager.close();
		return allViewedAndUnViewedOpenRequirementsDTO;
	}

	public String getBuilderViewedCustomerRequirementId(BuilderRedRequirements builderRedRequirements) {
		return String.valueOf(builderRedRequirements.getCustomerRequirementId());
	}

	public CustomerRequirementDTO markCustomerRequirementViewedOrnot(CustomerRequirementDTO customerRequirementDTO,
			List<String> builderViewedCustomerRequirementIds) {
		// List<String> builderViewedCustomerRequirementIds = null;
		if (builderViewedCustomerRequirementIds != null) {
			if (builderViewedCustomerRequirementIds
					.contains(String.valueOf(customerRequirementDTO.getCustomerRequirementId()))) {
				customerRequirementDTO.setIsRequirementViewedByBuilder("Viewed");
			} else {
				customerRequirementDTO.setIsRequirementViewedByBuilder("New");
			}
		} else {
			customerRequirementDTO.setIsRequirementViewedByBuilder("New");
		}

		return customerRequirementDTO;
	}

	public List<BuilderRedRequirements> getAllViewedRequirementsForBuilder(BuilderDTO builderDTO) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<BuilderRedRequirements> builderRedRequirements;
		String isBuilderRedCustomerRequirementAlready = "New";
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuilderRedRequirements> criteria = builder.createQuery(BuilderRedRequirements.class);
		Root<BuilderRedRequirements> rootBuilder = criteria.from(BuilderRedRequirements.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderDTO.getBuilderId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuilderRedRequirements> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderRedRequirements = query.getResultList();

		return builderRedRequirements;
	}

	public boolean validateIfEstimateAlreadyProvidedByBuilder(int customerRequirementId, int builderId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<BuildersEstimate> buildersEstimate;
		boolean isEstimateAlreadyProvidedByBuilder = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersEstimate> criteria = builder.createQuery(BuildersEstimate.class);
		Root<BuildersEstimate> rootBuilder = criteria.from(BuildersEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"), customerRequirementId));
		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersEstimate = query.getResultList();

		if (!buildersEstimate.isEmpty()) {
			isEstimateAlreadyProvidedByBuilder = true;
		} else {
			isEstimateAlreadyProvidedByBuilder = false;
		}
		return isEstimateAlreadyProvidedByBuilder;
	}

	public boolean VerifyIfBuilderRedCustomerRequirementAlready(int customerRequirementId, int builderId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<BuilderRedRequirements> builderRedRequirements;
		boolean isBuilderRedCustomerRequirementAlready = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuilderRedRequirements> criteria = builder.createQuery(BuilderRedRequirements.class);
		Root<BuilderRedRequirements> rootBuilder = criteria.from(BuilderRedRequirements.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"), customerRequirementId));
		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuilderRedRequirements> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderRedRequirements = query.getResultList();

		if (!builderRedRequirements.isEmpty()) {
			isBuilderRedCustomerRequirementAlready = true;
		} else {
			isBuilderRedCustomerRequirementAlready = false;
		}
		return isBuilderRedCustomerRequirementAlready;
	}

	public void builderRedCustomerRequirementEntry(int customerRequirementId, int builderId) {
		BuilderRedRequirements builderRedRequirements = new BuilderRedRequirements();
		builderRedRequirements.setBuilderId(builderId);
		builderRedRequirements.setCustomerRequirementId(customerRequirementId);

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(builderRedRequirements)) {
			// persist object - add to entity manager
			entityManager.persist(builderRedRequirements);
			// flush em - save to DB
			entityManager.flush();

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();
	}

	public void ceateImageDirectoryForBuilder(BuilderDTO builderDTO) {
		System.out.println("ceateImageDirectoryForBuilder");

		String path = "C:/Users/User/GitHub Repository/BuildersImage/";
		String finalPath = path.concat("Builder").concat(Integer.toString(builderDTO.getBuilderId()));
		if (!new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForBuilder2");
			new File(finalPath).mkdir();
		}
		System.out.println("realPathtoUploads = {}" + finalPath);
	}

	public void ceateImageDirectoryForProject(ProjectsDTO projectDTO, MultipartFile multipartFile) {
		System.out.println("ceateImageDirectoryForBuilder");

		String path = "C:/Users/User/GitHub Repository/BuildersImage/";
		String finalPath = path.concat("Builder").concat(Integer.toString(projectDTO.getBuilderId())).concat("/")
				.concat("Project").concat(Integer.toString(projectDTO.getProjectId()));
		if (!new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForBuilder2");
			new File(finalPath).mkdir();
			try {
				String fileName = multipartFile.getOriginalFilename();
				multipartFile.transferTo(new File(finalPath.concat("/").concat(fileName)));
				String extension = fileName.substring(fileName.lastIndexOf("."));
				if (extension.equalsIgnoreCase("mp4")) {
					projectDTO.setProjMainVideoFilePath(finalPath.concat("/").concat(fileName));
				} else {
					projectDTO.setProjMainPicFilePath(finalPath.concat("/").concat(fileName));
				}

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String childImagePath = path.concat("Builder").concat(Integer.toString(projectDTO.getBuilderId())).concat("/")
				.concat("Project").concat(Integer.toString(projectDTO.getProjectId())).concat("/ChildImages");
		if (!new File(childImagePath).exists()) {
			System.out.println("ceateImageDirectoryForChildImage");
			new File(childImagePath).mkdir();
		}
		System.out.println("realPathtoUploads = {}" + finalPath);

	}

	public void ceateImageDirectoryForPicture(PictureDTO pictureDTO, String builderId, MultipartFile multipartFile) {
		System.out.println("ceateImageDirectoryForBuilder");

		String path = "C:/Users/User/GitHub Repository/BuildersImage/";
		String finalPath = path.concat("Builder").concat(builderId).concat("/").concat("Project")
				.concat(Integer.toString(pictureDTO.getProjectId())).concat("/").concat("ChildImages");
		if (new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForBuilder2");
			try {
				String fileName = multipartFile.getOriginalFilename();
				multipartFile.transferTo(new File(finalPath.concat("/").concat(fileName)));
				pictureDTO.setPictureFilePath(finalPath.concat("/").concat(fileName));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void ceateImageDirectoryForBuildersEstimate(BuildersEstimateDTO buildersEstimateDTO,
			MultipartFile multipartFile, String customerId) {
		System.out.println("ceateImageDirectoryForBuilder");

		String path = "C:/Users/User/GitHub Repository/CustomersImage/";
		String finalPath = path.concat("Customer").concat(customerId).concat("/").concat("CustomerRequirement")
				.concat(Integer.toString(buildersEstimateDTO.getCustomerRequirementId()));

		if (new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForBuilder2");
			try {
				String fileName = multipartFile.getOriginalFilename();
				multipartFile.transferTo(new File(finalPath.concat("/").concat(fileName)));
				buildersEstimateDTO.setDetailedEstimateFilePath(finalPath.concat("/").concat(fileName));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public Map<String, Object> sendOTP(String Phone, String Password) {
		BuilderDTO LoginBuilderDTO = new BuilderDTO();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Builder> LoginBuilder = new ArrayList<Builder>();
		String responseStatus = null;

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Builder> loginBuilder = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phone"), Phone));
		restrictions.add(builder.isNull(rootBuilder.get("accountStatus")));
		//restrictions.add(builder.notEqual(rootBuilder.get("accountStatus"), "DELETED"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");

		/*EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery(
				"SELECT b FROM Builder b WHERE b.phone = :phone and b.accountStatus <> :accountStatus", Builder.class);
		q.setParameter("phone", Phone);
		q.setParameter("accountStatus", "DELETED");*/
		// q.setParameter("keyword", keyword); //etc
		loginBuilder = query.getResultList();

		if (loginBuilder != null && !loginBuilder.isEmpty()) {
			LoginBuilder = loginBuilder.stream().filter(loggedInBuilder -> loggedInBuilder.getPhone().equalsIgnoreCase(Phone))
					.collect(Collectors.toList());
		}

		if (LoginBuilder.isEmpty() && LoginBuilder.size() == 0) {
			// throw new ResourceNotFoundException("Mobile Number: " + builderDTO.getPhone()
			// + " not Registered...");
			responseStatus = "Builder Mobile Not Registered";
		}

		if (!LoginBuilder.isEmpty()) {

			if (verifyBuilderPassword(LoginBuilder.get(0), Password)) {
				responseStatus = "Success";
				LoginBuilderDTO = setBuilderDTO(LoginBuilder.get(0));
			} else {
				responseStatus = "Incorrect Password";
			}
		}
		entityManager.close();
		response.put("responseStatus", responseStatus);
		response.put("loggedinBuilder", LoginBuilderDTO);
		// System.out.println("LoginBuilderDTO" +new Gson().toJson(LoginBuilderDTO));
		return response;
	}

	public Builder getBuilderByBuilderId(int builderId) {
		List<Builder> builder = new ArrayList<Builder>();
		String responseStatus = null;
		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT b FROM Builder b WHERE b.builderId = :builderId", Builder.class);
		q.setParameter("builderId", builderId);
		// q.setParameter("keyword", keyword); //etc
		builder = q.getResultList();

		return builder.get(0);
	}

	public boolean verifyBuilderPassword(Builder loggedinBuilder, String password) {
		if (loggedinBuilder.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}

	}

	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsById(int amenitiesAndSpecificationsId) {
		AmenitiesAndSpecifications amenitiesAndSpecifications = new AmenitiesAndSpecifications();

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		amenitiesAndSpecifications = entityManager.find(AmenitiesAndSpecifications.class, amenitiesAndSpecificationsId);

		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();
		return amenitiesAndSpecifications;
	}

	public BuilderDTO getBuildersById(int builderId) {

		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT b FROM Builder b WHERE b.builderId = :builderId", Builder.class);
		q.setParameter("builderId", builderId);
		// q.setParameter("keyword", keyword); //etc
		builderEntityList = q.getResultList();

		entityManager.close();

		/*
		 * Builder builderEntity = new Builder(); EntityManager entityManager =
		 * em.getEntityManager("builder"); Query q = entityManager.
		 * createQuery("SELECT b FROM Builder b WHERE b.builderId = :builderId",
		 * Builder.class); q.setParameter("builderId", builderId); builderEntity =
		 * (Builder) q.getSingleResult();
		 */

		builderDTO1 = setBuilderDTOWithoutProject(builderEntityList.get(0));
		entityManager.close();

		return builderDTO1;
	}

	public Builder setBuilderEntity(BuilderDTO builderDTO) {
		Builder builderEntity = new Builder();

		builderEntity.setAddress(this.copyAddressDTOToEntity(builderDTO.getAddress()));

		final Set<String> prop = new HashSet<>(
				Arrays.asList("builderName", "manufacturingCompany", "projectType", "phone", "userName", "password"));
		this.copyBuilderBasicDTOToEntity(builderDTO, builderEntity, prop);
		// builderEntity.setBuildersAvailableAmenities(builderDTO.getBuildersAvailableAmenities().stream().map(buildersAvailableAmenitiesDTO
		// ->
		// this.copyBuildersBasicAvailableAmenitiesDTOToEntity(buildersAvailableAmenitiesDTO,
		// new BuildersAvailableAmenities())).collect(Collectors.toList()));

		return builderEntity;
	}

	public Projects setProjectEntity(ProjectsDTO projectDTO) {
		Projects projectEntity = new Projects();
		// projectEntity.setProjectsAvailableAmenities(projectDTO.getProjectsAvailableAmenities().stream().map(proJAvailAnemity
		// -> copyProjectsBasicAvailableAmenitiesDTOToEntity(proJAvailAnemity, new
		// ProjectsAvailableAmenities())).collect(Collectors.toList()));
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "builderId", "estimateCost",
				"areaInSquareFeet", "projMainPicFilePath", "projMainVideoFilePath"));
		this.copyProjectsBasicDTOToEntity(projectDTO, projectEntity, prop);

		return projectEntity;
	}

	public static ProjectsAvailableAmenities copyProjectsBasicAvailableAmenitiesDTOToEntity(
			ProjectsAvailableAmenitiesDTO projectsAvailableAmenitiesDTO,
			ProjectsAvailableAmenities ProjectsAvailableAmenitiesEntity) throws BeansException {
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "amenitiesAndSpecificationsId"));

		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(ProjectsAvailableAmenitiesEntity.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(projectsAvailableAmenitiesDTO, ProjectsAvailableAmenitiesEntity, excludedProperties);
		return ProjectsAvailableAmenitiesEntity;
	}

	public Picture setPictureEntity(PictureDTO picturetDTO) {
		Picture pictureEntity = new Picture();
		final Set<String> prop = new HashSet<>(Arrays.asList("pictureId", "projectId", "pictureFilePath",
				"videoFilePath", "roomType", "roomDescription", "materialBrand", "paintBrand", "plumbingBrand",
				"electricalBrand", "cementBrand", "steelBrand"));
		this.copyPicturesBasicDTOToEntity(picturetDTO, pictureEntity, prop);

		return pictureEntity;
	}

	// @Async
	public BuilderDTO setBuilderDTO(Builder builderEntity) {
		BuilderDTO builderDTO = new BuilderDTO();
		Address address = builderEntity.getAddress();
		List<Address> builderAddress = GetBuilderAddressByAddressId(address.getAddressId());
		AddressDTO addressDTO = this.copyAddressBasicEntityToDto(builderAddress.get(0));
		builderDTO.setAddress(addressDTO);

		List<Projects> builderProjects = this.GetAllProjectsByBuilderId(builderEntity.getBuilderId());
		if (builderProjects != null && !builderProjects.isEmpty()) {
			builderDTO.setProjects(
					builderProjects.stream().map(project -> setProjectDTO(project)).collect(Collectors.toList()));
		}
		// List<BuildersAvailableAmenities> buildersAvailableAmenities=
		// getAllBuildersAvaiableAmenitiesByBuilderid(builderEntity.getBuilderId());
		List<BuildersAvailableAmenities> builderAvailableAmenitiesById = this
				.GetBuildersAvailableAmenitiesBuilderId(builderEntity.getBuilderId());
		if (builderAvailableAmenitiesById != null && !builderAvailableAmenitiesById.isEmpty()) {
			builderDTO.setBuildersAvailableAmenities(builderAvailableAmenitiesById.stream()
					.map(builderAvailableAmenities -> this.copyBuildersBasicAvailableAmenitiesEntityToDTO(
							builderAvailableAmenities, new BuildersAvailableAmenitiesDTO()))
					.collect(Collectors.toList()));
		}

		List<BuildersEstimate> buildersEstimates = this.GetBuildersEstimatesByBuilderId(builderEntity.getBuilderId());
		if (buildersEstimates != null && !buildersEstimates.isEmpty()) {
			builderDTO.setBuildersEstimate(buildersEstimates.stream()
					.map(estimate -> customerService.setBuilderEstimateDTObymanualCustomerRequirementPicking(estimate))
					.collect(Collectors.toList()));
		}

		List<MaterialRequirement> materialRequirements = GetMaterialRequirementByBuilderId(
				builderEntity.getBuilderId());
		if (materialRequirements != null && !materialRequirements.isEmpty()) {
			builderDTO.setMaterialRequirement(materialRequirements.stream()
					.map(materialRequirement -> productService.setMaterialRequirementDTO(materialRequirement))
					.collect(Collectors.toList()));

		}

		/*
		 * List<String> materialRequirementIds = new ArrayList<String>();
		 * builderEntity.getMaterialRequirement() .stream().filter(estimate ->
		 * materialRequirementIds.add(String.valueOf(estimate.getMaterialRequirementId()
		 * ) )).collect(Collectors.toList());
		 * 
		 * List<String> distinctMaterialRequirementIds = materialRequirementIds.stream()
		 * .distinct() .collect(Collectors.toList());
		 */

		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "builderName", "manufacturingCompany",
				"projectType", "phone", "userName", "password", "amenityAndSpecificationId"));
		this.copyBuilderBasicEntityToDTO(builderEntity, builderDTO, prop);
		// carDTOList.add(carDTO);
		return builderDTO;
	}

	public List<MaterialRequirement> GetMaterialRequirementByBuilderId(int builderId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirements = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirements = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return materialRequirements;
	}

	public List<Address> GetBuilderAddressByAddressId(int addressId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Address> address = new ArrayList<Address>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Address> criteria = builder.createQuery(Address.class);
		Root<Address> rootBuilder = criteria.from(Address.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("addressId"), addressId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Address> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		address = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return address;
	}

	public List<BuildersEstimate> GetBuildersEstimatesByBuilderId(int builderId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<BuildersEstimate> buildersEstimates = new ArrayList<BuildersEstimate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersEstimate> criteria = builder.createQuery(BuildersEstimate.class);
		Root<BuildersEstimate> rootBuilder = criteria.from(BuildersEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersEstimates = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return buildersEstimates;
	}

	public List<BuildersAvailableAmenities> GetBuildersAvailableAmenitiesBuilderId(int builderId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<BuildersAvailableAmenities> buildersAvailableAmenities = new ArrayList<BuildersAvailableAmenities>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersAvailableAmenities> criteria = builder.createQuery(BuildersAvailableAmenities.class);
		Root<BuildersAvailableAmenities> rootBuilder = criteria.from(BuildersAvailableAmenities.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersAvailableAmenities> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersAvailableAmenities = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return buildersAvailableAmenities;
	}

	public List<Projects> GetAllProjectsByBuilderId(int builderId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Projects> projects = new ArrayList<Projects>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Projects> criteria = builder.createQuery(Projects.class);
		Root<Projects> rootBuilder = criteria.from(Projects.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("builderId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Projects> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		projects = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return projects;
	}

	public BuilderDTO setBuilderDTOWithoutProject(Builder builderEntity) {
		BuilderDTO builderDTO = new BuilderDTO();
		builderDTO.setAddress(this.copyAddressEntityToDto(builderEntity.getAddress()));
		final Set<String> prop = new HashSet<>(
				Arrays.asList("builderId", "builderName", "projectType", "manufacturingCompany", "phone"));
		this.copyBuilderBasicEntityToDTO(builderEntity, builderDTO, prop);
		// carDTOList.add(carDTO);
		return builderDTO;
	}

	public BuilderDTO setBuilderDTOForCustomer(Builder builderEntity) {
		BuilderDTO builderDTO = new BuilderDTO();
		Address address = builderEntity.getAddress();
		List<Address> builderAddress = GetBuilderAddressByAddressId(address.getAddressId());
		AddressDTO addressDTO = this.copyAddressBasicEntityToDto(builderAddress.get(0));
		builderDTO.setAddress(addressDTO);

		List<Projects> builderProjects = this.GetAllProjectsByBuilderId(builderEntity.getBuilderId());
		if (builderProjects != null && !builderProjects.isEmpty()) {
			builderDTO.setProjects(
					builderProjects.stream().map(project -> setProjectDTO(project)).collect(Collectors.toList()));
		}
		// List<BuildersAvailableAmenities> buildersAvailableAmenities=
		// getAllBuildersAvaiableAmenitiesByBuilderid(builderEntity.getBuilderId());
		List<BuildersAvailableAmenities> builderAvailableAmenitiesById = this
				.GetBuildersAvailableAmenitiesBuilderId(builderEntity.getBuilderId());
		if (builderAvailableAmenitiesById != null && !builderAvailableAmenitiesById.isEmpty()) {
			builderDTO.setBuildersAvailableAmenities(builderAvailableAmenitiesById.stream()
					.map(builderAvailableAmenities -> this.copyBuildersBasicAvailableAmenitiesEntityToDTO(
							builderAvailableAmenities, new BuildersAvailableAmenitiesDTO()))
					.collect(Collectors.toList()));
		}

		List<BuildersEstimate> buildersEstimates = this.GetBuildersEstimatesByBuilderId(builderEntity.getBuilderId());
		if (buildersEstimates != null && !buildersEstimates.isEmpty()) {
			builderDTO.setBuildersEstimate(buildersEstimates.stream()
					.map(estimate -> customerService.setBuilderEstimateDTObymanualCustomerRequirementPicking(estimate))
					.collect(Collectors.toList()));
		}

		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "builderName", "manufacturingCompany",
				"projectType", "phone", "userName", "password", "amenityAndSpecificationId"));
		this.copyBuilderBasicEntityToDTO(builderEntity, builderDTO, prop);
		// carDTOList.add(carDTO);
		return builderDTO;
	}

	// @Async
	public ProjectsDTO setProjectDTO(Projects projectEntity) {
		ProjectsDTO projectDTO = new ProjectsDTO();
		HttpServletResponse response = null;
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "builderId", "amenitiesAndSpecificationsId",
				"estimateCost", "areaInSquareFeet", "projMainPicFilePath", "projMainVideoFilePath"));
		this.copyProjectsBasicEntityToDTO(projectEntity, projectDTO, prop);
		if (projectEntity.getProjMainPicFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(projectEntity.getProjMainPicFilePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				projectDTO.setImage(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/*
		 * if (projectEntity.getProjMainVideoFilePath() != null) { //
		 * projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath()
		 * , // response)); ServletContext sc = null; // InputStream in = //
		 * sc.getResourceAsStream(projectEntity.getProjMainPicFilePath()); InputStream
		 * in = null; try { in =
		 * this.getFileSystem(projectEntity.getProjMainVideoFilePath(),
		 * response).getInputStream(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } try { byte[] media =
		 * IOUtils.toByteArray(in); projectDTO.setImage(media); } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */
		/*
		 * if (projectEntity.getBuilderForProjects() != null) {
		 * projectDTO.setBuilder(setBuilderDTOWithoutProject(projectEntity.
		 * getBuilderForProjects())); }
		 */

		List<Picture> projectPictures = this.GetProjectPicturesByProjectId(projectEntity.getProjectId());
		if (projectPictures != null && !projectPictures.isEmpty()) {
			projectDTO.setPicture(projectPictures.stream().map(this::setPictureDTO).collect(Collectors.toList()));
		}

		List<ProjectsAvailableAmenities> projectsAvailableAmenities = this
				.GetProjectAvailableAmenitiesByProjectId(projectEntity.getProjectId());
		if (projectsAvailableAmenities != null) {
			projectDTO.setProjectsAvailableAmenities(projectsAvailableAmenities.stream()
					.map(projectsAvailableEntities -> this.copyProjectsBasicAvailableAmenitiesEntityToDTO(
							projectsAvailableEntities, new ProjectsAvailableAmenitiesDTO()))
					.collect(Collectors.toList()));
		}

		// carDTOList.add(carDTO);
		return projectDTO;
	}

	public List<Picture> GetProjectPicturesByProjectId(int projectId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Picture> pictures = new ArrayList<Picture>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Picture> criteria = builder.createQuery(Picture.class);
		Root<Picture> rootBuilder = criteria.from(Picture.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("projectId"), projectId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Picture> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		pictures = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return pictures;
	}

	public List<ProjectsAvailableAmenities> GetProjectAvailableAmenitiesByProjectId(int projectId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<ProjectsAvailableAmenities> projectsAvailableAmenities = new ArrayList<ProjectsAvailableAmenities>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProjectsAvailableAmenities> criteria = builder.createQuery(ProjectsAvailableAmenities.class);
		Root<ProjectsAvailableAmenities> rootBuilder = criteria.from(ProjectsAvailableAmenities.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		restrictions.add(builder.equal(rootBuilder.get("projectId"), projectId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<ProjectsAvailableAmenities> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		projectsAvailableAmenities = query.getResultList();

		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();

		return projectsAvailableAmenities;
	}

	// @Async
	public PictureDTO setPictureDTO(Picture pictureEntity) {
		PictureDTO pictureDTO = new PictureDTO();
		HttpServletResponse response = null;
		final Set<String> prop = new HashSet<>(Arrays.asList("pictureId", "projectId", "pictureFilePath",
				"videoFilePath", "roomType", "roomDescription", "materialBrand", "paintBrand", "plumbingBrand",
				"electricalBrand", "cementBrand", "steelBrand"));
		this.copyPicturesBasicEntityToDTO(pictureEntity, pictureDTO, prop);
		if (pictureEntity.getPictureFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(pictureEntity.getPictureFilePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				pictureDTO.setPicture(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// carDTOList.add(carDTO);
		return pictureDTO;
	}

	public StateDTO setStateDTO(State stateEntity) {
		threadpoolToGtetAllStates = Executors.newCachedThreadPool();
		Future<StateDTO> futureTask = threadpoolToGtetAllStates.submit(() -> setStateDTOThreadExecution(stateEntity));
		StateDTO response = null;
		try {
			response = futureTask.get();
			threadpoolToGtetAllStates.shutdown();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public ProductCategoryDTO setProductCategoryWithBrand(ProductCategory productCategory) {
		threadpoolToGtetAllStates = Executors.newCachedThreadPool();
		Future<ProductCategoryDTO> futureTask = threadpoolToGtetAllStates.submit(() -> setProductCategoryWithBrandDTOThreadExecution(productCategory));
		ProductCategoryDTO response = null;
		try {
			response = futureTask.get();
			threadpoolToGtetAllStates.shutdown();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public StateDTO setStateDTOThreadExecution(State stateEntity) {
		StateDTO stateDTO = new StateDTO();
		if (stateEntity.getDistrict() != null && !stateEntity.getDistrict().isEmpty()) {
			stateDTO.setDistrict(stateEntity.getDistrict().stream().map(district -> setDistrictDTO(district))
					.collect(Collectors.toList()));
		}

		final Set<String> prop = new HashSet<>(Arrays.asList("stateId", "stateName"));
		this.copyStateBasicEntityToDTO(stateEntity, stateDTO, prop);
		// carDTOList.add(carDTO);
		return stateDTO;
	}
	
	public ProductCategoryDTO setProductCategoryWithBrandDTOThreadExecution(ProductCategory productCategory) {
		ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
		if (productCategory.getProductBrand() != null && !productCategory.getProductBrand().isEmpty()) {
			productCategoryDTO.setProductBrand(productCategory.getProductBrand().stream().map(brand -> setProductBrandDTO(brand))
					.collect(Collectors.toList()));
			
		}
		
		final Set<String> prop = new HashSet<>(Arrays.asList("productCategoryId", "productCategoryName"));
		this.copyProductCategoryBasicEntityToDTO(productCategory, productCategoryDTO, prop);
		// carDTOList.add(carDTO);
		return productCategoryDTO;
	}

	public DistrictDTO setDistrictDTO(District districtEntity) {
		DistrictDTO districtDTO = new DistrictDTO();

		final Set<String> prop = new HashSet<>(Arrays.asList("districtId", "districtName"));
		this.copyDistrictBasicEntityToDTO(districtEntity, districtDTO, prop);
		// carDTOList.add(carDTO);
		return districtDTO;
	}
	
	public ProductBrandDTO setProductBrandDTO(ProductBrand productBrand) {
		ProductBrandDTO productBrandDTO = new ProductBrandDTO();
		
		final Set<String> prop = new HashSet<>(Arrays.asList("productBrandId", "productBrandName"));
		this.copyBrandBasicEntityToDTO(productBrand, productBrandDTO, prop);
		// carDTOList.add(carDTO);
		return productBrandDTO;
	}

	private ProjectsDTO copyProjectsEntityToDto(Projects projectEntity) {
		ProjectsDTO projectDTO = new ProjectsDTO();
		// projectDTO.setBuilder(this.setBuilderDTO(projectEntity.getBuilder()));
		if (projectEntity.getPicture() != null && !projectEntity.getPicture().isEmpty()) {
			projectDTO.setPicture(
					projectEntity.getPicture().stream().map(this::copyPictureEntityToDto).collect(Collectors.toList()));
		}

		BeanUtils.copyProperties(projectEntity, projectDTO);
		return projectDTO;
	}

	private PictureDTO copyPictureEntityToDto(Picture pictureEntity) {
		PictureDTO pitureDTO = new PictureDTO();
		BeanUtils.copyProperties(pictureEntity, pitureDTO);
		return pitureDTO;
	}

	private AddressDTO copyAddressEntityToDto(Address addressEntity) {
		AddressDTO addressDTO = new AddressDTO();
		BeanUtils.copyProperties(addressEntity, addressDTO);
		return addressDTO;
	}

	private AddressDTO copyAddressBasicEntityToDto(Address addressEntity) {
		AddressDTO addressDTO = new AddressDTO();

		final Set<String> prop = new HashSet<>(Arrays.asList("addressId", "doorNumber", "streetFirst", "streetSecond",
				"landmark", "city", "state", "district", "pincode", "country"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(addressEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		BeanUtils.copyProperties(addressEntity, addressDTO, excludedProperties);
		return addressDTO;
	}

	private BuildersAvailableAmenitiesDTO copymenitiesEntityToDto(
			AmenitiesAndSpecifications amenitiesAndSpecificationsEntity) {
		BuildersAvailableAmenitiesDTO buildersAvailableAmenitiesDTODTO = new BuildersAvailableAmenitiesDTO();
		final Set<String> prop = new HashSet<>(
				Arrays.asList("amenitiesAndSpecificationsId", "amenitiesAndSpecificationsName"));
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(amenitiesAndSpecificationsEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		BeanUtils.copyProperties(amenitiesAndSpecificationsEntity, buildersAvailableAmenitiesDTODTO,
				excludedProperties);
		return buildersAvailableAmenitiesDTODTO;
	}

	private Address copyAddressDTOToEntity(AddressDTO addressDTO) {
		Address addressEntity = new Address();
		BeanUtils.copyProperties(addressDTO, addressEntity);
		return addressEntity;
	}

	private BuilderDTO copyBuilderEntityToDto(Builder builderEntity) {
		BuilderDTO builderDTO = new BuilderDTO();

		builderDTO.setAddress(this.copyAddressEntityToDto(builderEntity.getAddress()));
		builderDTO.setProjects(
				builderEntity.getProjects().stream().map(this::copyProjectsEntityToDto).collect(Collectors.toList()));
		BeanUtils.copyProperties(builderEntity, builderDTO);
		return builderDTO;
	}

	private AmenitiesAndSpecificationsDTO copyAmenitiesAndSpecificationEntityToDto(
			AmenitiesAndSpecifications amenitiesAndSpecifications) {

		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO = new AmenitiesAndSpecificationsDTO();
		final Set<String> prop = new HashSet<>(
				Arrays.asList("amenitiesAndSpecificationsId", "amenitiesAndSpecificationsName"));
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(amenitiesAndSpecificationsDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		BeanUtils.copyProperties(amenitiesAndSpecifications, amenitiesAndSpecificationsDTO, excludedProperties);
		return amenitiesAndSpecificationsDTO;
	}

	public static void copyProjectsBasicEntityToDTO(Projects projectEntity, ProjectsDTO projectDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(projectEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(projectEntity, projectDTO, excludedProperties);
	}

	public static void copyPicturesBasicEntityToDTO(Picture pictureEntity, PictureDTO pictureDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(pictureEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(pictureEntity, pictureDTO, excludedProperties);
	}

	public static void copyBuilderBasicEntityToDTO(Builder builderEntity, BuilderDTO builderDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(builderEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(builderEntity, builderDTO, excludedProperties);
	}

	public static void copyStateBasicEntityToDTO(State stateEntity, StateDTO stateDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(stateEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(stateEntity, stateDTO, excludedProperties);
	}
	
	public static void copyProductCategoryBasicEntityToDTO(ProductCategory productCategoryEntity, ProductCategoryDTO productCategoryDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(productCategoryEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(productCategoryEntity, productCategoryDTO, excludedProperties);
	}

	public static void copyDistrictBasicEntityToDTO(District districtEntity, DistrictDTO districtDTO,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(districtEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(districtEntity, districtDTO, excludedProperties);
	}
	
	public static void copyBrandBasicEntityToDTO(ProductBrand ProductBrandEntity, ProductBrandDTO ProductBrandDTO,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(ProductBrandEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(ProductBrandEntity, ProductBrandDTO, excludedProperties);
	}

	public static void copyBuilderBasicDTOToEntity(BuilderDTO builderDTO, Builder builderEntity, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(builderEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(builderDTO, builderEntity, excludedProperties);
	}

	public static BuildersAvailableAmenities copyBuildersBasicAvailableAmenitiesDTOToEntity(
			BuildersAvailableAmenitiesDTO BuildersAvailableAmenitiesDTO,
			BuildersAvailableAmenities BuildersAvailableAmenitiesEntity) throws BeansException {
		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "amenitiesAndSpecificationsId"));

		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(BuildersAvailableAmenitiesEntity.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(BuildersAvailableAmenitiesDTO, BuildersAvailableAmenitiesEntity, excludedProperties);
		return BuildersAvailableAmenitiesEntity;
	}

	public BuildersAvailableAmenitiesDTO copyBuildersBasicAvailableAmenitiesEntityToDTO(
			BuildersAvailableAmenities BuildersAvailableAmenitiesEntity,
			BuildersAvailableAmenitiesDTO BuildersAvailableAmenitiesDTO) throws BeansException {
		BuildersAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
				copyAmenityAndSpecificationsEntityToDTO(getAmenitiesAndSpecificationsByAmenityid(
						BuildersAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));
		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "amenitiesAndSpecificationsId"));

		/*
		 * BuildersAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
		 * copyAmenityAndSpecificationsEntityToDTO(
		 * getAmenitiesAndSpecificationsByAmenityid(
		 * BuildersAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));
		 */
		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(BuildersAvailableAmenitiesDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(BuildersAvailableAmenitiesEntity, BuildersAvailableAmenitiesDTO, excludedProperties);
		return BuildersAvailableAmenitiesDTO;
	}

	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsByAmenityid(int amenityId) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<AmenitiesAndSpecifications> amenitiesAndSpecifications = new ArrayList<AmenitiesAndSpecifications>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AmenitiesAndSpecifications> criteria = builder.createQuery(AmenitiesAndSpecifications.class);
		Root<AmenitiesAndSpecifications> rootBuilder = criteria.from(AmenitiesAndSpecifications.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("amenitiesAndSpecificationsId"), amenityId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<AmenitiesAndSpecifications> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		amenitiesAndSpecifications = query.getResultList();

		return amenitiesAndSpecifications.get(0);
		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

	private static AmenitiesAndSpecificationsDTO copyAmenityAndSpecificationsEntityToDTO(
			AmenitiesAndSpecifications amenitiesAndSpecifications) {
		final Set<String> prop = new HashSet<>(
				Arrays.asList("amenitiesAndSpecificationsId", "amenitiesAndSpecificationsName"));
		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO = new AmenitiesAndSpecificationsDTO();
		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(amenitiesAndSpecificationsDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(amenitiesAndSpecifications, amenitiesAndSpecificationsDTO, excludedProperties);
		return amenitiesAndSpecificationsDTO;
	}

	public ProjectsAvailableAmenitiesDTO copyProjectsBasicAvailableAmenitiesEntityToDTO(
			ProjectsAvailableAmenities projectsAvailableAmenitiesEntity,
			ProjectsAvailableAmenitiesDTO projectsAvailableAmenitiesDTO) throws BeansException {
		/*
		 * projectsAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
		 * copyAmenityAndSpecificationsEntityToDTO(
		 * getAmenitiesAndSpecificationsByAmenityid(
		 * projectsAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));
		 */
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "amenitiesAndSpecificationsId"));

		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(projectsAvailableAmenitiesDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(projectsAvailableAmenitiesEntity, projectsAvailableAmenitiesDTO, excludedProperties);
		return projectsAvailableAmenitiesDTO;
	}

	public static void copyProjectsBasicDTOToEntity(ProjectsDTO projectDTO, Projects projectEntity, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(projectEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(projectDTO, projectEntity, excludedProperties);
	}

	public static void copyPicturesBasicDTOToEntity(PictureDTO pictureDTO, Picture pictureEntity, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(pictureEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(pictureDTO, pictureEntity, excludedProperties);
	}

	public Resource getFileSystem(String filename, HttpServletResponse response) {
		return getResource(filename, response, ResourceType.FILE_SYSTEM);
	}

	/**
	 * @param filename filename
	 * @param response Http response.
	 * @return file from classpath.
	 */
	public Resource getClassPathFile(String filename, HttpServletResponse response) {
		return getResource(filename, response, ResourceType.CLASSPATH);
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

	public char[] GenerateBuildersOTP(int len) {
		System.out.println("Generating OTP using random() : ");
		System.out.print("You OTP is : ");

		// Using numeric values
		String numbers = "0123456789";

		// Using random method
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}

	public void saveBuilderOTP(BuilderDTO builderDTO) {
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();

		BuilderOtp builderOtp = new BuilderOtp();

		int otpDigits = 4;
		char[] otpGeneratedForBuilder = GenerateBuildersOTP(otpDigits);
		System.out.println(otpGeneratedForBuilder);
		// String[] otpGeneratedForBuilderStringArr = new
		// String[otpGeneratedForBuilder.length];
		String otpGeneratedForBuilderConcated = null;
		for (int i = 0; i < otpGeneratedForBuilder.length; i++) {
			// ints[i] = Character.getNumericValue(otpGeneratedForBuilder[i]);
			System.out.println(String.valueOf(otpGeneratedForBuilder[i]));
			otpGeneratedForBuilderConcated = otpGeneratedForBuilderConcated + String.valueOf(otpGeneratedForBuilder[i]);
			// otpGeneratedForBuilderConcated.concat(String.valueOf(otpGeneratedForBuilder[i]));
			// otpGeneratedForBuilderStringArr[i] =
			// String.valueOf(otpGeneratedForBuilder[i]);
		}

		System.out.println(otpGeneratedForBuilderConcated.substring(4));

		builderOtp.setBuilderPhoneNumber(builderDTO.getPhone());
		builderOtp.setBuildersOtpNumber(Integer.parseInt(otpGeneratedForBuilderConcated.substring(4)));

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(builderOtp)) {
			// BuilderOtp entityAvailableOrNot = entityManager.find(BuilderOtp.class,
			// builderOtp.getBuilderPhoneNumber());
			// EntityManager entityManager = em.getEntityManager("builder");
			List<BuilderOtp> builderOtps = new ArrayList<BuilderOtp>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<BuilderOtp> criteria = builder.createQuery(BuilderOtp.class);
			Root<BuilderOtp> rootBuilder = criteria.from(BuilderOtp.class);
			criteria.select(rootBuilder);

			List<Predicate> restrictions = new ArrayList<Predicate>();
			restrictions.add(builder.equal(rootBuilder.get("builderPhoneNumber"), builderOtp.getBuilderPhoneNumber()));

			criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
			TypedQuery<BuilderOtp> query = entityManager.createQuery(criteria);
			query.setHint(QueryHints.HINT_CACHEABLE, true);
			query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
			builderOtps = query.getResultList();
			if (builderOtps.isEmpty()) {
				// if (1 == 1) {
				// persist object - add to entity manager
				entityManager.persist(builderOtp);
				// flush em - save to DB
				entityManager.flush();
			} else {
				builderOtp.setBuilderOtpId(builderOtps.get(0).getBuilderOtpId());
				entityManager.merge(builderOtp);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();

	}

	public boolean VerifyBuildersOTP(BuilderDTO builderDTO, String otp) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<BuilderOtp> builderOtp = new ArrayList<BuilderOtp>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuilderOtp> criteria = builder.createQuery(BuilderOtp.class);
		Root<BuilderOtp> rootBuilder = criteria.from(BuilderOtp.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("builderPhoneNumber"), builderDTO.getPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuilderOtp> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderOtp = query.getResultList();

		int otpDB = builderOtp.get(0).getBuildersOtpNumber();
		int otpCustomerEnered = Integer.parseInt(otp.replace("\"", ""));

		if (otpDB == otpCustomerEnered) {
			return true;
		} else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

	public boolean VerifyAlreadyRegisteredBuilder(BuilderDTO builderDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Builder> builderEntity = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phone"), builderDTO.getPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderEntity = query.getResultList();

		if (!builderEntity.isEmpty()) {
			return true;
		} else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

	public boolean VerifyIfMobileAlreadyRegisteredAsCustomer(BuilderDTO builderDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Customer> customerEntity = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phoneCustomer"), builderDTO.getPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Customer> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerEntity = query.getResultList();

		if (!customerEntity.isEmpty()) {
			return true;
		} else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

}
