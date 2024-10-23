package com.shop.organic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.BuildersEstimate;
import com.shop.organic.entity.car.Customer;
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.BuilderService;
import com.shop.organic.service.CustomerService;

import org.springframework.scheduling.annotation.Async;

@CrossOrigin(origins = "http://localhost")
@Validated
@RestController
@RequestMapping("/Customer")
public class CustomerController {

	@Autowired
	private BuilderService builderService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private HttpServletRequest request;

	List<BuilderDTO> buildersList = null;

	@PostMapping(value = "/RegisterCustomer")
	public ResponseEntity<Object> registerBuilder(@RequestBody CustomerDTO customerDTO) {
		System.out.println("BuilderDirectory" + new Gson().toJson(customerDTO));
		CustomerDTO registeredCustomerDTO = new CustomerDTO();
		registeredCustomerDTO = customerService.registerCustomer(customerDTO);

		customerService.ceateImageDirectoryForCustomer(registeredCustomerDTO);
		return generateResponse("Customer Registered Successful!", HttpStatus.OK, registeredCustomerDTO);
	}

	@PostMapping(value = "/CreateCustomerRequirement")
	public ResponseEntity<Object> CreateCustomerRequirement(@RequestParam("planPDFFileFormat") MultipartFile planPDFFileFormat,
			@RequestParam("landImagePNGorJPGFileFormat") MultipartFile landImagePNGorJPGFileFormat,
			@RequestParam("customerRequirementDTO") String createRequirementDTO) throws JsonMappingException, JsonProcessingException {
		System.out.println("ProjectDetails" + new Gson().toJson(createRequirementDTO));
		ObjectMapper objectMapper = new ObjectMapper();

		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		customerRequirementDTO = objectMapper.readValue(createRequirementDTO, CustomerRequirementDTO.class);
		boolean isOpenRequirementAvailable = customerService.validateCustomersOpenRequirement(customerRequirementDTO);
		if(isOpenRequirementAvailable) {
			String amenityName = null;
			if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 1) {
				amenityName = "Full House/Flat Construction";
			}else if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 2) {
				amenityName = "Interior House Design";
			}else if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 3) {
				amenityName = "Electrical Work";
			}else if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 4) {
				amenityName = "Plumbing Work";
			}else if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 5) {
				amenityName = "Painting";
			}else if(customerRequirementDTO.getAmenityAndSpecifiactionId() == 6) {
				amenityName = "Solar Planting";
			}
			return generateResponse("Open Requirement is already available for " +amenityName+ "Please close the existing requirement", HttpStatus.NOT_FOUND, null);
		}
		CustomerRequirementDTO newCustomerRequirementDTOAddded = new CustomerRequirementDTO();
		   /*Add new Customer Requirement entry with new requirement id created*/
		newCustomerRequirementDTOAddded = customerService.CreateCustomerRequirement(customerRequirementDTO);
		/*Set the newly created customer requirement id to DTO object*/
		/*Based on the Newly created Custmer requirement id create the image directory for Customer requirement and save the multipart files in the directory*/
		customerService.ceateImageDirectoryForCustomerRequirement(newCustomerRequirementDTOAddded, planPDFFileFormat, landImagePNGorJPGFileFormat);
		/*Now again add the image path in customer requirement table*/
		newCustomerRequirementDTOAddded = customerService.CreateCustomerRequirement(newCustomerRequirementDTOAddded);
		return generateResponse("List of Builders!", HttpStatus.OK, newCustomerRequirementDTOAddded);
	}
	
	
	@PostMapping(value = "/getEstimate")
	public ResponseEntity<Object> getEstimate(@RequestParam("builderId") String builderId,
			@RequestParam("projectsId") String projectsId,
			@RequestParam("customerId") String customerId,
			@RequestParam("amenitiesAndSpecificationsDTO") String amenitiesAndSpecifications) throws JsonMappingException, JsonProcessingException {
		BuildersEstimateDTO buildersEstimateDTO;
		ObjectMapper objectMapper = new ObjectMapper();
		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO = new AmenitiesAndSpecificationsDTO();
		amenitiesAndSpecificationsDTO = objectMapper.readValue(amenitiesAndSpecifications, AmenitiesAndSpecificationsDTO.class);
		List<CustomerRequirementDTO> customerOpenRequirement= customerService.getCustomersOpenRequirement(Integer.parseInt(customerId), amenitiesAndSpecificationsDTO.getAmenitiesAndSpecificationsId());
		if(customerOpenRequirement != null && customerOpenRequirement.size() == 1) {
			/*if(!customerService.validateIfQouteAlreadyRequestedToBuilder(customerOpenRequirement.get(0).getCustomerRequirementId(), Integer.parseInt(builderId))) {
				buildersEstimateDTO =	customerService.addBuildersEstimateEntry(customerOpenRequirement.get(0).getCustomerRequirementId(), Integer.parseInt(customerId), 
						amenitiesAndSpecificationsDTO.getAmenitiesAndSpecificationsId(), Integer.parseInt(projectsId), Integer.parseInt(builderId));
			}else {
				return generateResponse("Get Quote was already requested to this Builder ", HttpStatus.ALREADY_REPORTED, null);
			}*/
			return generateResponse("Open Requirement already Available, and quoation request sent to builders, please check the quoatations in View Estimate tab ", HttpStatus.OK, null);
			
		}else {
			return generateResponse("Open Requirement is not available , Please create Requirement", HttpStatus.NOT_FOUND, null);
		}
		
	}
	
	@PostMapping(value = "/AcceptDeclineQuotation")
	public ResponseEntity<Object> AcceptDeclineQuotation(
			@RequestParam("buildersEstimate") String buildersEstimateString) throws IOException {
		System.out.println("pictureDTO" + new Gson().toJson(buildersEstimateString));
		ObjectMapper objectMapper = new ObjectMapper();

		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimateDTO = objectMapper.readValue(buildersEstimateString, BuildersEstimateDTO.class);

		BuildersEstimateDTO uploadedEstimate = new BuildersEstimateDTO();
		if(buildersEstimateDTO.getCustomerAcceptedDeclined().equals("ACCEPT")) {
			boolean isAnyQuotationAcceptedForRequirement = builderService.VerifyIfAnyQuotationAcceptedForRequirement(buildersEstimateDTO);
			if(isAnyQuotationAcceptedForRequirement) {
				return generateResponse("Quotation Already Accepted!", HttpStatus.ALREADY_REPORTED, uploadedEstimate);
			}else {
				uploadedEstimate = builderService.AcceptDeclineQuotation(buildersEstimateDTO);
				builderService.DeclineRestAllQuotationsExceptApprovedQuote(buildersEstimateDTO);
			}
		}else if(buildersEstimateDTO.getCustomerAcceptedDeclined().equals("DECLINE")) {
			uploadedEstimate = builderService.AcceptDeclineQuotation(buildersEstimateDTO);
		}
		
		
		return generateResponse("List of Builders!", HttpStatus.OK, uploadedEstimate);
	}
	
	@PostMapping(value = "/SubmitReview")
	public ResponseEntity<Object> SubmitReview(
			@RequestParam("buildersEstimate") String buildersEstimateString) throws IOException {
		System.out.println("pictureDTO" + new Gson().toJson(buildersEstimateString));
		ObjectMapper objectMapper = new ObjectMapper();

		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimateDTO = objectMapper.readValue(buildersEstimateString, BuildersEstimateDTO.class);

		BuildersEstimateDTO submitedReview = new BuildersEstimateDTO();
		submitedReview = builderService.SubmitReview(buildersEstimateDTO);
		
		
		return generateResponse("List of Builders!", HttpStatus.OK, submitedReview);
	}

	@GetMapping(value = "/AllStates")
	public ResponseEntity<List<StateDTO>> getAllStatesAndrespectiveDistricts() {
		List<StateDTO> allStatesDTO = new ArrayList<StateDTO>();
		allStatesDTO = builderService.getAllStates();
		final HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		return (ResponseEntity<List<StateDTO>>) ResponseEntity.ok().headers(headers).body(allStatesDTO);
	}

	@PostMapping(value = "/SendOTPCustomerLogin")
	public ResponseEntity<Object> SendOTPCustomerLogin(@RequestBody CustomerDTO customerDTO) {
		CustomerDTO loginCustomer = new CustomerDTO();
		System.out.println("builderDTO:::::Test" + new Gson().toJson(customerDTO));
		loginCustomer = customerService.sendOTPForCustomerLogin(customerDTO);
		Object uriVariables = null;
		// throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, loginCustomer);
	}

	@PostMapping(value = "/getProjectDetailsById")
	public ResponseEntity<Object> getProjectDetailsById(@RequestBody ProjectsDTO projectsDTO) {
		ProjectsDTO selectedProject = new ProjectsDTO();
		System.out.println("builderDTO:::::Test" + new Gson().toJson(projectsDTO));
		selectedProject = builderService.getProjectDetailsById(projectsDTO);
		Object uriVariables = null;
		// throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, selectedProject.getPicture());
	}


	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
