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
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.DistrictDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.Customer;
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
public class CustomerService {

	// @Autowired
	// private CarRepository carRepository;

	// @Value("${server.port}")
	private String port;

	@Autowired
	private CreateEntityManager em;

	public Customer registerCustomer(CustomerDTO customerDTO) {
		BuilderDTO responseBuilderDTO = new BuilderDTO();

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

		System.out.println("builderEntity.getBuilderId()::::" + customerEntity.getCustomerId());
		System.out
				.println("builderEntity.getAddress().getAddressId())::::" + customerEntity.getPhoneCustomer());

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);

		return customerEntity;
	}
	
	public Customer setCustomerEntity(CustomerDTO customerDTO) {
		Customer customerEntity = new Customer();
		
		final Set<String> prop = new HashSet<>(
				Arrays.asList("customerName", "phoneCustomer"));
		this.copyCustomerBasicDTOToEntity(customerDTO, customerEntity, prop);
		// builderEntity.setBuildersAvailableAmenities(builderDTO.getBuildersAvailableAmenities().stream().map(buildersAvailableAmenitiesDTO
		// ->
		// this.copyBuildersBasicAvailableAmenitiesDTOToEntity(buildersAvailableAmenitiesDTO,
		// new BuildersAvailableAmenities())).collect(Collectors.toList()));

		return customerEntity;
	}
	
	public static void copyCustomerBasicDTOToEntity(CustomerDTO customerDTO, Customer custmerEntity, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(custmerEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(customerDTO, custmerEntity, excludedProperties);
	}
	
	public CustomerDTO setCustomerDTO(Customer customerEntity) {
		CustomerDTO customerDTO = new CustomerDTO();
		
		final Set<String> prop = new HashSet<>(Arrays.asList("customerId", "customerName", "phoneCustomer"));
		this.copyCustomerBasicEntityToDTO(customerEntity, customerDTO, prop);
		// carDTOList.add(carDTO);
		return customerDTO;
	}
	
	public static void copyCustomerBasicEntityToDTO(Customer customerEntity, CustomerDTO customerDTO, Set<String> props) {
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
			throw new ResourceNotFoundException("Mobile Number: " + customerDTO.getPhoneCustomer() + " not Registered...");
		}

		if (!LoginCustomer.isEmpty()) {

			LoginCustomerDTO = setCustomerDTO(LoginCustomer.get(0));
		}
		// System.out.println("LoginBuilderDTO" +new Gson().toJson(LoginBuilderDTO));
		return LoginCustomerDTO;
	}
	
	

}
