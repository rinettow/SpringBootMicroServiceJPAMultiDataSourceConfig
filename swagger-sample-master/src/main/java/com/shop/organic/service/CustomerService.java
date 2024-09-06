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
import java.util.HashSet;
import java.util.List;
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
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.BuildersEstimate;
import com.shop.organic.entity.car.Customer;
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.District;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
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

	// @Value("${server.port}")
	private String port;

	@Autowired
	private CreateEntityManager em;
	
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
	
	public void ceateImageDirectoryForCustomerRequirement(CustomerRequirementDTO CustomerRequirementDTO, MultipartFile planPDFFileFormat,
			MultipartFile landImagePNGorJPGFileFormat) {
		System.out.println("ceateImageDirectoryForCustomerRequirement");

		String path = "C:/Users/User/GitHub Repository/CustomersImage/";
		String finalPath = path.concat("Customer").concat(Integer.toString(CustomerRequirementDTO.getCustomerId())).concat("/")
				.concat("CustomerRequirement").concat(Integer.toString(CustomerRequirementDTO.getCustomerRequirementId()));
		if (!new File(finalPath).exists()) {
			System.out.println("ceateImageDirectoryForCustomerRequirement");
			new File(finalPath).mkdir();
			try {
				String planPDFFileFormatfileName = planPDFFileFormat.getOriginalFilename();
				String landImagePNGorJPGFileFormatfileName = landImagePNGorJPGFileFormat.getOriginalFilename();
				if(!planPDFFileFormatfileName.isEmpty()) {
					planPDFFileFormat.transferTo(new File(finalPath.concat("/").concat(planPDFFileFormatfileName)));
					CustomerRequirementDTO.setPlanImagePath(finalPath.concat("/").concat(planPDFFileFormatfileName));
				}
				
				if(!landImagePNGorJPGFileFormatfileName.isEmpty()) {
					landImagePNGorJPGFileFormat.transferTo(new File(finalPath.concat("/").concat(landImagePNGorJPGFileFormatfileName)));
					CustomerRequirementDTO.setLandImagePath(finalPath.concat("/").concat(landImagePNGorJPGFileFormatfileName));
				}
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



	public Customer setCustomerEntity(CustomerDTO customerDTO) {
		Customer customerEntity = new Customer();

		final Set<String> prop = new HashSet<>(Arrays.asList("customerName", "phoneCustomer"));
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

		final Set<String> prop = new HashSet<>(Arrays.asList("customerId", "customerName", "phoneCustomer"));
		this.copyCustomerBasicEntityToDTO(customerEntity, customerDTO, prop);
		
		if(customerEntity.getCustomerRequirement() != null && !customerEntity.getCustomerRequirement().isEmpty()) {
		 customerDTO.setCustomerRequirement(customerEntity.getCustomerRequirement()
				 .stream().map(this::setCustomerRequirementDTO).collect(Collectors.toList()));
		}
		// carDTOList.add(carDTO);
		return customerDTO;
	}
	
	public CustomerDTO setCustomerDTOWithoutRequirement(Customer customerEntity) {
		CustomerDTO customerDTO = new CustomerDTO();

		final Set<String> prop = new HashSet<>(Arrays.asList("customerId", "customerName", "phoneCustomer"));
		this.copyCustomerBasicEntityToDTO(customerEntity, customerDTO, prop);
		
		return customerDTO;
	}

	public static void copyCustomerBasicEntityToDTO(Customer customerEntity, CustomerDTO customerDTO,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(customerDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerEntity, customerDTO, excludedProperties);
	}

	public CustomerDTO sendOTPForCustomerLogin(CustomerDTO customerDTO) {
		CustomerDTO LoginCustomerDTO = new CustomerDTO();
		List<Customer> LoginCustomer = new ArrayList<Customer>();
		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT c FROM Customer c", Customer.class);
		// q.setParameter("keyword", keyword); //etc
		LoginCustomer = q.getResultList();

		System.out.println("LoginBuilderDTO" + LoginCustomer.get(0).getPhoneCustomer());

		LoginCustomer = LoginCustomer.stream()
				.filter(customer -> customer.getPhoneCustomer().equalsIgnoreCase(customerDTO.getPhoneCustomer()))
				.collect(Collectors.toList());

		if (LoginCustomer.isEmpty() && LoginCustomer.size() == 0) {
			throw new ResourceNotFoundException(
					"Mobile Number: " + customerDTO.getPhoneCustomer() + " not Registered...");
		}

		if (!LoginCustomer.isEmpty()) {

			LoginCustomerDTO = setCustomerDTO(LoginCustomer.get(0));
		}
		entityManager.close();
		return LoginCustomerDTO;
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
		restrictions.add(builder.equal(rootBuilder.get("amenityAndSpecifiactionId"), customerRequirementDTO.getAmenityAndSpecifiactionId()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<CustomerRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerRequirementEntity = query.getResultList();

		if (!customerRequirementEntity.isEmpty()) {
			customerRequirementEntityOpenStatus = customerRequirementEntity.stream().filter(custRequirement-> custRequirement.getRequirementStatus().equals("OPEN")).collect(Collectors.toList());
			if(!customerRequirementEntityOpenStatus.isEmpty()) {
				isOpenRequirementAvailable = true;
				//throw new ResourceNotFoundException("Open Requirement is already available for " +amenityName+ "Please close the existing requirement");
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
			customerRequirementEntityOpenStatus = customerRequirementEntity.stream().filter(custRequirement-> custRequirement.getRequirementStatus().equals("OPEN")).collect(Collectors.toList());
			if(!customerRequirementEntityOpenStatus.isEmpty()) {
				isOpenRequirementAvailable = true;
				//throw new ResourceNotFoundException("Open Requirement is already available for " +amenityName+ "Please close the existing requirement");
			}
			
		}
		customerRequirementDTOOpenStatus = customerRequirementEntityOpenStatus.stream().map(require ->setCustomerRequirementDTO(require)).collect(Collectors.toList());
		entityManager.close();
		return customerRequirementDTOOpenStatus;
	}
	
	
	public CustomerRequirementDTO CreateCustomerRequirement(CustomerRequirementDTO customerRequirementDTO) {
		CustomerRequirementDTO customerRequirementDTOResponse = new CustomerRequirementDTO();
		CustomerRequirement customerRequirementEntity = new CustomerRequirement();
		copyCustomerRequirementBasicDTOToEntity(customerRequirementDTO, customerRequirementEntity);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		
		if (!entityManager.contains(customerRequirementEntity)) {
			CustomerRequirement entityAvailableOrNot = entityManager.find(CustomerRequirement.class, customerRequirementEntity.getCustomerRequirementId());
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
		customerRequirementDTOResponse= this.setCustomerRequirementDTO(customerRequirementEntity);
		entityManager.close();
		return customerRequirementDTOResponse;
	}
	
	public BuildersEstimateDTO addBuildersEstimateEntry(int customerOpenRequirementId, int customerId, int amenitiesAndSpecificationId, int projectId, int builderId) {
		BuildersEstimate buildersEstimate = new BuildersEstimate();
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimate.setCustomerRequirementId(customerOpenRequirementId);
		buildersEstimate.setProjectId(projectId);
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
		if (customerRequirementEntity.getCustomerForCustomerRequirement() != null) {
			customerRequirementDTO.setCustomerForCustomerRequirement(setCustomerDTOWithoutRequirement(customerRequirementEntity.getCustomerForCustomerRequirement()));
		}
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		if (customerRequirementEntity.getBuildersEstimate() != null && !customerRequirementEntity.getBuildersEstimate().isEmpty()) {
			customerRequirementDTO.setBuildersEstimate(customerRequirementEntity.getBuildersEstimate().stream().map(this::setBuilderEstimateDTO).collect(Collectors.toList()));
		}
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}
	
	public BuildersEstimateDTO setBuilderEstimateDTO(BuildersEstimate buildersEstimateEntity) {
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		this.copyBuildersEstimateBasicEntityToDTO(buildersEstimateEntity, buildersEstimateDTO);
		
		if (buildersEstimateEntity.getCustomerRequirementForBuildersEstimate() != null) {
			buildersEstimateDTO.setCustomerRequirementDTO(setCustomerRequirementDTOWithoutBuilderEstimate(buildersEstimateEntity.getCustomerRequirementForBuildersEstimate()));
		}
		
		if (buildersEstimateEntity.getProjectForBuildersEstimate() != null) {
			buildersEstimateDTO.setProjectDTO(builderService.setProjectDTO(buildersEstimateEntity.getProjectForBuildersEstimate()));
		}
		
		if (buildersEstimateEntity.getBuilderForBuildersEstimate() != null) {
			buildersEstimateDTO.setBuilderDTO(builderService.setBuilderDTO(buildersEstimateEntity.getBuilderForBuildersEstimate()));
		}
		// carDTOList.add(carDTO);
		return buildersEstimateDTO;
	}
	
	public BuildersEstimateDTO setBuilderEstimateDTObymanualCustomerRequirementPicking(BuildersEstimate buildersEstimateEntity) {
		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		this.copyBuildersEstimateBasicEntityToDTO(buildersEstimateEntity, buildersEstimateDTO);
		
		
			buildersEstimateDTO.setCustomerRequirementDTO(getCustomerRequirementById(buildersEstimateEntity.getCustomerRequirementId()));
		
		if (buildersEstimateEntity.getProjectForBuildersEstimate() != null) {
			buildersEstimateDTO.setProjectDTO(builderService.setProjectDTO(buildersEstimateEntity.getProjectForBuildersEstimate()));
		}
		
		if (buildersEstimateEntity.getBuilderForBuildersEstimate() != null) {
			buildersEstimateDTO.setBuilderDTO(builderService.setBuilderDTOWithoutProject(buildersEstimateEntity.getBuilderForBuildersEstimate()));
		}
		// carDTOList.add(carDTO);
		return buildersEstimateDTO;
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
		custRequirementDTO = setCustomerRequirementDTOWithoutBuilderEstimate(custRequirement.get(0));
		entityManager.close();
		return custRequirementDTO;
	}
	
	public CustomerRequirementDTO setCustomerRequirementDTOWithoutBuilderEstimate(CustomerRequirement customerRequirementEntity) {
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
		if (customerRequirementEntity.getCustomerForCustomerRequirement() != null) {
			customerRequirementDTO.setCustomerForCustomerRequirement(setCustomerDTOWithoutRequirement(customerRequirementEntity.getCustomerForCustomerRequirement()));
		}
		if (customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement() != null) {
			customerRequirementDTO.setAmenitiesAndSpecificationsForCustomerRequirement(copyAmenitiesAndSpecificationEntityToDto(customerRequirementEntity.getAmenitiesAndSpecificationsForCustomerRequirement()));
		}
		// carDTOList.add(carDTO);
		return customerRequirementDTO;
	}
	
	public static void copyBuildersEstimateBasicEntityToDTO(BuildersEstimate buildersEstimatetEntity, BuildersEstimateDTO buildersEstimateDTO) {
		final Set<String> prop = new HashSet<>(Arrays.asList("buildersEstimateId",
				"customerRequirementId",
				"projectId",
				"builderId",
				"perSquareFeetCost",
				"detailedEstimateFilePath"));
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
	
	
	public static void copyCustomerRequirementBasicDTOToEntity(CustomerRequirementDTO customerRequirementDTO, CustomerRequirement CustomerRequirementEntity) {
		final Set<String> prop = new HashSet<>(Arrays.asList("customerRequirementId", "customerId", "amenityAndSpecifiactionId", "requirementStatus", "bhkCount", "totalSquareFeet", "totalWallSquareFeet", 
				"planImagePath", "landImagePath", "brickType", "pillerBeamRequired", "floorType", "woodType", "paintCoatCount", "paintWallPuttyCount",
				"paintBrand", "paintQuality", "plumbingBrand", "electricalBrand", "cementBrand", "steelBrand", "tilesFloorWallBrand"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(CustomerRequirementEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerRequirementDTO, CustomerRequirementEntity, excludedProperties);
	}
	
	public static void copyCustomerRequirementBasicEntityToDTO(CustomerRequirement CustomerRequirementEntity, CustomerRequirementDTO customerRequirementDTO) {
		final Set<String> prop = new HashSet<>(Arrays.asList("customerRequirementId", "customerId", "amenityAndSpecifiactionId", "requirementStatus", "bhkCount", "totalSquareFeet", "totalWallSquareFeet", 
				"planImagePath", "landImagePath", "brickType", "pillerBeamRequired", "floorType", "woodType", "paintCoatCount", "paintWallPuttyCount",
				"paintBrand", "paintQuality", "plumbingBrand", "electricalBrand", "cementBrand", "steelBrand", "tilesFloorWallBrand"));
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(customerRequirementDTO.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(CustomerRequirementEntity, customerRequirementDTO, excludedProperties);
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


}
