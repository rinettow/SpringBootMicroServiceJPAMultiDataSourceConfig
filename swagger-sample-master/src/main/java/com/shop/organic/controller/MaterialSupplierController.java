package com.shop.organic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shop.organic.dto.AmenitiesAndSpecificationsDTO;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.BuildersAvailableAmenitiesDTO;
import com.shop.organic.dto.BuildersEstimateDTO;
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.MaterialRequirementDTO;
import com.shop.organic.dto.MaterialRequirementItemsDTO;
import com.shop.organic.dto.MaterialRequirementItemsEstimateDTO;
import com.shop.organic.dto.MaterialSupplierDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.dto.SupplierAvailableBrandsDTO;
import com.shop.organic.dto.SupplierAvailableCategoriesDTO;
import com.shop.organic.dto.SuppliersEstimates;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuilderRedRequirements;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.MaterialRequirementItemsEstimate;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.entity.car.SupplierAvailableBrands;
import com.shop.organic.entity.car.SupplierAvailableCategories;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.BuilderService;
import com.shop.organic.service.MaterialSupplierService;
import com.shop.organic.service.ProductService;
import com.shop.organic.service.ProductService2;

import org.springframework.scheduling.annotation.Async;

@CrossOrigin(origins = "http://localhost")
@Validated
@RestController
@RequestMapping("/MaterialSupplier")
public class MaterialSupplierController {

	@Autowired
	private MaterialSupplierService materialSupplierService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private HttpServletRequest request;

	List<BuilderDTO> buildersList = null;
	
	
	@PostMapping(value = "/RegisterSupplier")
	//public ResponseEntity<Object> registerBuilder(@RequestBody BuilderDTO builderDTO) {
	public ResponseEntity<Object> registerSupplier(@RequestParam("otp") String otp, @RequestParam("supplierDTO") String supplierDTOString) throws JsonMappingException, JsonProcessingException {
		System.out.println("supplierDTOString" + new Gson().toJson(supplierDTOString));
		MaterialSupplierDTO registeredSupplier = new MaterialSupplierDTO();
		
		ObjectMapper objectMapper = new ObjectMapper();

		MaterialSupplierDTO materialSupplierDTO = new MaterialSupplierDTO();
		materialSupplierDTO = objectMapper.readValue(supplierDTOString, MaterialSupplierDTO.class);
		
		if(materialSupplierService.VerifyMaterialSuppliersOTP(materialSupplierDTO, otp)) {
			if(!materialSupplierService.VerifyAlreadyRegisteredMaterialSuplier(materialSupplierDTO)) {
				if(!materialSupplierService.VerifyIfMobileAlreadyRegisteredAsCustomer(materialSupplierDTO) 
						&& !materialSupplierService.VerifyIfMobileAlreadyRegisteredAsBuilder(materialSupplierDTO)) {
					registeredSupplier = materialSupplierService.registerMaterialSupplier(materialSupplierDTO);
					int supplierid = registeredSupplier.getMaterialSupplierBuilderId();
					List<SupplierAvailableCategoriesDTO> supplierAvailableCategoriesDTOWithSupplierId = materialSupplierDTO
							.getMaterialSupplierAvailableCategories().stream()
							.peek(supplierAvailableCatgDTO -> supplierAvailableCatgDTO.setMaterialSupplierId(supplierid))
							.collect(Collectors.toList());
					
					List<SupplierAvailableBrandsDTO> supplierAvailableBrandDTOWithSupplierId = materialSupplierDTO
							.getMaterialSupplierAvailableBrands()
							.stream()
							.peek(supplierAvailableBrandDTO -> supplierAvailableBrandDTO.setMaterialSupplierId(supplierid))
							.collect(Collectors.toList());
				
					
					List<SupplierAvailableCategories> supplierAvailableCategories = supplierAvailableCategoriesDTOWithSupplierId
							.stream()
							.map(supplierAvailableCatgDTO -> materialSupplierService.copySupplierBasicAvailableCategoriesDTOToEntity(
									supplierAvailableCatgDTO, new SupplierAvailableCategories()))
							.collect(Collectors.toList());
					
					List<SupplierAvailableBrands> supplierAvailableBrands = supplierAvailableBrandDTOWithSupplierId
							.stream()
							.map(supplierAvailableBrandDTO -> materialSupplierService.copySupplierBasicAvailableBrandsDTOToEntity(
									supplierAvailableBrandDTO, new SupplierAvailableBrands()))
							.collect(Collectors.toList());
					
					
					for (SupplierAvailableCategories supplierAvailableCategoriesToRegister : supplierAvailableCategories) {
						materialSupplierService.registerSupplierAvailableCategories(supplierAvailableCategoriesToRegister);
					}
					
					
					for (SupplierAvailableBrands brandForSupplier : supplierAvailableBrands) {
						materialSupplierService.registerSupplierAvailableBrands(brandForSupplier);
					}

					Object uriVariables = null;
					//builderService.ceateImageDirectoryForBuilder(registeredBuilder);
					// throw new RuntimeException("Not Available");
					// carList = carService.findCarList();
					// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
					// return generateResponse("List of Cars!", HttpStatus.OK, carList);
					return generateResponse("Supplier Registered Successful!", HttpStatus.OK, registeredSupplier);
				}else {
					return generateResponse("Mobile Already registered as Customer or Builder!", HttpStatus.CONFLICT, null);
				}
				
				
			}else {
				return generateResponse("Already registered Supplier!", HttpStatus.ALREADY_REPORTED, null);
			}
		}else {
			return generateResponse("Incorrect OTP!", HttpStatus.NOT_FOUND, null);
		}
		//return null;
		
	}
	
	@PostMapping(value = "/sendOTPSupplierLogin")
	//public ResponseEntity<Object> sendOTPSupplierLogin(@RequestBody MaterialSupplierDTO materialSupplierDTO) {
	public ResponseEntity<Object> sendOTPSupplierLogin(@RequestParam("Phone") String Phone, @RequestParam("Password") String Password) {
		MaterialSupplierDTO loginSupplier = new MaterialSupplierDTO();
		Map<String, Object> response = null;
		//System.out.println("materialSupplierDTO:::::Test" + new Gson().toJson(materialSupplierDTO));
		String PasswordQuotesRemoved =Password.replace("\"","");
		String PhoneQuotesRemoved =Phone.replace("\"","");
		response = materialSupplierService.sendOTPSupplierLogin(PhoneQuotesRemoved, PasswordQuotesRemoved);
		if(response.get("responseStatus").equals("Supplier Mobile Not Registered")) {
			return generateResponse("Supplier Mobile Not Registered", HttpStatus.NOT_FOUND, null);
		}else if(response.get("responseStatus").equals("Incorrect Password")){
			return generateResponse("Incorrect Password", HttpStatus.UNAUTHORIZED, null);
		}else {
			loginSupplier = (MaterialSupplierDTO) response.get("loggedinSupplier");
			return generateResponse("Builder details!", HttpStatus.OK, loginSupplier);
		}
		
	}
	
	
	@PostMapping(value = "/ResetSupplierPassword")
	//public ResponseEntity<Object> registerBuilder(@RequestBody BuilderDTO builderDTO) {
	public ResponseEntity<Object> ResetSupplierPassword(@RequestParam("otp") String otp, 
			@RequestParam("supplierDTO") String supplierDTOString) throws JsonMappingException, JsonProcessingException {
		System.out.println("supplierDTOtring" + new Gson().toJson(supplierDTOString));
		BuilderDTO registeredBuilder = new BuilderDTO();
		
		ObjectMapper objectMapper = new ObjectMapper();

		MaterialSupplierDTO materialSupplierDTO = new MaterialSupplierDTO();
		materialSupplierDTO = objectMapper.readValue(supplierDTOString, MaterialSupplierDTO.class);
		
		if(materialSupplierService.VerifyMaterialSuppliersOTP(materialSupplierDTO, otp)) {
			if(materialSupplierService.VerifyAlreadyRegisteredMaterialSuplier(materialSupplierDTO)) {
				materialSupplierService.ResetSupplierPassword(materialSupplierDTO);
				return generateResponse("Password changed!", HttpStatus.OK, null);
			}else {
				return generateResponse("Mobile Number not Registered!", HttpStatus.ALREADY_REPORTED, null);
			}
		}else {
			return generateResponse("Incorrect OTP!", HttpStatus.NOT_FOUND, null);
		}
		//return null;
		
	}
	
	
	@PostMapping(value = "/GenerateSuppliersOTP")
	public ResponseEntity<Object> GenerateSuppliersOTP(@RequestBody MaterialSupplierDTO materialSupplierDTO) {
		MaterialSupplierDTO loginMaterialSupplier = new MaterialSupplierDTO();
		materialSupplierService.saveSupplierOTP(materialSupplierDTO);
		return generateResponse("OTP generated!", HttpStatus.OK, null);
	}
	
	@PostMapping(value = "/GetAllOpenMaterialReqirements")
	//public ResponseEntity<Object> registerBuilder(@RequestBody ProductDTO ProductDTO) {
	public ResponseEntity<Object> GetAllOpenMaterialReqirements(@RequestBody MaterialSupplierDTO materialSupplierDTO) throws JsonMappingException, JsonProcessingException {
	//public ResponseEntity<Object> GetAllOpenMaterialReqirements(@RequestParam("supplierAvailCatg") String supplierAvailCatg, 
		//	@RequestParam("state") String state, @RequestParam("district") String district, @RequestParam("supplierId") String supplierId) throws JsonMappingException, JsonProcessingException {
		System.out.println("materialSupplierDTO" + new Gson().toJson(materialSupplierDTO));
		/*String avilCatgQuotesRemoved =supplierAvailCatg.replace("\"","");
		String avilCatgBracketRemoved = avilCatgQuotesRemoved.substring(1, avilCatgQuotesRemoved.length()-1);
		String[] avilCatg = avilCatgBracketRemoved.split(",");*/
		String stateQuotesRemoved =materialSupplierDTO.getMaterialSupplierAddress().getState().replace("\"","");
		String districtQuotesRemoved =materialSupplierDTO.getMaterialSupplierAddress().getDistrict().replace("\"","");
		List<MaterialRequirement> materialRequirement = new ArrayList<MaterialRequirement>();
		List<MaterialRequirementDTO> materialRequirementDTO = new ArrayList<MaterialRequirementDTO>();
		List<MaterialRequirementDTO> brandFilteredMaterialRequirementDTO = new ArrayList<MaterialRequirementDTO>();
		BuilderDTO builderDTO = new BuilderDTO();
		materialRequirement = materialSupplierService.GetAllOpenMaterialReqirements(materialSupplierDTO, stateQuotesRemoved, districtQuotesRemoved);
		
		
		if(materialRequirement != null) {
			try {
				materialRequirementDTO = materialRequirement.stream()
						.filter(materialReq-> !materialSupplierService.verifyIfEstimateAlreadySubmittedForMaterialRequirementByBuilderCustomer(materialReq, materialSupplierDTO.getMaterialSupplierBuilderId()))
						.map(matReq-> productService.setMaterialRequirementDTO(matReq)).collect(Collectors.toList());
				List<String> brandNames = new ArrayList<String>();

				materialSupplierDTO.getMaterialSupplierAvailableBrands().stream()
						.filter(brand -> brandNames.add(brand.getProductBrandForSupplierAvailableBrands().getProductBrandName()))
						.collect(Collectors.toList());

				List<String> distinctBrandNames = brandNames.stream().distinct().collect(Collectors.toList());
				
				brandFilteredMaterialRequirementDTO = materialRequirementDTO.stream()
						.map(matReqDTO-> this.setMaterialRequirementDTO(matReqDTO, distinctBrandNames))
						.filter(matReqDTO->matReqDTO.getMaterialRequirementItems() != null && !matReqDTO.getMaterialRequirementItems().isEmpty())
						.collect(Collectors.toList());
			}catch(Exception e) {
				System.out.println("Autowired Exception::::::" +e.getMessage());
			}
			
		}
		
		
		builderDTO.setMaterialRequirement(brandFilteredMaterialRequirementDTO);
		
		return generateResponse("Get All Material Open Requirements!", HttpStatus.OK, builderDTO);
		//return productCategoryDTO.getProductSubCategory();
		
	}
	
	
	public MaterialRequirementDTO setMaterialRequirementDTO(MaterialRequirementDTO materialRequirementDTO, List<String> distinctBrandNames) {
		List<MaterialRequirementItemsDTO> filteredItemsBrandAvail = null;
		if(materialRequirementDTO.getMaterialRequirementItems() != null) {
	    	filteredItemsBrandAvail = materialRequirementDTO.getMaterialRequirementItems().stream()
					.filter(item-> distinctBrandNames.contains(item.getProductForMaterialRequirementItems().getBrandName()))
					.collect(Collectors.toList());
	    }
		materialRequirementDTO.setMaterialRequirementItems(filteredItemsBrandAvail);
		return materialRequirementDTO;
		
	}


	/**/
	


	
	//public ResponseEntity<Object> registerBuilder(@RequestBody ProductDTO ProductDTO) {
	//public ResponseEntity<Object> uploadSupplierEstimatewithTotalCost(@RequestBody List<MaterialRequirementItemsEstimateDTO> MaterialRequirementItemsEstimates) throws JsonMappingException, JsonProcessingException {
	//@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	//@PostMapping(value = "/uploadSupplierEstimatewithTotalCost", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	//public ResponseEntity<Object> uploadSupplierEstimatewithTotalCost(@RequestBody MaterialRequirementDTO materialRequirementDTO) throws JsonMappingException, JsonProcessingException {
	@PostMapping(value = "/uploadSupplierEstimatewithTotalCost", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> uploadSupplierEstimatewithTotalCost(@RequestBody MaterialRequirementDTO materialRequirementDTO) throws JsonMappingException, JsonProcessingException {
		System.out.println("materialSupplierDTO" + new Gson().toJson(materialRequirementDTO));
		List<MaterialRequirementItemsEstimate> materialRequirementItemEstimates = null;
		
		materialRequirementItemEstimates = materialRequirementDTO.getMaterialRequirementItemsEstimate().stream().map(estimate->materialSupplierService.copySupplierBasicAvailableItemEstimatesDTOToEntity(estimate, new MaterialRequirementItemsEstimate())).collect(Collectors.toList());
		if(materialRequirementItemEstimates != null) {
			try {
				 materialRequirementItemEstimates.stream().map(estimateEntity->materialSupplierService.saveSupplierEstimate(estimateEntity)).collect(Collectors.toList());
			}catch(Exception e) {
				System.out.println("Autowired Exception::::::" +e.getMessage());
			}
			
		}
		
		return generateResponse("Supplier Estimates added Successfull!", HttpStatus.OK, null);
		//return productCategoryDTO.getProductSubCategory();
		
	}

	
	
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
	
	

}
