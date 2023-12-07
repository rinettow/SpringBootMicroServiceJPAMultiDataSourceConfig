package com.shop.organic.service;

import java.beans.PropertyDescriptor;
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
import org.hibernate.jpa.QueryHints;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.shop.organic.dto.AddressDTO;
import com.shop.organic.dto.BootSpaceDTO;
import com.shop.organic.dto.CarDTO;
import com.shop.organic.dto.CategoryDTO;
import com.shop.organic.dto.EntertainmentOptionDTO;
import com.shop.organic.dto.MileageDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.priceDTO;
import com.shop.organic.dto.productDTO;
import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.BootSpace;
import com.shop.organic.entity.car.Car;
import com.shop.organic.entity.car.EntertainmentOption;
import com.shop.organic.entity.car.Mileage;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.category.category;
import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.repo.CarRepository;
import com.shop.organic.repo.categoryRepository;
import com.shop.organic.util.CreateEntityManager;

@Service
@Transactional
public class CarService {
	
	//@Autowired
	//private CarRepository carRepository;
	
	@Autowired
	private CreateEntityManager em;
    
	private List<Car> carEntityList;
	private List<CarDTO> carDTOList=new ArrayList<CarDTO>();
	//private List<CategoryDTO> catgDTOList;
	
	public List<CarDTO> findCarList() {
		//return categoryRepository.findAll().stream().map(this::copyCategoryEntityToDto).collect(Collectors.toList());
		//carEntityList=carRepository.findAll();
		
		EntityManager entityManager = em.getEntityManager("car");
		Query q = entityManager.createQuery("SELECT c FROM Car c", Car.class);
	    //q.setParameter("keyword", keyword); //etc
		carEntityList= q.getResultList();
		System.out.println("carEntityList.size()::::" +carEntityList.size());
		
		carDTOList.clear();
		int i=0;
		
		return carEntityList.stream().map(this::setCarDTO).collect(Collectors.toList());
	}
	
	
	public List<CarDTO> getCarById(String carId) {
		EntityManager entityManager = em.getEntityManager("car");
		
		carDTOList.clear();
		int i=0;
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		  CriteriaQuery<Car> criteria = builder.createQuery(Car.class);
		  Root<Car> rootCar = criteria.from(Car.class);
		  criteria.select(rootCar);
		  
		  List<Predicate> restrictions = new ArrayList<Predicate>();
		  restrictions.add(builder.equal(rootCar.get("carId"), carId));
		  
		  criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  TypedQuery<Car> query = entityManager.createQuery(criteria);
		  query.setHint(QueryHints.HINT_CACHEABLE, true);
		  query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		  carEntityList = query.getResultList();
		  
		  if (carEntityList.isEmpty()) {
              throw new ResourceNotFoundException("Car: " +carId+ " not Found..." );
          }
		  System.out.println("carEntityList.size()::::" +carEntityList.size());
		return carEntityList.stream().map(this::setCarDTO).collect(Collectors.toList());
	}
	
	
	
	 
	
	public CarDTO setCarDTO(Car carEntity){
		CarDTO carDTO= new CarDTO();
		
		carDTO.setAddress(this.copyAddressEntityToDto(carEntity.getAddress()));
		carDTO.setMileage(this.copyMileageEntityToDto(carEntity.getMileage()));
		carDTO.setBootSpace(this.copyBootSpaceToDto(carEntity.getBootSpace()));
		carDTO.setEntertainmentOption(this.copyEntertainmentOptionToDto(carEntity.getEntertainmentOption()));
		
		carDTO.setPicturesDTO(carEntity.getPictures().stream().map(this::copyPictureEntityToDto).collect(Collectors.toList()));
		
		final Set<String> prop = new HashSet<>(Arrays.asList("carId", "carName", "color", "costPerHour", "costPerKilometer", "cruseControl", "fuelType"
				, "luxary", "make", "manualOrAuto", "manufacturingCompany", "modelYear", "navigationInBuild", "seatCount", "steeringControl", "sunRoof"
				, "totalMileage", "variant"));
		this.copyCarBasicEntityToDTO(carEntity, carDTO, prop);
		
		//carDTOList.add(carDTO);
		return carDTO;
	}
	
	private PictureDTO copyPictureEntityToDto(Picture pictureEntity) {
		PictureDTO picDTO=new PictureDTO();
		BeanUtils.copyProperties(pictureEntity, picDTO);
		return picDTO;
	}
	
	private AddressDTO copyAddressEntityToDto(Address addressEntity) {
		AddressDTO addressDTO= new AddressDTO();
		BeanUtils.copyProperties(addressEntity, addressDTO);
		return addressDTO;
	}
	
	private BootSpaceDTO copyBootSpaceToDto(BootSpace bootSpaceEntity) {
		BootSpaceDTO bootSpaceDTO= new BootSpaceDTO();
		BeanUtils.copyProperties(bootSpaceEntity, bootSpaceDTO);
		return bootSpaceDTO;
	}
	
	private MileageDTO copyMileageEntityToDto(Mileage mileageEntity) {
		MileageDTO mileageDTO= new MileageDTO();
		BeanUtils.copyProperties(mileageEntity, mileageDTO);
		return mileageDTO;
	}
	
	private EntertainmentOptionDTO copyEntertainmentOptionToDto(EntertainmentOption entertainmentOptionEntity) {
		EntertainmentOptionDTO entertainmentOptionDTO= new EntertainmentOptionDTO();
		BeanUtils.copyProperties(entertainmentOptionEntity, entertainmentOptionDTO);
		return entertainmentOptionDTO;
	}
	
	public static void copyCarBasicEntityToDTO(Car carEntity, CarDTO carDTO, Set<String> props) {
	    String[] excludedProperties = 
	            Arrays.stream(BeanUtils.getPropertyDescriptors(carEntity.getClass()))
	                  .map(PropertyDescriptor::getName)
	                  .filter(name -> !props.contains(name))
	                  .toArray(String[]::new);

	    BeanUtils.copyProperties(carEntity, carDTO, excludedProperties);
	}
	
}
