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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.util.CreateEntityManager;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;

import java.net.MalformedURLException;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Service
@Transactional
//@ConfigurationProperties("application-dev")
public class BuilderService {
	
	//@Autowired
	//private CarRepository carRepository;
	
	//@Value("${server.port}")
    private String port;

	
	@Autowired
	private CreateEntityManager em;
	
	private enum ResourceType {
		FILE_SYSTEM,
		CLASSPATH
	}
    
	private List<Builder> builderEntityList;
	private List<BuilderDTO> builderDTOList=new ArrayList<BuilderDTO>();
	//private List<CategoryDTO> catgDTOList;
	
	public List<BuilderDTO> findBuildersList() {
		//return categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		//carEntityList=carRepository.findAll();
		
		EntityManager entityManager = em.getEntityManager("builder");
		Query q = entityManager.createQuery("SELECT b FROM Builder b", Builder.class);
	    //q.setParameter("keyword", keyword); //etc
		builderEntityList= q.getResultList();
		System.out.println("carEntityList.size()::::" +builderEntityList.size()+ port);
		
		builderDTOList.clear();
		int i=0;
		
		return builderEntityList.stream().map(this::setBuilderDTO).collect(Collectors.toList());
	}
	
	public Builder registerBuilder(BuilderDTO builderDTO) {
		BuilderDTO responseBuilderDTO =new BuilderDTO();
		
		Builder builderEntity= setBuilderEntity(builderDTO);
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
		
		System.out.println("builderEntity.getBuilderId()::::" +builderEntity.getBuilderId());
		System.out.println("builderEntity.getAddress().getAddressId())::::" +builderEntity.getAddress().getAddressId());
		
		
		//responseBuilderDTO= this.setBuilderDTO(builderEntity);
		
		return builderEntity;
	}
	
	public Projects addNewProject(ProjectsDTO projectDTO) {
		ProjectsDTO responseProjectDTO =new ProjectsDTO();
		
		Projects projectEntity= setProjectEntity(projectDTO);
		EntityManager entityManager = em.getEntityManager("builder");
		
		
		entityManager.getTransaction().begin();
	    if (!entityManager.contains(projectEntity)) {
	    	Projects entityAvailableOrNot=entityManager.find(Projects.class, projectEntity.getProjectId());
	    	if(entityAvailableOrNot == null) {
	    		// persist object - add to entity manager
		    	entityManager.persist(projectEntity);
		        // flush em - save to DB
		    	entityManager.flush();
	    	}else {
		    	entityManager.merge(projectEntity);
		    }
	        	
	        
	    }
	    // commit transaction at all
	    entityManager.getTransaction().commit();
		
		System.out.println("builderEntity.getBuilderId()::::" +projectEntity.getProjectId());
		System.out.println("builderEntity.getAddress().getAddressId())::::" +projectEntity.getBuilderId());
		
		
		//responseBuilderDTO= this.setBuilderDTO(builderEntity);
		
		return projectEntity;
	}
	
	public void ceateImageDirectoryForBuilder(BuilderDTO builderDTO) {
		System.out.println("ceateImageDirectoryForBuilder");
		 
		String path= "C:/Users/User/GitHub Repository/BuildersImage/";
		String finalPath= path.concat("Builder").concat(Integer.toString(builderDTO.getBuilderId()));
		if(! new File(finalPath).exists())
	    {
	    	System.out.println("ceateImageDirectoryForBuilder2");
	        new File(finalPath).mkdir();
	    }
	    System.out.println("realPathtoUploads = {}" +finalPath);
	}
	
	public void ceateImageDirectoryForProject(ProjectsDTO projectDTO, MultipartFile multipartFile) {
		System.out.println("ceateImageDirectoryForBuilder");
		 
		String path= "C:/Users/User/GitHub Repository/BuildersImage/";
		String finalPath= path.concat("Builder").concat(Integer.toString(projectDTO.getBuilderId()))
				.concat("/").concat("Project").concat(Integer.toString(projectDTO.getProjectId()));
		if(! new File(finalPath).exists())
	    {
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
		
		String childImagePath= path.concat("Builder").concat(Integer.toString(projectDTO.getBuilderId()))
				.concat("/").concat("Project").concat(Integer.toString(projectDTO.getProjectId())).concat("/ChildImages");
		if(! new File(childImagePath).exists())
	    {
	    	System.out.println("ceateImageDirectoryForChildImage");
	        new File(childImagePath).mkdir();
	    }
	    System.out.println("realPathtoUploads = {}" +finalPath);
	}
	
	
	public BuilderDTO sendOTP(BuilderDTO builderDTO) {
		BuilderDTO LoginBuilderDTO =new BuilderDTO();
		List<Builder> LoginBuilder= new ArrayList<Builder>();
		EntityManager entityManager = em.getEntityManager("builder");
		
		Query q = entityManager.createQuery("SELECT b FROM Builder b", Builder.class);
	    //q.setParameter("keyword", keyword); //etc
		builderEntityList= q.getResultList();
		
		System.out.println("LoginBuilderDTO" +builderEntityList.get(0).getPhone());
		System.out.println("builderDTO.getPhone()" +builderDTO.getPhone());
		
		LoginBuilder= builderEntityList.stream().filter(builder -> builder.getPhone().equalsIgnoreCase(builderDTO.getPhone()))
		  .collect(Collectors.toList());
		
		if(LoginBuilder.isEmpty() && LoginBuilder.size() == 0) {
			throw new ResourceNotFoundException("Mobile Number: " +builderDTO.getPhone()+ " not Registered..." );
		}
		
		if(!LoginBuilder.isEmpty()) {
			
			LoginBuilderDTO= setBuilderDTO(LoginBuilder.get(0));
		}
		System.out.println("LoginBuilderDTO" +new Gson().toJson(LoginBuilderDTO));
		return LoginBuilderDTO;
	}
	
	
	
	public List<BuilderDTO> getBuilderById(String builderId) {
		EntityManager entityManager = em.getEntityManager("car");
		
		builderDTOList.clear();
		int i=0;
		
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
              throw new ResourceNotFoundException("Builder: " +builderId+ " not Found..." );
          }
		  System.out.println("carEntityList.size()::::" +builderEntityList.size());
		return builderEntityList.stream().map(this::setBuilderDTO).collect(Collectors.toList());
	}
	
	
	
	public Builder setBuilderEntity(BuilderDTO builderDTO) {
		Builder builderEntity =new Builder();
		
		builderEntity.setAddress(this.copyAddressDTOToEntity(builderDTO.getAddress()));
		
		final Set<String> prop = new HashSet<>(Arrays.asList("builderName", "manufacturingCompany", 
				  "phone", "userName", "password"));
		this.copyBuilderBasicDTOToEntity(builderDTO, builderEntity, prop);
		
		
		return builderEntity;
	}
	
	public Projects setProjectEntity(ProjectsDTO projectDTO) {
		Projects projectEntity =new Projects();
		
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "builderId", "amenitiesAndSpecificationsId", "estimateCost", 
				  "areaInSquareFeet", "projMainPicFilePath"));
		this.copyProjectsBasicDTOToEntity(projectDTO, projectEntity, prop);
		
		return projectEntity;
	}
	
	public BuilderDTO setBuilderDTO(Builder builderEntity){
		BuilderDTO builderDTO= new BuilderDTO();
		builderDTO.setAddress(this.copyAddressEntityToDto(builderEntity.getAddress()));
		builderDTO.setProjects(builderEntity.getProjects().stream().map(this::setProjectDTO).collect(Collectors.toList()));
		final Set<String> prop = new HashSet<>(Arrays.asList("builderId", "builderName", 
				  "projectType", "manufacturingCompany", 
				  "phone"));
		this.copyBuilderBasicEntityToDTO(builderEntity, builderDTO, prop);
		//carDTOList.add(carDTO);
		return builderDTO;
	}
	
	public ProjectsDTO setProjectDTO(Projects projectEntity){
		ProjectsDTO projectDTO= new ProjectsDTO();
		HttpServletResponse response = null;
		final Set<String> prop = new HashSet<>(Arrays.asList("projectId", "builderId", "amenitiesAndSpecificationsId", "estimateCost", 
				  "areaInSquareFeet", "projMainPicFilePath"));
		this.copyProjectsBasicEntityToDTO(projectEntity, projectDTO, prop);
		if(projectEntity.getProjMainPicFilePath() != null) {
		//projectDTO.setImage(this.getFileSystem(projectEntity.getProjMainPicFilePath(), response));
		ServletContext sc = null;
		 //InputStream in = sc.getResourceAsStream(projectEntity.getProjMainPicFilePath());
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
		//carDTOList.add(carDTO);
		return projectDTO;
	}
	
	private ProjectsDTO copyProjectsEntityToDto(Projects projectEntity) {
		ProjectsDTO projectDTO=new ProjectsDTO();
		//projectDTO.setBuilder(this.setBuilderDTO(projectEntity.getBuilder()));
		projectDTO.setAmenitiesAndSpecifications(this.copyAmenitiesAndSpecificationEntityToDto(projectEntity.getAmenitiesAndSpecifications()));
		projectDTO.setPicture(projectEntity.getPicture().stream().map(this::copyPictureEntityToDto).collect(Collectors.toList()));
		BeanUtils.copyProperties(projectEntity, projectDTO);
		return projectDTO;
	}
	
	private PictureDTO copyPictureEntityToDto(Picture pictureEntity) {
		PictureDTO pitureDTO= new PictureDTO();
		BeanUtils.copyProperties(pictureEntity, pitureDTO);
		return pitureDTO;
	}
	
	private AddressDTO copyAddressEntityToDto(Address addressEntity) {
		AddressDTO addressDTO= new AddressDTO();
		BeanUtils.copyProperties(addressEntity, addressDTO);
		return addressDTO;
	}
	
	private Address copyAddressDTOToEntity(AddressDTO addressDTO) {
		Address addressEntity= new Address();
		BeanUtils.copyProperties(addressDTO, addressEntity);
		return addressEntity;
	}
	
	private BuilderDTO copyBuilderEntityToDto(Builder builderEntity) {
		BuilderDTO builderDTO= new BuilderDTO();
		
		builderDTO.setAddress(this.copyAddressEntityToDto(builderEntity.getAddress()));
		builderDTO.setProjects(builderEntity.getProjects().stream().map(this::copyProjectsEntityToDto).collect(Collectors.toList()));
		BeanUtils.copyProperties(builderEntity, builderDTO);
		return builderDTO;
	}
	
	
	private AmenitiesAndSpecificationsDTO copyAmenitiesAndSpecificationEntityToDto(AmenitiesAndSpecifications amenitiesAndSpecifications) {
		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO= new AmenitiesAndSpecificationsDTO();
		BeanUtils.copyProperties(amenitiesAndSpecifications, amenitiesAndSpecificationsDTO);
		return amenitiesAndSpecificationsDTO;
	}
	
	
	public static void copyProjectsBasicEntityToDTO(Projects projectEntity, ProjectsDTO projectDTO, Set<String> props) {
	    String[] excludedProperties = 
	            Arrays.stream(BeanUtils.getPropertyDescriptors(projectEntity.getClass()))
	                  .map(PropertyDescriptor::getName)
	                  .filter(name -> !props.contains(name))
	                  .toArray(String[]::new);

	    BeanUtils.copyProperties(projectEntity, projectDTO, excludedProperties);
	}
	
	public static void copyBuilderBasicEntityToDTO(Builder builderEntity, BuilderDTO builderDTO, Set<String> props) {
	    String[] excludedProperties = 
	            Arrays.stream(BeanUtils.getPropertyDescriptors(builderEntity.getClass()))
	                  .map(PropertyDescriptor::getName)
	                  .filter(name -> !props.contains(name))
	                  .toArray(String[]::new);

	    BeanUtils.copyProperties(builderEntity, builderDTO, excludedProperties);
	}
	
	
	public static void copyBuilderBasicDTOToEntity(BuilderDTO builderDTO, Builder builderEntity, Set<String> props) {
	    String[] excludedProperties = 
	            Arrays.stream(BeanUtils.getPropertyDescriptors(builderEntity.getClass()))
	                  .map(PropertyDescriptor::getName)
	                  .filter(name -> !props.contains(name))
	                  .toArray(String[]::new);

	    BeanUtils.copyProperties(builderDTO, builderEntity, excludedProperties);
	}
	
	public static void copyProjectsBasicDTOToEntity(ProjectsDTO projectDTO, Projects projectEntity, Set<String> props) {
	    String[] excludedProperties = 
	            Arrays.stream(BeanUtils.getPropertyDescriptors(projectEntity.getClass()))
	                  .map(PropertyDescriptor::getName)
	                  .filter(name -> !props.contains(name))
	                  .toArray(String[]::new);

	    BeanUtils.copyProperties(projectDTO, projectEntity, excludedProperties);
	}
	
		
	
	
	public Resource getFileSystem(String filename, HttpServletResponse response) {
		return getResource(filename, response, ResourceType.FILE_SYSTEM);
	}
 
	/**
	 * @param filename filename
	 * @param response Http response.
	 * @return file from classpath.
	 */
	public  Resource getClassPathFile(String filename, HttpServletResponse response) {
		return getResource(filename, response, ResourceType.CLASSPATH);
	}
 
	private  Resource getResource(String filename, HttpServletResponse response, ResourceType resourceType) {
		//response.setContentType("text/csv; charset=utf-8");
		//response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		//response.setHeader("filename", filename);
 
		Resource resource = null;
		final String FILE_DIRECTORY = "C:/Users/User/GitHub Repository/BuildersImage/Builder23/Project29/";
		switch (resourceType) {
			case FILE_SYSTEM:
				resource = new FileSystemResource(filename);
				System.out.println("ceateImageDirectoryForBuilder2" +resource.exists());
				break;
			case CLASSPATH:
				resource = new ClassPathResource("data/" + filename);
				break;
		}
 
		return resource;
	}
	


}
