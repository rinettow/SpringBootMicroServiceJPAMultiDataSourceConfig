package com.shop.organic.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.jpa.QueryHints;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.MaterialRequirementDTO;
import com.shop.organic.dto.MaterialRequirementItemsEstimateDTO;
import com.shop.organic.dto.MaterialSupplierAddressDTO;
import com.shop.organic.dto.MaterialSupplierDTO;
import com.shop.organic.dto.ProductCategoryDTO;
import com.shop.organic.dto.SupplierAvailableCategoriesDTO;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.Customer;
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.MaterialRequirementItems;
import com.shop.organic.entity.car.MaterialRequirementItemsEstimate;
import com.shop.organic.entity.car.MaterialSupplier;
import com.shop.organic.entity.car.MaterialSupplierAddress;
import com.shop.organic.entity.car.ProductCategory;
import com.shop.organic.entity.car.SupplierAvailableCategories;
import com.shop.organic.entity.car.SupplierOtp;
import com.shop.organic.util.CreateEntityManager;

@Service
@Transactional
//@ConfigurationProperties("application-dev")
public class MaterialSupplierService {

	private String port;

	@Autowired
	private CreateEntityManager em;

	// @Autowired
	private ExecutorService threadpoolToGtetAllStates;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;

	private enum ResourceType {
		FILE_SYSTEM, CLASSPATH
	}

	@PreDestroy
	public void shutdonw() {
		// needed to avoid resource leak
		threadpoolToGtetAllStates.shutdown();
	}

	
	public boolean VerifyMaterialSuppliersOTP(MaterialSupplierDTO materialSupplierDTO, String otp) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<SupplierOtp> materialSuppliersOtp = new ArrayList<SupplierOtp>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierOtp> criteria = builder.createQuery(SupplierOtp.class);
		Root<SupplierOtp> rootBuilder = criteria.from(SupplierOtp.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("suplierPhoneNumber"), materialSupplierDTO.getMaterialSupplierPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<SupplierOtp> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialSuppliersOtp = query.getResultList();
		
		int otpDB= materialSuppliersOtp.get(0).getSuplierOtpNumber();
		int otpCustomerEnered = Integer.parseInt(otp.replace("\"",""));
		
		if(otpDB == otpCustomerEnered) {
			return true;
		}else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}
	
	public boolean VerifyAlreadyRegisteredMaterialSuplier(MaterialSupplierDTO materialSupplierDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<MaterialSupplier> materialSupplierEntity = new ArrayList<MaterialSupplier>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialSupplier> criteria = builder.createQuery(MaterialSupplier.class);
		Root<MaterialSupplier> rootBuilder = criteria.from(MaterialSupplier.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialSupplierPhone"), materialSupplierDTO.getMaterialSupplierPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialSupplier> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialSupplierEntity = query.getResultList();
		
		if(!materialSupplierEntity.isEmpty()) {
			return true;
		}else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}
	
	
	public boolean VerifyIfMobileAlreadyRegisteredAsCustomer(MaterialSupplierDTO materialSupplierDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Customer> customerEntity = new ArrayList<Customer>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> rootBuilder = criteria.from(Customer.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phoneCustomer"), materialSupplierDTO.getMaterialSupplierPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Customer> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		customerEntity = query.getResultList();
		
		if(!customerEntity.isEmpty()) {
			return true;
		}else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}
	
	
	public boolean VerifyIfMobileAlreadyRegisteredAsBuilder(MaterialSupplierDTO materialSupplierDTO) {
		EntityManager entityManager = em.getEntityManager("builder");
		List<Builder> builderEntity = new ArrayList<Builder>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Builder> criteria = builder.createQuery(Builder.class);
		Root<Builder> rootBuilder = criteria.from(Builder.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("phone"), materialSupplierDTO.getMaterialSupplierPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<Builder> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		builderEntity = query.getResultList();
		
		if(!builderEntity.isEmpty()) {
			return true;
		}else {
			return false;
		}

		// responseBuilderDTO= this.setBuilderDTO(builderEntity);
	}
	
	
	
	public MaterialSupplierDTO registerMaterialSupplier(MaterialSupplierDTO materialSupplierDTO) {
		MaterialSupplierDTO responseMaterialSupplierDTO = new MaterialSupplierDTO();

		MaterialSupplier materialSupplierEntity = setMaterialSupplierEntity(materialSupplierDTO);
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(materialSupplierEntity)) {
			// persist object - add to entity manager
			entityManager.persist(materialSupplierEntity);
			// flush em - save to DB
			entityManager.flush();
		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		responseMaterialSupplierDTO = this.setMaterialSupplierDTO(materialSupplierEntity);
		entityManager.close();

		return responseMaterialSupplierDTO;
	}
	
	
	
	public Map<String, Object> sendOTPSupplierLogin(String Phone, String Password) {
		MaterialSupplierDTO LoginMaterialSupplierDTO = new MaterialSupplierDTO();
		Map<String, Object> response = new HashMap<String, Object>();
		List<MaterialSupplier> LoginMaterialSupplier = new ArrayList<MaterialSupplier>();
		String responseStatus= null;
		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT s FROM MaterialSupplier s WHERE s.materialSupplierPhone = :materialSupplierPhone", MaterialSupplier.class);
		q.setParameter("materialSupplierPhone", Phone);
		// q.setParameter("keyword", keyword); //etc
		LoginMaterialSupplier = q.getResultList();

		
		

		if (LoginMaterialSupplier.isEmpty() && LoginMaterialSupplier.size() == 0) {
			//throw new ResourceNotFoundException("Mobile Number: " + builderDTO.getPhone() + " not Registered...");
			responseStatus = "Supplier Mobile Not Registered";
		}

		if (!LoginMaterialSupplier.isEmpty()) {

			if(verifySupplierPassword(LoginMaterialSupplier.get(0), Password)) {
				responseStatus = "Success";
				LoginMaterialSupplierDTO = setMaterialSupplierDTO(LoginMaterialSupplier.get(0));
			}else {
				responseStatus = "Incorrect Password";
			}	
		}
		entityManager.close();
		response.put("responseStatus", responseStatus);
		response.put("loggedinSupplier", LoginMaterialSupplierDTO);
		// System.out.println("LoginBuilderDTO" +new Gson().toJson(LoginBuilderDTO));
		return response;
	}
	
	public MaterialSupplier getMaterialSupplierById(int supplierId) {
		
		List<MaterialSupplier> materialSupplier = new ArrayList<MaterialSupplier>();
		String responseStatus= null;
		EntityManager entityManager = em.getEntityManager("builder");

		Query q = entityManager.createQuery("SELECT s FROM MaterialSupplier s WHERE s.materialSupplierBuilderId = :materialSupplierBuilderId", MaterialSupplier.class);
		q.setParameter("materialSupplierBuilderId", supplierId);
		// q.setParameter("keyword", keyword); //etc
		materialSupplier = q.getResultList();
		
		return materialSupplier.get(0);
	}
	
	public boolean verifySupplierPassword(MaterialSupplier materialSupplier, String password) {
		if(materialSupplier.getMaterialSupplierPassword().equals(password)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
	
	public void ResetSupplierPassword(MaterialSupplierDTO materialSupplierDTO) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialSupplier> materialSupplierEntity = new ArrayList<MaterialSupplier>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialSupplier> criteria = builder.createQuery(MaterialSupplier.class);
		Root<MaterialSupplier> rootBuilder = criteria.from(MaterialSupplier.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(rootBuilder.get("materialSupplierPhone"), materialSupplierDTO.getMaterialSupplierPhone()));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialSupplier> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialSupplierEntity = query.getResultList();

		// commit transaction at all
		// entityManager.getTransaction().commit();
		
		MaterialSupplier materialSupplierToChangePasword = materialSupplierEntity.get(0);
		materialSupplierToChangePasword.setMaterialSupplierPassword(materialSupplierDTO.getMaterialSupplierPassword());;

		//entityManager.getTransaction().begin();
		
		if (!entityManager.contains(materialSupplierToChangePasword)) {
			// persist object - add to entity manager
			entityManager.persist(materialSupplierToChangePasword);
			// flush em - save to DB
			entityManager.flush();
		} else {
			entityManager.merge(materialSupplierToChangePasword);
			entityManager.flush();
		}
		
		
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();

	}
	
	public void saveSupplierOTP(MaterialSupplierDTO materialSupplierDTO) {
		BuildersEstimateDTO responseBuildersEstimateDTO = new BuildersEstimateDTO();
		
		SupplierOtp supplierOtp =new SupplierOtp();
		
		int otpDigits =4;
		char[] otpGeneratedForSupplier = GenerateSuppliersOTP(otpDigits);
        System.out.println(otpGeneratedForSupplier); 
		//String[] otpGeneratedForBuilderStringArr = new String[otpGeneratedForBuilder.length];
		String otpGeneratedForSupplierConcated = null;
		for (int i = 0; i < otpGeneratedForSupplier.length; i++) {
	        //ints[i] = Character.getNumericValue(otpGeneratedForBuilder[i]);
			System.out.println(String.valueOf(otpGeneratedForSupplier[i])); 
			otpGeneratedForSupplierConcated = otpGeneratedForSupplierConcated + String.valueOf(otpGeneratedForSupplier[i]);
			//otpGeneratedForBuilderConcated.concat(String.valueOf(otpGeneratedForBuilder[i]));
			//otpGeneratedForBuilderStringArr[i] = String.valueOf(otpGeneratedForBuilder[i]);
	    }
		
		System.out.println(otpGeneratedForSupplierConcated.substring(4)); 
		
		
		supplierOtp.setSuplierPhoneNumber(materialSupplierDTO.getMaterialSupplierPhone());
		supplierOtp.setSuplierOtpNumber(Integer.parseInt(otpGeneratedForSupplierConcated.substring(4)));
		
		
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		if (!entityManager.contains(supplierOtp)) {
			//BuilderOtp entityAvailableOrNot = entityManager.find(BuilderOtp.class, builderOtp.getBuilderPhoneNumber());
			//EntityManager entityManager = em.getEntityManager("builder");
			List<SupplierOtp> supplierOtps = new ArrayList<SupplierOtp>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<SupplierOtp> criteria = builder.createQuery(SupplierOtp.class);
			Root<SupplierOtp> rootBuilder = criteria.from(SupplierOtp.class);
			criteria.select(rootBuilder);

			List<Predicate> restrictions = new ArrayList<Predicate>();
			restrictions.add(builder.equal(rootBuilder.get("suplierPhoneNumber"), supplierOtp.getSuplierPhoneNumber()));

			criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
			TypedQuery<SupplierOtp> query = entityManager.createQuery(criteria);
			query.setHint(QueryHints.HINT_CACHEABLE, true);
			query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
			supplierOtps = query.getResultList();
			if (supplierOtps.isEmpty()) {
				//if (1 == 1) {
				// persist object - add to entity manager
				entityManager.persist(supplierOtp);
				// flush em - save to DB
				entityManager.flush();
			} else {
				supplierOtp.setSuplierOtpId(supplierOtps.get(0).getSuplierOtpId());
				entityManager.merge(supplierOtp);
			}

		}
		// commit transaction at all
		entityManager.getTransaction().commit();

		entityManager.close();
		

	}
	
	public MaterialRequirementItemsEstimate saveSupplierEstimate(MaterialRequirementItemsEstimate materialRequirementItemsEstimate) {

		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		// if (!entityManager.contains(builderEntity)) {
		// persist object - add to entity manager
		entityManager.persist(materialRequirementItemsEstimate);
		// flush em - save to DB
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();
	    return materialRequirementItemsEstimate;
	}
	
	
	public List<MaterialRequirement> GetAllOpenMaterialReqirements(MaterialSupplierDTO materialSupplierDTO, 
			String state, String district) {
		boolean isOpenMaterialReuirementAvailable = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirement> criteria = builder.createQuery(MaterialRequirement.class);
		Root<MaterialRequirement> rootBuilder = criteria.from(MaterialRequirement.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		List<Long> categoryIds = new ArrayList<>();
		for (SupplierAvailableCategoriesDTO supplierAvailableCategories : materialSupplierDTO.getMaterialSupplierAvailableCategories()) {
			if(Integer.valueOf(supplierAvailableCategories.getProductCategoryId()) == 6) {
				categoryIds.add(1L);
				categoryIds.add(2L);
				categoryIds.add(3L);
				categoryIds.add(4L);
				categoryIds.add(5L);
				categoryIds.add(6L);
				
			}else {
				categoryIds.add(Long.valueOf(Integer.valueOf(supplierAvailableCategories.getProductCategoryId())));
				
			}
		}
		restrictions.add(rootBuilder.get("productCategoryId").in(categoryIds));

		
	    
		restrictions.add(builder.equal(rootBuilder.get("state"), state));
		restrictions.add(builder.equal(rootBuilder.get("district"), district));
		restrictions.add(builder.equal(rootBuilder.get("requirementStatus"), "OPEN"));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirement> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirement = query.getResultList();

		
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		if(!materialRequirement.isEmpty()) {
			isOpenMaterialReuirementAvailable = true;
			return materialRequirement;
		}

		return null;
	}
	
	public MaterialRequirement GetMaterialReqirementById(int materialRequirementId) {
		boolean isOpenMaterialReuirementAvailable = false;
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
		
		if(!materialRequirement.isEmpty()) {
			isOpenMaterialReuirementAvailable = true;
			return materialRequirement.get(0);
		}

		return null;
	}
	
	
	public boolean verifyIfEstimateAlreadySubmittedForMaterialRequirementByBuilderCustomer(MaterialRequirement materialRequirement, int SuplierId) {
		boolean isEstimateAlreadySubmittedForMaterialRequirementByBuilderCustomer = false;
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		List<MaterialRequirementItemsEstimate> materialRequirementEstimate = new ArrayList<MaterialRequirementItemsEstimate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MaterialRequirementItemsEstimate> criteria = builder.createQuery(MaterialRequirementItemsEstimate.class);
		Root<MaterialRequirementItemsEstimate> rootBuilder = criteria.from(MaterialRequirementItemsEstimate.class);
		criteria.select(rootBuilder);

		List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.equal(rootBuilder.get("materialRequirementId"), materialRequirement.getMaterialRequirementId()));
				restrictions.add(builder.equal(rootBuilder.get("materialSupplierId"), SuplierId));

		criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
		TypedQuery<MaterialRequirementItemsEstimate> query = entityManager.createQuery(criteria);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "blCarIdQuery");
		materialRequirementEstimate = query.getResultList();

		
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		if(!materialRequirementEstimate.isEmpty()) {
			isEstimateAlreadySubmittedForMaterialRequirementByBuilderCustomer = true;
			return true;
		}

		return false;
	}
	
	
	public char[] GenerateSuppliersOTP(int len) 
    { 
        System.out.println("Generating OTP using random() : "); 
        System.out.print("You OTP is : "); 
  
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] otp = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            otp[i] = 
             numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        return otp; 
    } 
	
	public void registerSupplierAvailableCategories(SupplierAvailableCategories supplierAvailableCategories) {
		EntityManager entityManager = em.getEntityManager("builder");

		entityManager.getTransaction().begin();
		// if (!entityManager.contains(builderEntity)) {
		// persist object - add to entity manager
		entityManager.persist(supplierAvailableCategories);
		// flush em - save to DB
		entityManager.flush();
		// }
		// commit transaction at all
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public MaterialSupplier setMaterialSupplierEntity(MaterialSupplierDTO materialSupplierDTO) {
		MaterialSupplier materialSupplierEntity = new MaterialSupplier();

		materialSupplierEntity.setMaterialSupplierAddress(this.copySupplierAddressDTOToEntity(materialSupplierDTO.getMaterialSupplierAddress()));
		
		final Set<String> prop = new HashSet<>(
				Arrays.asList("materialSupplierBuilderName", "materialSupplierCompany", "materialSupplierPhone", 
						"materialSupplierUserName", "materialSupplierPassword"));
		this.copyMaterialSupplierBasicDTOToEntity(materialSupplierDTO, materialSupplierEntity, prop);
		// builderEntity.setBuildersAvailableAmenities(builderDTO.getBuildersAvailableAmenities().stream().map(buildersAvailableAmenitiesDTO
		// ->
		// this.copyBuildersBasicAvailableAmenitiesDTOToEntity(buildersAvailableAmenitiesDTO,
		// new BuildersAvailableAmenities())).collect(Collectors.toList()));

		return materialSupplierEntity;
	}
	
	public static void copyMaterialSupplierBasicDTOToEntity(MaterialSupplierDTO materialSupplierDTO, MaterialSupplier materialSupplierEntity, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(materialSupplierEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(materialSupplierDTO, materialSupplierEntity, excludedProperties);
	}
	
	private MaterialSupplierAddress copySupplierAddressDTOToEntity(MaterialSupplierAddressDTO materialSupplierAddressDTO) {
		MaterialSupplierAddress materialSupplierAddressEntity = new MaterialSupplierAddress();
		BeanUtils.copyProperties(materialSupplierAddressDTO, materialSupplierAddressEntity);
		return materialSupplierAddressEntity;
	}
	
	
	
	public MaterialSupplierDTO setMaterialSupplierDTO(MaterialSupplier materialSupplierEntity) {
		MaterialSupplierDTO materialSupplierDTO = new MaterialSupplierDTO();
		materialSupplierDTO.setMaterialSupplierAddress(this.copySupplierAddressEntityToDto(materialSupplierEntity.getMaterialSupplierAddress()));
		
		if (materialSupplierEntity.getMaterialSupplierAvailableCategories() != null
				&& !materialSupplierEntity.getMaterialSupplierAvailableCategories().isEmpty()) {
			materialSupplierDTO.setMaterialSupplierAvailableCategories(materialSupplierEntity.getMaterialSupplierAvailableCategories().stream()
					.map(supplierAvailableCategories -> this.copySupplierBasicAvailableCategoriesEntityToDTO(
							supplierAvailableCategories, new SupplierAvailableCategoriesDTO()))
					.collect(Collectors.toList()));
		}

		if (materialSupplierEntity.getMaterialRequirementItemsEstimate() != null
				&& !materialSupplierEntity.getMaterialRequirementItemsEstimate().isEmpty()) {
			List<String> materialRequirementIds = new ArrayList<String>();
			materialSupplierEntity.getMaterialRequirementItemsEstimate()
			.stream().filter(estimate -> materialRequirementIds.add(String.valueOf(estimate.getMaterialRequirementId()) )).collect(Collectors.toList());
			
			List<String> distinctMaterialRequirementIds = materialRequirementIds.stream()
	                .distinct()
	                .collect(Collectors.toList());
			
			List<MaterialRequirement> SuppliersSubmitedApprovedDecliedRequirements = distinctMaterialRequirementIds.stream().map(reqId->this.GetMaterialReqirementById(Integer.valueOf(reqId))).collect(Collectors.toList());
			
			List<MaterialRequirementDTO> SuppliersSubmitedApprovedDecliedRequirementsDTO = SuppliersSubmitedApprovedDecliedRequirements.stream()
			.map(matReq-> productService.setMaterialRequirementDTO(matReq)).collect(Collectors.toList());
			
			List<MaterialRequirementDTO> SuppliersSubmitedApprovedDecliedRequirementsDTOWithEstimate = SuppliersSubmitedApprovedDecliedRequirementsDTO.stream()
			.map(matReqDTO-> this.setSupplierEstimateDTO(matReqDTO, materialSupplierEntity)).collect(Collectors.toList());
			
			//materialSupplierDTO.setSuppliersSubmitedApprovedDecliedRequirementsDTOWithEstimate(SuppliersSubmitedApprovedDecliedRequirementsDTOWithEstimate);
			materialSupplierDTO.setMaterialRequirement(SuppliersSubmitedApprovedDecliedRequirementsDTOWithEstimate);
		}
		
		/*final Set<String> prop = new HashSet<>(Arrays.asList("materialSupplierBuilderId", "materialSupplierBuilderName", "materialSupplierCompany", "materialSupplierPhone", 
				"materialSupplierUserName", "materialSupplierPassword"));
		this.copyMaterialSupplierBasicEntityToDTO(materialSupplierEntity, materialSupplierDTO, prop);*/
		materialSupplierDTO.setMaterialSupplierBuilderId(materialSupplierEntity.getMaterialSupplierBuilderId());
		materialSupplierDTO.setMaterialSupplierBuilderName(materialSupplierEntity.getMaterialSupplierBuilderName());
		materialSupplierDTO.setMaterialSupplierCompany(materialSupplierEntity.getMaterialSupplierCompany());
		materialSupplierDTO.setMaterialSupplierPhone(materialSupplierEntity.getMaterialSupplierPhone());
		materialSupplierDTO.setMaterialSupplierUserName(materialSupplierEntity.getMaterialSupplierUserName());
		materialSupplierDTO.setMaterialSupplierPassword(materialSupplierEntity.getMaterialSupplierPassword());
		// carDTOList.add(carDTO);
		return materialSupplierDTO;
	}
	
	public MaterialSupplierDTO setMaterialSupplierDTOWithoutEstimate(MaterialSupplier materialSupplierEntity) {
		MaterialSupplierDTO materialSupplierDTO = new MaterialSupplierDTO();
		materialSupplierDTO.setMaterialSupplierAddress(this.copySupplierAddressEntityToDto(materialSupplierEntity.getMaterialSupplierAddress()));
		
		if (materialSupplierEntity.getMaterialSupplierAvailableCategories() != null
				&& !materialSupplierEntity.getMaterialSupplierAvailableCategories().isEmpty()) {
			materialSupplierDTO.setMaterialSupplierAvailableCategories(materialSupplierEntity.getMaterialSupplierAvailableCategories().stream()
					.map(supplierAvailableCategories -> this.copySupplierBasicAvailableCategoriesEntityToDTO(
							supplierAvailableCategories, new SupplierAvailableCategoriesDTO()))
					.collect(Collectors.toList()));
		}

		
		
		/*final Set<String> prop = new HashSet<>(Arrays.asList("materialSupplierBuilderId", "materialSupplierBuilderName", "materialSupplierCompany", "materialSupplierPhone", 
				"materialSupplierUserName", "materialSupplierPassword"));
		this.copyMaterialSupplierBasicEntityToDTO(materialSupplierEntity, materialSupplierDTO, prop);*/
		materialSupplierDTO.setMaterialSupplierBuilderId(materialSupplierEntity.getMaterialSupplierBuilderId());
		materialSupplierDTO.setMaterialSupplierBuilderName(materialSupplierEntity.getMaterialSupplierBuilderName());
		materialSupplierDTO.setMaterialSupplierCompany(materialSupplierEntity.getMaterialSupplierCompany());
		materialSupplierDTO.setMaterialSupplierPhone(materialSupplierEntity.getMaterialSupplierPhone());
		materialSupplierDTO.setMaterialSupplierUserName(materialSupplierEntity.getMaterialSupplierUserName());
		materialSupplierDTO.setMaterialSupplierPassword(materialSupplierEntity.getMaterialSupplierPassword());
		// carDTOList.add(carDTO);
		return materialSupplierDTO;
	}
	
	
	public MaterialRequirementDTO setSupplierEstimateDTO(MaterialRequirementDTO materialRequirementDTO, MaterialSupplier materialSupplierEntity){
		
		List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate = materialSupplierEntity.getMaterialRequirementItemsEstimate()
		.stream().filter(estimate-> estimate.getMaterialRequirementId() == materialRequirementDTO.getMaterialRequirementId())
		.collect(Collectors.toList());
		
		List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimateDTO = materialRequirementItemsEstimate.stream()
				.map(estimateEntity-> copyMaterialRequirementItemsEstimateEntityToDTO(estimateEntity, new MaterialRequirementItemsEstimateDTO())).collect(Collectors.toList());
		// carDTOList.add(carDTO);
		materialRequirementDTO.setMaterialRequirementItemsEstimate(materialRequirementItemsEstimateDTO);
		return materialRequirementDTO;
	}
	
	public MaterialRequirementItemsEstimateDTO copyMaterialRequirementItemsEstimateEntityToDTO(
			MaterialRequirementItemsEstimate materialRequirementItemsEstimate,
			MaterialRequirementItemsEstimateDTO materialRequirementItemsEstimateDTO) throws BeansException {
		materialRequirementItemsEstimateDTO.setMaterialRequirementItemsForMaterialRequirementItemsEstimate(productService.setMaterialRequirementItemsDTO(productService.getMaterialRequirementItemByItemId(materialRequirementItemsEstimate.getMaterialRequirementItemId()).get(0)));
		
		final Set<String> prop = new HashSet<>(Arrays.asList("materialRequirementItemEstmtimateId", "materialRequirementItemId",
				"materialRequirementId", "materialSupplierId", "totalPrice", "customerBuilderAceptedDeclined"));

		/*
		 * BuildersAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
		 * copyAmenityAndSpecificationsEntityToDTO(
		 * getAmenitiesAndSpecificationsByAmenityid(
		 * BuildersAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));
		 */
		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(materialRequirementItemsEstimateDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(materialRequirementItemsEstimate, materialRequirementItemsEstimateDTO, excludedProperties);
		return materialRequirementItemsEstimateDTO;
	}
	
	
	
	public SupplierAvailableCategoriesDTO copySupplierBasicAvailableCategoriesEntityToDTO(
			SupplierAvailableCategories supplierAvailableCategoriesEntity,
			SupplierAvailableCategoriesDTO SupplierAvailableCategoriesDTO) throws BeansException {
		SupplierAvailableCategoriesDTO.setProductCategoryForSupplierAvailableCategories(copyCategoriesEntityToDTO(supplierAvailableCategoriesEntity.getProductCategoryForSupplierAvailableCategories()));
		final Set<String> prop = new HashSet<>(Arrays.asList("materialSupplierId", "productCategoryId"));

		/*
		 * BuildersAvailableAmenitiesDTO.setAmenitiesAndSpecifications(
		 * copyAmenityAndSpecificationsEntityToDTO(
		 * getAmenitiesAndSpecificationsByAmenityid(
		 * BuildersAvailableAmenitiesEntity.getAmenitiesAndSpecificationsId())));
		 */
		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(SupplierAvailableCategoriesDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(supplierAvailableCategoriesEntity, SupplierAvailableCategoriesDTO, excludedProperties);
		return SupplierAvailableCategoriesDTO;
	}
	
	public static SupplierAvailableCategories copySupplierBasicAvailableCategoriesDTOToEntity(
			SupplierAvailableCategoriesDTO SupplierAvailableCategoriesDTO,
			SupplierAvailableCategories supplierAvailableCategoriesEntity) throws BeansException {
		final Set<String> prop = new HashSet<>(Arrays.asList("materialSupplierId", "productCategoryId"));

		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(supplierAvailableCategoriesEntity.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(SupplierAvailableCategoriesDTO, supplierAvailableCategoriesEntity, excludedProperties);
		return supplierAvailableCategoriesEntity;
	}
	
	public static MaterialRequirementItemsEstimate copySupplierBasicAvailableItemEstimatesDTOToEntity(
			MaterialRequirementItemsEstimateDTO materialRequirementItemsEstimateDTO,
			MaterialRequirementItemsEstimate materialRequirementItemsEstimate) throws BeansException {
		final Set<String> prop = new HashSet<>(Arrays.asList("materialRequirementItemId", "materialRequirementId", "materialSupplierId", "totalPrice", "customerBuilderAceptedDeclined", "deliveryCharge"));

		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(materialRequirementItemsEstimate.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(materialRequirementItemsEstimateDTO, materialRequirementItemsEstimate, excludedProperties);
		return materialRequirementItemsEstimate;
	}
	
	private static ProductCategoryDTO copyCategoriesEntityToDTO(
			ProductCategory productCategory) {
		final Set<String> prop = new HashSet<>(
				Arrays.asList("productCategoryId", "productCategoryName"));
		ProductCategoryDTO ProductCategoryDTO = new ProductCategoryDTO();
		String[] excludedProperties = null;
		try {
			excludedProperties = Arrays
					.stream(BeanUtils.getPropertyDescriptors(ProductCategoryDTO.getClass()))
					.map(PropertyDescriptor::getName).filter(name -> !prop.contains(name)).toArray(String[]::new);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BeanUtils.copyProperties(productCategory, ProductCategoryDTO, excludedProperties);
		return ProductCategoryDTO;
	}
	
	public static void copyMaterialSupplierBasicEntityToDTO(MaterialSupplier materialSupplierEntity, MaterialSupplierDTO materialSupplierDTO, Set<String> props) {
		String[] excludedProperties = Arrays.stream(BeanUtils.getPropertyDescriptors(materialSupplierEntity.getClass()))
				.map(PropertyDescriptor::getName).filter(name -> !props.contains(name)).toArray(String[]::new);

		BeanUtils.copyProperties(materialSupplierDTO, materialSupplierEntity, excludedProperties);
	}
	
	private MaterialSupplierAddressDTO copySupplierAddressEntityToDto(MaterialSupplierAddress materialSupplierAddressEntity) {
		MaterialSupplierAddressDTO materialSupplierAddressDTO = new MaterialSupplierAddressDTO();
		BeanUtils.copyProperties(materialSupplierAddressEntity, materialSupplierAddressDTO);
		return materialSupplierAddressDTO;
	}



}
