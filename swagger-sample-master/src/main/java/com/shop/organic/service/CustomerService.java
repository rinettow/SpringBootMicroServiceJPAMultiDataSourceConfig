package com.shop.organic.service;

import java.beans.PropertyDescriptor;
import java.io.File;
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
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.SiteLocationDTO;
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
import com.shop.organic.entity.car.CustomerRedQuotations;
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.District;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.SiteLocation;
import com.shop.organic.entity.car.State;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
//import com.shop.organic.service.BuilderService.ResourceType;
import com.shop.organic.util.CreateEntityManager;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

@Service
@Transactional
//@ConfigurationProperties("application-dev")
public class CustomerService {

	// @Autowired
	// private CarRepository carRepository;

	@Autowired
	private BuilderService builderService;
	
	@Autowired
	private ProductService productService;

	// @Value("${server.port}")
	private String port;

	@Autowired
	private CreateEntityManager em;
	
	private WebClient webClient = null;

	@Autowired
	public CustomerService(WebClient webClient) {
		this.webClient = webClient;
	}

	private enum ResourceType {
		FILE_SYSTEM, CLASSPATH
	}

	public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
		CustomerDTO registeredCustomerDTO = new CustomerDTO();

		Customer customerEntity = setCustomerEntity(customerDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(customerEntity)) {
			// persist object - add to entity manager
			entityManager.persist(customerEntity);
			// flush em - save to DB
			entityManager.flush();
		}
		// commit transaction at all
		entityManager.getTransaction().commit();
		registeredCustomerDTO = setCustomerDTO(customerEntity);
		entityManager.close();
		return registeredCustomerDTO;
	}

	public void ResetCustomerPassword(CustomerDTO customerDTO) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Customer> customerEntity = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phoneCustomer"), customerDTO.getPhoneCustomer()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Customer> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerEntity = query.getResultList();

		// commit transaction at all
		// entityManager.getTransaction().commit();

		Customer customerToChangePasword = customerEntity.get(0);
		customerToChangePasword.setPasswordCustomer(customerDTO.getPasswordCustomer());

		entityManager.getTransaction().begin();
		// if (!entityManager.contains(builderEntity)) {
		// persist object - add to entity manager
		entityManager.merge(customerToChangePasword);
		// flush em - save to DB
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	public void ceateImageDirectoryForCustomer(CustomerDTO customerDTO) {
		System.out.println("ceateImageDirectoryForCustomer");

		String path = "C:/Users/User/GitHub Repository/CustomersImage/";
		String finalPath = path.concat("Customer").concat(Integer.toString(customerDTO.getCustomerId()));
		if (!new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForCustomer2");
			new File(finalPath).mkdir();
		}
		System.out.println("realPathtoUploads = {}" + finalPath);
	}

	public void ceateImageDirectoryForCustomerRequirement(CustomerRequirementDTO CustomerRequirementDTO,
			MultipartFile planPDFFileFormat, MultipartFile[] landImagePNGorJPGFileFormat) {
		System.out.println("ceateImageDirectoryForCustomerRequirement");

		String path = "C:/Users/User/GitHub Repository/CustomersImage/";
		String finalPath = path.concat("Customer").concat(Integer.toString(CustomerRequirementDTO.getCustomerId()))
				.concat("/").concat("CustomerRequirement")
				.concat(Integer.toString(CustomerRequirementDTO.getCustomerRequirementId()));
		if (!new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForCustomerRequirement");
			new File(finalPath).mkdir();
			try {
				String planPDFFileFormatfileName = planPDFFileFormat.getOriginalFilename();
				// String landImagePNGorJPGFileFormatfileName =
				// landImagePNGorJPGFileFormat.getOriginalFilename();
				if (!planPDFFileFormatfileName.isEmpty()) {
					planPDFFileFormat.transferTo(new File(finalPath.concat("/").concat(planPDFFileFormatfileName)));
					CustomerRequirementDTO.setPlanImagePath(finalPath.concat("/").concat(planPDFFileFormatfileName));
				}

				/*
				 * if(!landImagePNGorJPGFileFormatfileName.isEmpty()) {
				 * landImagePNGorJPGFileFormat.transferTo(new
				 * File(finalPath.concat("/").concat(landImagePNGorJPGFileFormatfileName)));
				 * CustomerRequirementDTO.setLandImagePath(finalPath.concat("/").concat(
				 * landImagePNGorJPGFileFormatfileName)); }
				 */

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String siteLocationPath = finalPath.concat("/").concat("SiteLocation");
		if (!new File(siteLocationPath).exists()) {
			new File(siteLocationPath).mkdir();
			List<SiteLocationDTO> siteLocations = new ArrayList<SiteLocationDTO>();
			for (int i = 0; i < landImagePNGorJPGFileFormat.length; i++) {
				String landImagePNGorJPGFileFormatfileName = landImagePNGorJPGFileFormat[i].getOriginalFilename();
				SiteLocationDTO siteLocationDTO = new SiteLocationDTO();
				SiteLocation siteLocationEntity = new SiteLocation();
				if (!landImagePNGorJPGFileFormatfileName.isEmpty()) {
					try {
						landImagePNGorJPGFileFormat[i].transferTo(
								new File(siteLocationPath.concat("/").concat(landImagePNGorJPGFileFormatfileName)));
					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					siteLocationDTO.setCustomerRequirementId(CustomerRequirementDTO.getCustomerRequirementId());
					siteLocationDTO.setSiteLocationFilePath(
							siteLocationPath.concat("/").concat(landImagePNGorJPGFileFormatfileName));
					copySiteLocationBasicDTOToEntity(siteLocationDTO, siteLocationEntity);
					this.CreateSiteLocation(siteLocationEntity);
					siteLocations.add(siteLocationDTO);
				}
			}
			CustomerRequirementDTO.setSiteLocations(siteLocations);

		}

	}

	public Customer setCustomerEntity(CustomerDTO customerDTO) {
		Customer customerEntity = new Customer();

		final Set<String> prop = new HashSet<>(
				Arrays.asList("customerId", "customerName", "phoneCustomer", "passwordCustomer"));
		this.copyCustomerBasicDTOToEntity(customerDTO, customerEntity, prop);
		// builderEntity.setBuildersAvailableAmenities(builderDTO.getBuildersAvailableAmenities().stream().map(buildersAvailableAmenitiesDTO
		// ->
		// this.copyBuildersBasicAvailableAmenitiesDTOToEntity(buildersAvailableAmenitiesDTO,
		// new BuildersAvailableAmenities())).collect(Collectors.toList()));

		return customerEntity;
	}

	public static void copyCustomerBasicDTOToEntity(CustomerDTO customerDTO, Customer custmerEntity,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(custmerEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerDTO, custmerEntity, excludedProperties);
	}

	public CustomerDTO setCustomerDTO(Customer customerEntity) {
		CustomerDTO customerDTO = new CustomerDTO();

		final Set<String> prop = new HashSet<>(
				Arrays.asList("customerId", "customerName", "phoneCustomer", "passwordCustomer"));
		this.copyCustomerBasicEntityToDTO(customerEntity, customerDTO, prop);

		if (customerEntity.getCustomerRequirement() != null && !customerEntity.getCustomerRequirement().isEmpty()) {
			customerDTO.setCustomerRequirement(customerEntity.getCustomerRequirement().stream()
					.map(this::setCustomerRequirementDTOForCustomerLogin).collect(Collectors.toList()));
		}
		
		if (customerEntity.getMaterialRequirement() != null && !customerEntity.getMaterialRequirement().isEmpty()) {
			//customerDTO.setMaterialRequirement(customerEntity.getMaterialRequirement().stream()
			//	.map(materialRequirement -> productService.setMaterialRequirementDTO(materialRequirement))
			//	.collect(Collectors.toList()));

		}
		// carDTOList.add(carDTO);
		return customerDTO;
	}

	public CustomerDTO setCustomerDTOWithoutRequirement(Customer customerEntity) {
		CustomerDTO customerDTO = new CustomerDTO();

		final Set<String> prop = new HashSet<>(
				Arrays.asList("customerId", "customerName", "phoneCustomer", "passwordCustomer"));
		this.copyCustomerBasicEntityToDTO(customerEntity, customerDTO, prop);

		return customerDTO;
	}

	public static void copyCustomerBasicEntityToDTO(Customer customerEntity, CustomerDTO customerDTO,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(customerDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerEntity, customerDTO, excludedProperties);
	}

	public Map<String, Object> sendOTPForCustomerLogin(CustomerDTO customerDTO) {
		CustomerDTO LoginCustomerDTO = new CustomerDTO();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Customer> LoginCustomer = new ArrayList<Customer>();
		String responseStatus = null;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Customer> loginCustomer = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phoneCustomer"), customerDTO.getPhoneCustomer()));
		restrictions.add(builder.isNull(rootBuilder.get("accountStatus")));
		//restrictions.add(builder.notEqual(rootBuilder.get("accountStatus"), "DELETED"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Customer> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		
		
		/*EntityManager entityManager = em.getEntityManager("builder");
		String responseStatus = null;

		Query q = entityManager.createQuery("SELECT c FROM Customer c", Customer.class);*/
		
		LoginCustomer = query.getResultList();

		System.out.println("LoginBuilderDTO" + LoginCustomer.get(0).getPhoneCustomer());


		if (LoginCustomer.isEmpty() && LoginCustomer.size() == 0) {
			// throw new ResourceNotFoundException(
			// "Mobile Number: " + customerDTO.getPhoneCustomer() + " not Registered...");
			responseStatus = "Customer Mobile Not Registered";
		}

		if (!LoginCustomer.isEmpty()) {
			if (verifyPassword(LoginCustomer.get(0), customerDTO.getPasswordCustomer())) {
				responseStatus = "Success";
				LoginCustomerDTO = setCustomerDTO(LoginCustomer.get(0));
			} else {
				responseStatus = "Incorrect Password";
			}
		}
		entityManager.close();
		response.put("responseStatus", responseStatus);
		response.put("loggedinCustomer", LoginCustomerDTO);
		return response;
	}

	public boolean verifyPassword(Customer loggedinCustomer, String password) {
		if (loggedinCustomer.getPasswordCustomer().equals(password)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean validateCustomersOpenRequirement(CustomerRequirementDTO customerRequirementDTO) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<CustomerRequirement> customerRequirementEntity;
		List<CustomerRequirement> customerRequirementEntityOpenStatus;
		boolean isOpenRequirementAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRequirement> criteria = builder.createQuery(CustomerRequirement.class);
		Root<CustomerRequirement> rootBuilder = criteria.from(CustomerRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerId"), customerRequirementDTO.getCustomerId()));
		restrictions.add(builder.equal(rootBuilder.get("amenityAndSpecifiactionId"),
				customerRequirementDTO.getAmenityAndSpecifiactionId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRequirementEntity = query.getResultList();

		if (!customerRequirementEntity.isEmpty()) {
			customerRequirementEntityOpenStatus = customerRequirementEntity.stream()
					.filter(custRequirement -> custRequirement.getRequirementStatus().equals("OPEN"))
					.collect(Collectors.toList());
			if (!customerRequirementEntityOpenStatus.isEmpty()) {
				isOpenRequirementAvailable = true;
				// throw new ResourceNotFoundException("Open Requirement is already available
				// for " +amenityName+ "Please close the existing requirement");
			}

		}
		entityManager.close();
		return isOpenRequirementAvailable;
	}

	public List<CustomerRequirementDTO> getCustomersOpenRequirement(int customerId, int amenityAndSpecificationId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<CustomerRequirement> customerRequirementEntity;
		List<CustomerRequirement> customerRequirementEntityOpenStatus = null;
		List<CustomerRequirementDTO> customerRequirementDTOOpenStatus = null;
		boolean isOpenRequirementAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRequirement> criteria = builder.createQuery(CustomerRequirement.class);
		Root<CustomerRequirement> rootBuilder = criteria.from(CustomerRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerId"), customerId));
		restrictions.add(builder.equal(rootBuilder.get("amenityAndSpecifiactionId"), amenityAndSpecificationId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRequirementEntity = query.getResultList();

		if (!customerRequirementEntity.isEmpty()) {
			customerRequirementEntityOpenStatus = customerRequirementEntity.stream()
					.filter(custRequirement -> custRequirement.getRequirementStatus().equals("OPEN"))
					.collect(Collectors.toList());
			if (!customerRequirementEntityOpenStatus.isEmpty()) {
				isOpenRequirementAvailable = true;
				// throw new ResourceNotFoundException("Open Requirement is already available
				// for " +amenityName+ "Please close the existing requirement");
			}

		}
		if (customerRequirementEntityOpenStatus != null && !customerRequirementEntityOpenStatus.isEmpty()) {
			customerRequirementDTOOpenStatus = customerRequirementEntityOpenStatus.stream()
					.map(require -> setCustomerRequirementDTO(require)).collect(Collectors.toList());
		}

		entityManager.close();
		return customerRequirementDTOOpenStatus;
	}

	public boolean validateIfQouteAlreadyRequestedToBuilder(int customerRequirementId, int builderId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<BuildersEstimate> buildersEstimate;
		boolean isQouteAlreadyRequestedToBuilder = false;
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
			isQouteAlreadyRequestedToBuilder = true;
		} else {
			isQouteAlreadyRequestedToBuilder = false;
		}
		return isQouteAlreadyRequestedToBuilder;
	}

	public List<SiteLocationDTO> getCustomerSiteLocationByRequirmentId(CustomerRequirementDTO customerRequirementDTO) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<SiteLocationDTO> siteLocationDTO;
		List<SiteLocation> siteLocation;
		boolean isQouteAlreadyRequestedToBuilder = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteLocation> criteria = builder.createQuery(SiteLocation.class);
		Root<SiteLocation> rootBuilder = criteria.from(SiteLocation.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"),
				customerRequirementDTO.getCustomerRequirementId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<SiteLocation> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		siteLocation = query.getResultList();
		siteLocationDTO = siteLocation.stream().map(siteLoc -> this.setSiteLOcationDTO(siteLoc))
				.collect(Collectors.toList());

		return siteLocationDTO;
	}

	public boolean VerifyIfCustomerRedQuotationAlready(int builderEstimateIdId, int customerId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<CustomerRedQuotations> customerRedQuotations;
		boolean isCustomerRedQuotationsAlready = false;
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRedQuotations> criteria = builder.createQuery(CustomerRedQuotations.class);
		Root<CustomerRedQuotations> rootBuilder = criteria.from(CustomerRedQuotations.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("builderEstimateIdId"), builderEstimateIdId));
		restrictions.add(builder.equal(rootBuilder.get("customerId"), customerId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRedQuotations> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRedQuotations = query.getResultList();

		if (!customerRedQuotations.isEmpty()) {
			isCustomerRedQuotationsAlready = true;
		} else {
			isCustomerRedQuotationsAlready = false;
		}
		return isCustomerRedQuotationsAlready;
	}

	public void customerRedQuotationsEntry(int builderEstimateIdId, int customerId) {
		CustomerRedQuotations customerRedQuotations = new CustomerRedQuotations();
		customerRedQuotations.setCustomerId(customerId);
		customerRedQuotations.setBuilderEstimateIdId(builderEstimateIdId);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(customerRedQuotations)) {
			// persist object - add to entity manager
			entityManager.persist(customerRedQuotations);
			// flush em - save to DB
			

		}
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.flush();
		entityManager.close();
	}

	public BuildersEstimateDTO customerRedQuotations(int builderEstimateIdId, int customerId) {
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		if (!this.VerifyIfCustomerRedQuotationAlready(builderEstimateIdId, customerId)) {
			this.customerRedQuotationsEntry(builderEstimateIdId, customerId);
		}
		return buildersEstimateDTO;
	}

	public CustomerRequirementDTO CreateCustomerRequirement(CustomerRequirementDTO customerRequirementDTO) {
		CustomerRequirementDTO customerRequirementDTOResponse = new CustomerRequirementDTO();
		CustomerRequirement customerRequirementEntity = new CustomerRequirement();
		copyCustomerRequirementBasicDTOToEntity(customerRequirementDTO, customerRequirementEntity);
		EntityManager entityManager = em.getEntityManager("builder");
		
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
		
		customerRequirementEntity.setReqCreatedTimestamp(timestampObject);

		entityManager.getTransaction().begin();

		if (!entityManager.contains(customerRequirementEntity)) {
			CustomerRequirement entityAvailableOrNot = entityManager.find(CustomerRequirement.class,
					customerRequirementEntity.getCustomerRequirementId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(customerRequirementEntity);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(customerRequirementEntity);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();
		customerRequirementDTOResponse = this.setCustomerRequirementDTO(customerRequirementEntity);
		entityManager.close();
		return customerRequirementDTOResponse;
	}

	public void CreateSiteLocation(SiteLocation siteLocationEntity) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();

		if (!entityManager.contains(siteLocationEntity)) {
			SiteLocation entityAvailableOrNot = entityManager.find(SiteLocation.class,
					siteLocationEntity.getSiteLocationId());
			if (entityAvailableOrNot == null) {
				// persist object - add to entity manager
				entityManager.persist(siteLocationEntity);
				// flush em - save to DB
				entityManager.flush();
			} else {
				entityManager.merge(siteLocationEntity);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();
		// return customerRequirementDTOResponse;
	}

	public BuildersEstimateDTO addBuildersEstimateEntry(int customerOpenRequirementId, int customerId,
			int amenitiesAndSpecificationId, int projectId, int builderId) {
		BuildersEstimate buildersEstimate = new BuildersEstimate();
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimate.setCustomerRequirementId(customerOpenRequirementId);
		buildersEstimate.setBuilderId(builderId);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();

		if (!entityManager.contains(buildersEstimate)) {
			// persist object - add to entity manager
			entityManager.persist(buildersEstimate);
			// flush em - save to DB
			entityManager.flush();

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		buildersEstimateDTO = setBuilderEstimateDTO(buildersEstimate);
		entityManager.close();
		return buildersEstimateDTO;
	}

	public CustomerRequirementDTO setCustomerRequirementDTO(CustomerRequirement customerRequirementEntity) {
		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		this.copyCustomerRequirementBasicEntityToDTO(customerRequirementEntity, customerRequirementDTO);
		HttpServletResponse response = null;
		if (customerRequirementEntity.getPlanImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(customerRequirementEntity.getPlanImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				customerRequirementDTO.setPlanPDFFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * if (customerRequirementEntity.getLandImagePath() != null) { //
		 * projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath()
		 * , // response)); ServletContext sc = null; // InputStream in = //
		 * sc.getResourceAsStream(projectEntity.getProjMainPicFilePath()); InputStream
		 * in = null; try { in =
		 * this.getFileSystem(customerRequirementEntity.getLandImagePath(),
		 * response).getInputStream(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } try { byte[] media =
		 * IOUtils.toByteArray(in);
		 * customerRequirementDTO.setLandImagePNGorJPGFileFormat(media); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */

		if (customerRequirementEntity.getSiteLocations() != null
				&& !customerRequirementEntity.getSiteLocations().isEmpty()) {
			customerRequirementDTO.setSiteLocations(customerRequirementEntity.getSiteLocations().stream()
					.map(siteLocation -> this.setSiteLOcationDTO(siteLocation)).collect(Collectors.toList()));
		}

		if (customerRequirementEntity.getCustomerForCustomerRequirement() != null) {
			customerRequirementDTO.setCustomerForCustomerRequirement(
					setCustomerDTOWithoutRequirement(customerRequirementEntity.getCustomerForCustomerRequirement()));
		}
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO
					.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(
							customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		if (customerRequirementEntity.getBuildersEstimate() != null
				&& !customerRequirementEntity.getBuildersEstimate().isEmpty()) {
			customerRequirementDTO.setBuildersEstimate(customerRequirementEntity.getBuildersEstimate().stream()
				.map(this::setBuilderEstimateDTO).collect(Collectors.toList()));
		}
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}
	
	
	public CustomerRequirementDTO setCustomerRequirementDTOForCustomerLogin(CustomerRequirement customerRequirementEntity) {
		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		this.copyCustomerRequirementBasicEntityToDTO(customerRequirementEntity, customerRequirementDTO);
		HttpServletResponse response = null;
		if (customerRequirementEntity.getPlanImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(customerRequirementEntity.getPlanImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				//customerRequirementDTO.setPlanPDFFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * if (customerRequirementEntity.getLandImagePath() != null) { //
		 * projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath()
		 * , // response)); ServletContext sc = null; // InputStream in = //
		 * sc.getResourceAsStream(projectEntity.getProjMainPicFilePath()); InputStream
		 * in = null; try { in =
		 * this.getFileSystem(customerRequirementEntity.getLandImagePath(),
		 * response).getInputStream(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } try { byte[] media =
		 * IOUtils.toByteArray(in);
		 * customerRequirementDTO.setLandImagePNGorJPGFileFormat(media); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */

		if (customerRequirementEntity.getSiteLocations() != null
				&& !customerRequirementEntity.getSiteLocations().isEmpty()) {
			customerRequirementDTO.setSiteLocations(customerRequirementEntity.getSiteLocations().stream()
					.map(siteLocation -> this.setSiteLOcationDTO(siteLocation)).collect(Collectors.toList()));
		}

		
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO
					.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(
							customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		if (customerRequirementEntity.getBuildersEstimate() != null
				&& !customerRequirementEntity.getBuildersEstimate().isEmpty()) {
			customerRequirementDTO.setBuildersEstimate(customerRequirementEntity.getBuildersEstimate().stream()
				.map(this::setBuilderEstimateDTOForCustomerLogin).collect(Collectors.toList()));
		}
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}
	
	
	public CustomerRequirementDTO setCustomerRequirementDTOForBuilderOpenTenders(CustomerRequirement customerRequirementEntity) {
		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		this.copyCustomerRequirementBasicEntityToDTO(customerRequirementEntity, customerRequirementDTO);
		HttpServletResponse response = null;
		if (customerRequirementEntity.getPlanImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(customerRequirementEntity.getPlanImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				//customerRequirementDTO.setPlanPDFFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * if (customerRequirementEntity.getLandImagePath() != null) { //
		 * projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath()
		 * , // response)); ServletContext sc = null; // InputStream in = //
		 * sc.getResourceAsStream(projectEntity.getProjMainPicFilePath()); InputStream
		 * in = null; try { in =
		 * this.getFileSystem(customerRequirementEntity.getLandImagePath(),
		 * response).getInputStream(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } try { byte[] media =
		 * IOUtils.toByteArray(in);
		 * customerRequirementDTO.setLandImagePNGorJPGFileFormat(media); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */

		if (customerRequirementEntity.getSiteLocations() != null
				&& !customerRequirementEntity.getSiteLocations().isEmpty()) {
			customerRequirementDTO.setSiteLocations(customerRequirementEntity.getSiteLocations().stream()
					.map(siteLocation -> this.setSiteLOcationDTO(siteLocation)).collect(Collectors.toList()));
		}

		if (customerRequirementEntity.getCustomerForCustomerRequirement() != null) {
			customerRequirementDTO.setCustomerForCustomerRequirement(
					setCustomerDTOWithoutRequirement(customerRequirementEntity.getCustomerForCustomerRequirement()));
		}
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO
					.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(
							customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}

	public SiteLocationDTO setSiteLOcationDTO(SiteLocation siteLocationEntity) {
		SiteLocationDTO siteLocationDTO = new SiteLocationDTO();
		this.copySiteLocationBasicEntityToDTO(siteLocationEntity, siteLocationDTO);
		HttpServletResponse response = null;

		if (siteLocationEntity.getSiteLocationFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(siteLocationEntity.getSiteLocationFilePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				//siteLocationDTO.setLandImagePNGorJPGFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return siteLocationDTO;
	}
	
	public SiteLocationDTO setSiteLOcationDTOWithImage(SiteLocation siteLocationEntity) {
		SiteLocationDTO siteLocationDTO = new SiteLocationDTO();
		this.copySiteLocationBasicEntityToDTO(siteLocationEntity, siteLocationDTO);
		HttpServletResponse response = null;

		if (siteLocationEntity.getSiteLocationFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(siteLocationEntity.getSiteLocationFilePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				siteLocationDTO.setLandImagePNGorJPGFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return siteLocationDTO;
	}

	public BuildersEstimateDTO setBuilderEstimateDTO(BuildersEstimate buildersEstimateEntity) {
		HttpServletResponse response = null;
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		this.copyBuildersEstimateBasicEntityToDTO(buildersEstimateEntity, buildersEstimateDTO);

		if (buildersEstimateEntity.getDetailedEstimateFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(buildersEstimateEntity.getDetailedEstimateFilePath(), response)
						.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				buildersEstimateDTO.setDetailedEstimateFile(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (buildersEstimateEntity.getCustomerRequirementForBuildersEstimate() != null) {
			buildersEstimateDTO.setCustomerRequirementDTO(setCustomerRequirementDTOWithoutBuilderEstimate(
					buildersEstimateEntity.getCustomerRequirementForBuildersEstimate()));
		}

		/*
		 * if (buildersEstimateEntity.getProjectForBuildersEstimate() != null) {
		 * buildersEstimateDTO.setProjectDTO(builderService.setProjectDTO(
		 * buildersEstimateEntity.getProjectForBuildersEstimate())); }
		 */

		if (buildersEstimateEntity.getBuilderForBuildersEstimate() != null) {
			buildersEstimateDTO.setBuilderDTO(
					builderService.setBuilderDTOForCustomer(buildersEstimateEntity.getBuilderForBuildersEstimate()));
		}

		
		if(buildersEstimateEntity.getCustomerRequirementForBuildersEstimate() != null) {
			List<CustomerRedQuotations> customerRedQuotations = this.getAllViewedQuotationsbyCustomer(String.valueOf(buildersEstimateEntity.getCustomerRequirementForBuildersEstimate().getCustomerId()));
			List<String> customerViewedQuotationIds = null;
			if (customerRedQuotations != null && !customerRedQuotations.isEmpty()) {
				customerViewedQuotationIds = customerRedQuotations.stream()
						.map(custReqQuote -> this.getCustomerViewedBuilderEstimateId(custReqQuote))
						.collect(Collectors.toList());

			}

			if(customerViewedQuotationIds != null && !customerViewedQuotationIds.isEmpty()) {
				if(customerViewedQuotationIds.contains(String.valueOf(buildersEstimateEntity.getBuildersEstimateId()))) {
					buildersEstimateDTO.setIsEstimateRedByCustomer("Viewed");
				}else {
					buildersEstimateDTO.setIsEstimateRedByCustomer("New");
				}
			}else {
				buildersEstimateDTO.setIsEstimateRedByCustomer("New");
			}
		}	
		
		// carDTOList.add(carDTO);
		return buildersEstimateDTO;
	}
	
	
	public BuildersEstimateDTO setBuilderEstimateDTOForCustomerLogin(BuildersEstimate buildersEstimateEntity) {
		HttpServletResponse response = null;
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		this.copyBuildersEstimateBasicEntityToDTO(buildersEstimateEntity, buildersEstimateDTO);

		if (buildersEstimateEntity.getDetailedEstimateFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(buildersEstimateEntity.getDetailedEstimateFilePath(), response)
						.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				//buildersEstimateDTO.setDetailedEstimateFile(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		if (buildersEstimateEntity.getBuilderForBuildersEstimate() != null) {
			buildersEstimateDTO.setBuilderDTO(
					builderService.setBuilderDTOForCustomerLogin(buildersEstimateEntity.getBuilderForBuildersEstimate()));
		}

		
		if(buildersEstimateEntity.getCustomerRequirementForBuildersEstimate() != null) {
			List<CustomerRedQuotations> customerRedQuotations = this.getAllViewedQuotationsbyCustomer(String.valueOf(buildersEstimateEntity.getCustomerRequirementForBuildersEstimate().getCustomerId()));
			List<String> customerViewedQuotationIds = null;
			if (customerRedQuotations != null && !customerRedQuotations.isEmpty()) {
				customerViewedQuotationIds = customerRedQuotations.stream()
						.map(custReqQuote -> this.getCustomerViewedBuilderEstimateId(custReqQuote))
						.collect(Collectors.toList());

			}

			if(customerViewedQuotationIds != null && !customerViewedQuotationIds.isEmpty()) {
				if(customerViewedQuotationIds.contains(String.valueOf(buildersEstimateEntity.getBuildersEstimateId()))) {
					buildersEstimateDTO.setIsEstimateRedByCustomer("Viewed");
				}else {
					buildersEstimateDTO.setIsEstimateRedByCustomer("New");
				}
			}else {
				buildersEstimateDTO.setIsEstimateRedByCustomer("New");
			}
		}	
		
		// carDTOList.add(carDTO);
		return buildersEstimateDTO;
	}
	
	public String getCustomerViewedBuilderEstimateId(CustomerRedQuotations customerRedQuotations) {
		return String.valueOf(customerRedQuotations.getBuilderEstimateIdId());
}
	
	public List<CustomerRedQuotations> getAllViewedQuotationsbyCustomer(String customerId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<CustomerRedQuotations> customerRedQuotations;
		String isBuilderRedCustomerRequirementAlready = "New";
		EntityManager entityManager = em.getEntityManager("builder");

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRedQuotations> criteria = builder.createQuery(CustomerRedQuotations.class);
		Root<CustomerRedQuotations> rootBuilder = criteria.from(CustomerRedQuotations.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerId"), customerId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRedQuotations> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRedQuotations = query.getResultList();

		return customerRedQuotations;
	}

	public BuildersEstimateDTO setBuilderEstimateDTObymanualCustomerRequirementPicking(
			BuildersEstimate buildersEstimateEntity) {
		HttpServletResponse response = null;
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		this.copyBuildersEstimateBasicEntityToDTO(buildersEstimateEntity, buildersEstimateDTO);

		buildersEstimateDTO.setCustomerRequirementDTO(
				getCustomerRequirementById(buildersEstimateEntity.getCustomerRequirementId()));
		
		if (buildersEstimateEntity.getDetailedEstimateFilePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(buildersEstimateEntity.getDetailedEstimateFilePath(), response)
						.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				//buildersEstimateDTO.setDetailedEstimateFile(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * if (buildersEstimateEntity.getProjectForBuildersEstimate() != null) {
		 * buildersEstimateDTO.setProjectDTO(builderService.setProjectDTO(
		 * buildersEstimateEntity.getProjectForBuildersEstimate())); }
		 */
		//List<Builder> builder= GetBuilderByBuilderId(buildersEstimateEntity.getBuilderId());
		/*if (buildersEstimateEntity.getBuilderForBuildersEstimate() != null) {
			buildersEstimateDTO.setBuilderDTO(
					builderService.setBuilderDTOWithoutProject(buildersEstimateEntity.getBuilderForBuildersEstimate()));
		}*/
		// carDTOList.add(carDTO);
		return buildersEstimateDTO;
	}
	
	public List<Builder> GetBuilderByBuilderId(int builderId) {
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

	
	
	
	public CustomerRequirementDTO getCustomerRequirementById(int customerRequirementId) {
		List<CustomerRequirement> custRequirement = null;
		CustomerRequirementDTO custRequirementDTO = null;
		EntityManager entityManager = em.getEntityManager("builder");
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerRequirement> criteria = builder.createQuery(CustomerRequirement.class);
		Root<CustomerRequirement> rootBuilder = criteria.from(CustomerRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerRequirementId"), customerRequirementId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		custRequirement = query.getResultList();

		if (custRequirement.isEmpty()) {
			throw new ResourceNotFoundException("customerRequirementId: " + customerRequirementId + " not Found...");
		}
		custRequirementDTO = setCustomerRequirementDTOForBuilderOpenTenders(custRequirement.get(0));
		//custRequirementDTO = setCustomerRequirementDTOWithoutBuilderEstimate(custRequirement.get(0));
		entityManager.close();
		return custRequirementDTO;
	}

	public CustomerRequirementDTO setCustomerRequirementDTOWithoutBuilderEstimate(
			CustomerRequirement customerRequirementEntity) {
		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		this.copyCustomerRequirementBasicEntityToDTO(customerRequirementEntity, customerRequirementDTO);
		HttpServletResponse response = null;
		if (customerRequirementEntity.getPlanImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(customerRequirementEntity.getPlanImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				customerRequirementDTO.setPlanPDFFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (customerRequirementEntity.getLandImagePath() != null) {
			// projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(),
			// response));
			ServletContext sc = null;
			// InputStream in =
			// sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
			InputStream in = null;
			try {
				in = this.getFileSystem(customerRequirementEntity.getLandImagePath(), response).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				byte[] media = IOUtils.toByteArray(in);
				customerRequirementDTO.setLandImagePNGorJPGFileFormat(media);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//List<Customer> customer = GetCustomerByCustomerId(customerRequirementEntity.getCustomerId());
		if (customerRequirementEntity.getCustomerForCustomerRequirement() != null) {
			customerRequirementDTO.setCustomerForCustomerRequirement(
					setCustomerDTOWithoutRequirement(customerRequirementEntity.getCustomerForCustomerRequirement()));
		}
		//List<AmenitiesAndSpecifications> amenitiesAndSpecifications = GetAmenityAndSpecificationByAmenityId(customerRequirementEntity.getAmenityAndSpecifiactionId());
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO
					.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}
	
	public List<AmenitiesAndSpecifications> GetAmenityAndSpecificationByAmenityId(int amenityId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
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

		
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		return amenitiesAndSpecifications;
	}
	
	public List<Customer> GetCustomerByCustomerId(int customerId) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<Customer> customer = new ArrayList<Customer>();

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
		customer = query.getResultList();

		
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		return customer;
	}

	public static void copyBuildersEstimateBasicEntityToDTO(BuildersEstimate buildersEstimatetEntity,
			BuildersEstimateDTO buildersEstimateDTO) {
		final Set<String> prop = new HashSet<>(Arrays.asList("buildersEstimateId", "customerRequirementId", "builderId",
				"perSquareFeetCost", "detailedEstimateFilePath", "customerAcceptedDeclined", "customerReview",
				"customerReviewStarRating", "projectCompletionDurationInDays"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(buildersEstimateDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(buildersEstimatetEntity, buildersEstimateDTO, excludedProperties);
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

	public static void copyCustomerRequirementBasicDTOToEntity(CustomerRequirementDTO customerRequirementDTO,
			CustomerRequirement CustomerRequirementEntity) {
		final Set<String> prop = new HashSet<>(Arrays.asList("customerRequirementId", "customerId",
				"amenityAndSpecifiactionId", "projectBudgetFullHouseConstructionWithMaterial", "requirementStatus", "bhkCount", "totalSquareFeet", "totalWallSquareFeet",
				"planImagePath", "landImagePath", "brickType", "pillerBeamRequired", "floorType", "woodType",
				"paintCoatCount", "paintWallPuttyCount", "paintBrand", "paintQuality", "plumbingBrand",
				"electricalBrand", "cementBrand", "steelBrand", "tilesFloorWallBrand", "state", "district", "doorNumber", "streetFirst", "streetSecond", "landmark", "city", "pincode", "country"));
		String[] excludedProperties = Arrays
				.stream(BeanUtils.getPropertyDescriptors(CustomerRequirementEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerRequirementDTO, CustomerRequirementEntity, excludedProperties);
	}

	public static void copySiteLocationBasicDTOToEntity(SiteLocationDTO siteLocationDTO,
			SiteLocation siteLocationEntity) {
		final Set<String> prop = new HashSet<>(
				Arrays.asList("siteLocationId", "customerRequirementId", "siteLocationFilePath", "videoFilePath"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(siteLocationEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(siteLocationDTO, siteLocationEntity, excludedProperties);
	}

	public static void copyCustomerRequirementBasicEntityToDTO(CustomerRequirement CustomerRequirementEntity,
			CustomerRequirementDTO customerRequirementDTO) {
		final Set<String> prop = new HashSet<>(Arrays.asList("customerRequirementId", "customerId",
				"amenityAndSpecifiactionId", "requirementStatus", "bhkCount", "totalSquareFeet", "totalWallSquareFeet",
				"planImagePath", "landImagePath", "brickType", "pillerBeamRequired", "floorType", "woodType",
				"paintCoatCount", "paintWallPuttyCount", "paintBrand", "paintQuality", "plumbingBrand",
				"electricalBrand", "cementBrand", "steelBrand", "tilesFloorWallBrand", "state", "district", "doorNumber", "streetFirst", "streetSecond", "landmark", "city", "pincode", "country"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(customerRequirementDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(CustomerRequirementEntity, customerRequirementDTO, excludedProperties);
	}

	public static void copySiteLocationBasicEntityToDTO(SiteLocation siteLocation, SiteLocationDTO siteLocationDTO) {
		final Set<String> prop = new HashSet<>(
				Arrays.asList("siteLocationId", "customerRequirementId", "siteLocationFilePath", "videoFilePath"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(siteLocationDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(siteLocation, siteLocationDTO, excludedProperties);
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

	public char[] GenerateCustomersOTP(int len) {
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

	public void saveCustomerOTP(CustomerDTO customerDTO) {
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();

		CustomerOtp customerOtp = new CustomerOtp();

		int otpDigits = 4;
		char[] otpGeneratedForBuilder = GenerateCustomersOTP(otpDigits);
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

		customerOtp.setCustomerPhoneNumber(customerDTO.getPhoneCustomer());
		customerOtp.setCustomerOtpNumber(Integer.parseInt(otpGeneratedForBuilderConcated.substring(4)));

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(customerOtp)) {
			// BuilderOtp entityAvailableOrNot = entityManager.find(BuilderOtp.class,
			// builderOtp.getBuilderPhoneNumber());
			// EntityManager entityManager = em.getEntityManager("builder");
			List<CustomerOtp> customerOtps = new ArrayList<CustomerOtp>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<CustomerOtp> criteria = builder.createQuery(CustomerOtp.class);
			Root<CustomerOtp> rootBuilder = criteria.from(CustomerOtp.class);
			criteria.select(rootBuilder);

			List<Predicate> restrictions = new ArrayList<Predicate>();
			restrictions
					.add(builder.equal(rootBuilder.get("customerPhoneNumber"), customerOtp.getCustomerPhoneNumber()));

			criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
			TypedQuery<CustomerOtp> query = entityManager.createQuery(criteria);
			query.setHint(QueryHints.HINT_CACHEABLE, true);
			query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
			customerOtps = query.getResultList();
			if (customerOtps.isEmpty()) {
				// if (1 == 1) {
				// persist object - add to entity manager
				entityManager.persist(customerOtp);
				// flush em - save to DB
				entityManager.flush();
			} else {
				customerOtp.setCustomerOtpId(customerOtps.get(0).getCustomerOtpId());
				entityManager.merge(customerOtp);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();
		
		String response = webClient.get().uri(
				"/smsapi.aspx?uid=marilabor&pwd=11985&mobile="+customerOtp.getCustomerPhoneNumber()+"&msg=Please use the OTP- "+customerOtp.getCustomerOtpNumber()+" to complete Customer registration. - Mari Labor Estimates&sid=MARILE&type=0&dtTimeNow=09:00:57&entityid=1601819176286494692&tempid=1607100000000366850") // Appends
																																																																// to
																																																																// the
																																																																// base
																																																																// URL
																																																																// configured
																																																																// in
																																																																// the
																																																																// bean
				.retrieve() // Initiate the request and retrieve the response
				.bodyToMono(String.class) // Specify the expected response body type as a Mono
				.block(); // Block to get the result synchronously (useful in non-reactive services)

	}

	public boolean VerifyCustomersOTP(CustomerDTO customerDTO, String otp) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<CustomerOtp> customerOtp = new ArrayList<CustomerOtp>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CustomerOtp> criteria = builder.createQuery(CustomerOtp.class);
		Root<CustomerOtp> rootBuilder = criteria.from(CustomerOtp.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("customerPhoneNumber"), customerDTO.getPhoneCustomer()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerOtp> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerOtp = query.getResultList();

		int otpDB = customerOtp.get(0).getCustomerOtpNumber();
		int otpCustomerEnered = Integer.parseInt(otp.replace("\"", ""));

		if (otpDB == otpCustomerEnered) {
			return true;
		} else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

	public boolean VerifyAlreadyRegisteredCustomer(CustomerDTO customerDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Customer> customerEntity = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phoneCustomer"), customerDTO.getPhoneCustomer()));

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

	public boolean VerifyMobileAlreadyRegisteredAsBuilder(CustomerDTO customerDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Builder> builderEntity = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phone"), customerDTO.getPhoneCustomer()));

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

}
