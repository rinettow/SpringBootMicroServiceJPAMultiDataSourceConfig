package com.shop.organic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;

import org.hibernate.jpa.QueryHints;
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
import com.shop.organic.dto.MaterialSupplierDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProductCategoryDTO;
import com.shop.organic.dto.ProductDTO;
import com.shop.organic.dto.ProductSubCategoryDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuilderRedRequirements;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.MaterialRequirementItems;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.BuilderService;
import com.shop.organic.service.MaterialSupplierService;
import com.shop.organic.service.ProductService;

import org.springframework.scheduling.annotation.Async;

@CrossOrigin(origins = "http://localhost")
@Validated
@RestController
@RequestMapping("/Products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private HttpServletRequest request;

	List<BuilderDTO> buildersList = null;

	@PostMapping(value = "/getAllProductsBasedOnCategory")
	//public ResponseEntity<Object> registerBuilder(@RequestBody BuilderDTO builderDTO) {
	public ResponseEntity<Object> getAllProductsBasedOnCategory(@RequestParam("productCategoryId") String productCategoryId) throws JsonMappingException, JsonProcessingException {
		System.out.println("BuilderDirectory" + new Gson().toJson(productCategoryId));
		Map<String, List<ProductCategoryDTO>> responseProductCategory = new HashMap<String, List<ProductCategoryDTO>>();
		//ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
		List<ProductCategoryDTO> ProductCategoryDTO = new ArrayList<ProductCategoryDTO>();
		//List<ProductSubCategoryDTO> productSubCategory = null;
		
		ProductCategoryDTO = productService.getAllProductsBasedOnCategory(Integer.parseInt(productCategoryId.replace("\"", "")));
		if(ProductCategoryDTO != null) {
			responseProductCategory.put("productCategory", ProductCategoryDTO);
		}else {
			responseProductCategory.put("productCategory", ProductCategoryDTO);
		}
		
		ResponseEntity<Object> response = generateResponse("List of Product Category!", HttpStatus.OK, responseProductCategory);
		
		return response;
		//return productCategoryDTO.getProductSubCategory();
		
	}
	
	@PostMapping(value = "/addProductToCart")
	//public ResponseEntity<Object> registerBuilder(@RequestBody ProductDTO ProductDTO) {
	public ResponseEntity<Object> addProductToCart(@RequestParam("ProductId") String ProductId, 
			@RequestParam("customerOrBuilderId") String customerOrBuilderId,
			@RequestParam("isCustomerOrBuilder") String isCustomerOrBuilder,
			@RequestParam("productCategoryId") String productCategoryId,
			@RequestParam("productSubCategoryId") String productSubCategoryId,
			@RequestParam("quantity") String quantity) throws JsonMappingException, JsonProcessingException {
		System.out.println("BuilderDirectory" + new Gson().toJson(productCategoryId));
		MaterialRequirement materialRequirement = new MaterialRequirement();
		MaterialRequirementItems materialRequirementItems = new MaterialRequirementItems();
		
		ProductId = ProductId.replace("\"", "");
		customerOrBuilderId = customerOrBuilderId.replace("\"", "");
		isCustomerOrBuilder = isCustomerOrBuilder.replace("\"", "");
		productCategoryId = productCategoryId.replace("\"", "");
		productSubCategoryId = productSubCategoryId.replace("\"", "");
		quantity = quantity.replace("\"", "");
		
		if(!productService.checkIfOpenMaterialRequirementAvailable(customerOrBuilderId, isCustomerOrBuilder, productCategoryId)) {
			if(!productService.CheckIfCartAvailableAlready(customerOrBuilderId, isCustomerOrBuilder, productCategoryId)) {
				/*Create New cart..*/
				materialRequirement = productService.createMaterialRequirement(customerOrBuilderId, isCustomerOrBuilder, productCategoryId);
				materialRequirementItems = productService.addItemsToMaterialRequirement(materialRequirement.getMaterialRequirementId(), ProductId, productSubCategoryId, quantity);
				
			}else {
				/*Add product to the existing cart..*/
				materialRequirement = productService.getExistingCartMaterialRequirementId(customerOrBuilderId, isCustomerOrBuilder, productCategoryId);
				materialRequirementItems = productService.addItemsToMaterialRequirement(materialRequirement.getMaterialRequirementId(), ProductId, productSubCategoryId, quantity);
			}
			
		}else {
			System.out.println("Requirement already created for category, please wait for suppliers quotations or if and additional products needed edit the cart");
			return generateResponse("Requirement already created for category, please wait for suppliers quotations or if and additional products needed edit the cart!", HttpStatus.NOT_FOUND, null);
		}
		
		return generateResponse("Product added to cart Successfully!", HttpStatus.OK, null);
		//return productCategoryDTO.getProductSubCategory();
		
	}
	
	@PostMapping(value = "/fetchCart")
	//public ResponseEntity<Object> registerBuilder(@RequestBody ProductDTO ProductDTO) {
	public ResponseEntity<Object> fetchCart( 
			@RequestParam("customerOrBuilderId") String customerOrBuilderId,
			@RequestParam("isCustomerOrBuilder") String isCustomerOrBuilder,
			@RequestParam("productCategoryId") String productCategoryId) throws JsonMappingException, JsonProcessingException {
		System.out.println("BuilderDirectory" + new Gson().toJson(productCategoryId));
		MaterialRequirementDTO materialRequirementDTO = new MaterialRequirementDTO();
		
		customerOrBuilderId = customerOrBuilderId.replace("\"", "");
		isCustomerOrBuilder = isCustomerOrBuilder.replace("\"", "");
		productCategoryId = productCategoryId.replace("\"", "");
		materialRequirementDTO = productService.fetchCart(customerOrBuilderId, isCustomerOrBuilder, productCategoryId);
		
		if(materialRequirementDTO != null) {
			return generateResponse("Cart Retrieved Successfull!", HttpStatus.OK, materialRequirementDTO);
		}else {
			return generateResponse("Cart Empty!", HttpStatus.NOT_FOUND, materialRequirementDTO);
		}
		
		//return productCategoryDTO.getProductSubCategory();
		
	}
	
	
	@PostMapping(value = "/CheckOut")
	/*@RequestMapping(path = "/CheckOut", 
    consumes = MediaType.APPLICATION_JSON_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE, 
    method = {RequestMethod.POST})*/
	public ResponseEntity<Object> CheckOut(@RequestBody MaterialRequirementDTO materialRequirementDTO) {
		//public ResponseEntity<Object> CheckOut(@RequestParam("materialRequirementId") String materialRequirementId, @RequestParam("builderId") String builderId,
		//@RequestParam("customerId") String customerId, @RequestParam("productCategoryId") String productCategoryId,
		//@RequestParam("state") String state, @RequestParam("district") String district) {
		MaterialRequirementDTO materialRequirementDTOResponse = new MaterialRequirementDTO();
		MaterialRequirement materialRequirementEntity = productService.checkOut(materialRequirementDTO);
		
		
		return generateResponse("Checked out Successfull, New requirement created!", HttpStatus.OK, null);
	}
	
	
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
