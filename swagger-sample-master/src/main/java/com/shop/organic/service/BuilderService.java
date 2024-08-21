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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.District;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
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
public class BuilderService {

	// @Autowired
	// private CarRepository carRepository;

	// @Value("${server.port}")
	private String port;

	@Autowired
	private CreateEntityManager em;

	// @Autowired
	private ExecutorService threadpoolToGtetAllStates;

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
	private List<BuilderDTO> builderDTOList = new ArrayList<BuilderDTO>();
	// private List<CategoryDTO> catgDTOList;

	// @Async
	public List<BuilderDTO> findBuildersList(int amenitiesAndSpecificationsId) {
		// return
		// categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		// carEntityList=carRepository.findAll();
		List<BuilderDTO> response = null;
		EntityManager entityManager = em.getEntityManager("builder");

		builderDTOList.clear();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuildersAvailableAmenities> criteria = builder.createQuery(BuildersAvailableAmenities.class);
		Root<BuildersAvailableAmenities> rootBuilder = criteria.from(BuildersAvailableAmenities.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("amenitiesAndSpecificationsId"), amenitiesAndSpecificationsId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<BuildersAvailableAmenities> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		buildersAvailableAmenities = query.getResultList();

		if (buildersAvailableAmenities.isEmpty()) {
			throw new ResourceNotFoundException("Builder: " + amenitiesAndSpecificationsId + " not Found...");
		}
		int i = 0;
		// response =
		// builderEntityListAmenities.get(0).getBuilder().stream().map(builders->
		// setBuilderDTO(builders,
		// getAmenitiesAndSpecificationsById(builderEntityListAmenities.get(0).getAmenitiesAndSpecificationsId()))).collect(Collectors.toList());
		response = buildersAvailableAmenities.stream()
				.map(buildersAvailableAmenities -> setBuilderDTO(getBuildersById(buildersAvailableAmenities.getBuilderId())))
				.collect(Collectors.toList());
		// entityManager.close();
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

		return allStatesDTO;

	}

	public Builder registerBuilder(BuilderDTO builderDTO) {
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

		System.out.println("builderEntity.getBuilderId()::::" + builderEntity.getBuilderId());
		System.out
				.println("builderEntity.getAddress().getAddressId())::::" + builderEntity.getAddress().getAddressId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);

		return builderEntity;
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

		System.out.println("builderEntity.getBuilderId()::::" + buildersAvailableAmenities.getBuilderId());
		System.out.println("builderEntity.getAddress().getAddressId())::::"
				+ buildersAvailableAmenities.getAmenitiesAndSpecificationsId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
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

		System.out.println("builderEntity.getBuilderId()::::" + projectsAvailableAmenities.getProjectId());
		System.out.println("builderEntity.getAddress().getAddressId())::::"
				+ projectsAvailableAmenities.getAmenitiesAndSpecificationsId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}

	public Projects addNewProject(ProjectsDTO projectDTO) {
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

		System.out.println("builderEntity.getBuilderId()::::" + projectEntity.getProjectId());
		System.out.println("builderEntity.getAddress().getAddressId())::::" + projectEntity.getBuilderId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);

		return projectEntity;
	}

	public ProjectsDTO getProjectDetailsById(ProjectsDTO projectDTO) {
		ProjectsDTO responseProjectDTO = new ProjectsDTO();
		Projects projectEntityResponse = new Projects();
		Builder builderEntity = new Builder();

		Projects projectEntity = setProjectEntity(projectDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		// if (entityManager.contains(projectEntity)) {
		projectEntityResponse = entityManager.find(Projects.class, projectEntity.getProjectId());
		builderEntity = entityManager.find(Builder.class, projectEntityResponse.getBuilderId());
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();

		System.out.println("builderEntity.getBuilderId()::::" + projectEntity.getProjectId());
		System.out.println("builderEntity.getAddress().getAddressId())::::" + projectEntity.getBuilderId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);

		return setProjectDTO(projectEntityResponse, builderEntity);
	}

	public Picture addNewPicture(PictureDTO pictureDTO) {
		ProjectsDTO responseProjectDTO = new ProjectsDTO();

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

		System.out.println("pictureEntity.getPictureId()::::" + pictureEntity.getPictureId());
		System.out.println("pictureEntity.getProjectId()::::" + pictureEntity.getProjectId());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);

		return pictureEntity;
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
				projectDTO.setProjMainPicFilePath(finalPath.concat("/").concat(fileName));
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

	public BuilderDTO sendOTP(BuilderDTO builderDTO) {
		BuilderDTO LoginBuilderDTO = new BuilderDTO();
		List<Builder> LoginBuilder = new ArrayList<Builder>();
		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT b FROM Builder b", Builder.class);
		// q.setParameter("keyword", keyword); //etc
		builderEntityList = q.getResultList();

		System.out.println("LoginBuilderDTO" + builderEntityList.get(0).getPhone());
		System.out.println("builderDTO.getPhone()" + builderDTO.getPhone());

		LoginBuilder = builderEntityList.stream()
				.filter(builder -> builder.getPhone().equalsIgnoreCase(builderDTO.getPhone()))
				.collect(Collectors.toList());

		if (LoginBuilder.isEmpty() && LoginBuilder.size() == 0) {
			throw new ResourceNotFoundException("Mobile Number: " + builderDTO.getPhone() + " not Registered...");
		}

		if (!LoginBuilder.isEmpty()) {

			LoginBuilderDTO = setBuilderDTO(LoginBuilder.get(0));
		}
		// System.out.println("LoginBuilderDTO" +new Gson().toJson(LoginBuilderDTO));
		return LoginBuilderDTO;
	}

	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsById(int amenitiesAndSpecificationsId) {
		AmenitiesAndSpecifications amenitiesAndSpecifications = new AmenitiesAndSpecifications();

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		amenitiesAndSpecifications = entityManager.find(AmenitiesAndSpecifications.class, amenitiesAndSpecificationsId);

		// commit transaction at all
		entityManager.getTransaction().commit();

		return amenitiesAndSpecifications;
	}

	public Builder getBuildersById(int builderId) {
		Builder builder = new Builder();

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		builder = entityManager.find(Builder.class, builderId);

		// commit transaction at all
		entityManager.getTransaction().commit();

		return builder;
	}

	public List<BuilderDTO> getBuilderById(String builderId) {
		EntityManager entityManager = em.getEntityManager("car");

		builderDTOList.clear();
		int i = 0;

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("carId"), builderId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderEntityList = query.getResultList();

		if (builderEntityList.isEmpty()) {
			throw new ResourceNotFoundException("Builder: " + builderId + " not Found...");
		}
		System.out.println("carEntityList.size()::::" + builderEntityList.size());
		return builderEntityList.stream().map(this::setBuilderDTO).collect(Collectors.toList());
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
		final Set<String> prop = new HashSet<>(
				Arrays.asList("projectId", "builderId", "estimateCost", "areaInSquareFeet", "projMainPicFilePath"));
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
		builderDTO.setAddress(this.copyAddressEntityToDto(builderEntity.getAddress()));

		if (builderEntity.getProjects() != null && !builderEntity.getProjects().isEmpty()) {
			builderDTO.setProjects(builderEntity.getProjects().stream()
					.map(project -> setProjectDTO(project, builderEntity)).collect(Collectors.toList()));
		}
		// List<BuildersAvailableAmenities> buildersAvailableAmenities=
		// getAllBuildersAvaiableAmenitiesByBuilderid(builderEntity.getBuilderId());
		if (builderEntity.getBuildersAvailableAmenities() != null && !builderEntity.getBuildersAvailableAmenities().isEmpty()) {
			builderDTO
			.setBuildersAvailableAmenities(builderEntity.getBuildersAvailableAmenities().stream()
					.map(builderAvailableAmenities -> this.copyBuildersBasicAvailableAmenitiesEntityToDTO(
							builderAvailableAmenities, new BuildersAvailableAmenitiesDTO()))
					.collect(Collectors.toList()));
		}
		

		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "builderName", "manufacturingCompany",
				"projectType", "phone", "userName", "password", "amenityAndSpecificationId"));
		this.copyBuilderBasicEntityToDTO(builderEntity, builderDTO, prop);
		// carDTOList.add(carDTO);
		return builderDTO;
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

	// @Async
	public ProjectsDTO setProjectDTO(Projects projectEntity, Builder builderEntity) {
		ProjectsDTO projectDTO = new ProjectsDTO();
		HttpServletResponse response = null;
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "builderId", "amenitiesAndSpecificationsId",
				"estimateCost", "areaInSquareFeet", "projMainPicFilePath"));
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
		if (builderEntity != null) {
			projectDTO.setBuilder(setBuilderDTOWithoutProject(builderEntity));
		}
		if (projectEntity.getPicture() != null && !projectEntity.getPicture().isEmpty()) {
			projectDTO.setPicture(
					projectEntity.getPicture().stream().map(this::setPictureDTO).collect(Collectors.toList()));
		}
		if (projectEntity.getProjectsAvailableAmenities() != null) {
			projectDTO.setProjectsAvailableAmenities(projectEntity.getProjectsAvailableAmenities().stream()
					.map(projectsAvailableEntities -> this.copyProjectsBasicAvailableAmenitiesEntityToDTO(
							projectsAvailableEntities, new ProjectsAvailableAmenitiesDTO()))
					.collect(Collectors.toList()));
		}

		// carDTOList.add(carDTO);
		return projectDTO;
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

	public StateDTO setStateDTOThreadExecution(State stateEntity) {
		StateDTO stateDTO = new StateDTO();
		stateDTO.setDistrict(stateEntity.getDistrict().stream().map(district -> setDistrictDTO(district))
				.collect(Collectors.toList()));
		final Set<String> prop = new HashSet<>(Arrays.asList("stateId", "stateName"));
		this.copyStateBasicEntityToDTO(stateEntity, stateDTO, prop);
		// carDTOList.add(carDTO);
		return stateDTO;
	}

	public DistrictDTO setDistrictDTO(District districtEntity) {
		DistrictDTO districtDTO = new DistrictDTO();

		final Set<String> prop = new HashSet<>(Arrays.asList("districtId", "districtName"));
		this.copyDistrictBasicEntityToDTO(districtEntity, districtDTO, prop);
		// carDTOList.add(carDTO);
		return districtDTO;
	}

	private ProjectsDTO copyProjectsEntityToDto(Projects projectEntity) {
		ProjectsDTO projectDTO = new ProjectsDTO();
		// projectDTO.setBuilder(this.setBuilderDTO(projectEntity.getBuilder()));
		projectDTO.setPicture(
				projectEntity.getPicture().stream().map(this::copyPictureEntityToDto).collect(Collectors.toList()));
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

	public static void copyDistrictBasicEntityToDTO(District districtEntity, DistrictDTO districtDTO,
			Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(districtEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(districtEntity, districtDTO, excludedProperties);
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
		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "amenitiesAndSpecificationsId"));

		/*BuildersAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
				copyAmenityAndSpecificationsEntityToDTO(getAmenitiesAndSpecificationsByAmenityid(
						BuildersAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));*/
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
		/*projectsAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
				copyAmenityAndSpecificationsEntityToDTO(getAmenitiesAndSpecificationsByAmenityid(
						projectsAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));*/
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

}
