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
import com.shop.organic.dto.CustomerDTO;
import com.shop.organic.dto.CustomerRequirementDTO;
import com.shop.organic.dto.PictureDTO;
import com.shop.organic.dto.ProjectsAvailableAmenitiesDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.dto.StateDTO;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
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

	@PostMapping(value = "/Builders")
	@HystrixCommand(fallbackMethod = "fallbackRetrieveAllBuilders")
	public ResponseEntity<Object> getAllBuilders(
			@RequestParam("amenitiesAndSpecificationsId") String amenitiesAndSpecificationsId) {
		throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
	}

	// @Async
	public ResponseEntity<Object> fallbackRetrieveAllBuilders(String amenitiesAndSpecificationsId)
			throws JsonMappingException, JsonProcessingException {
		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO = new AmenitiesAndSpecificationsDTO();
		ObjectMapper objectMapper = new ObjectMapper();
		amenitiesAndSpecificationsDTO = objectMapper.readValue(amenitiesAndSpecificationsId,
				AmenitiesAndSpecificationsDTO.class);
		buildersList = builderService.findBuildersList(amenitiesAndSpecificationsDTO.getAmenitiesAndSpecificationsId());
		Object uriVariables = null;
		final HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		// headers.setContentType(MediaType.valueOf(myFileData.getContentType()));
		// headers.setContentType(MediaType.IMAGE_JPEG);
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		/*
		 * ResponseEntity<String> responseEntity=new RestTemplate().getForEntity(
		 * "http://localhost:8888/Car/RibbonLoadbalancedController", String.class,
		 * uriVariables); String response=responseEntity.getBody();
		 */

		return generateResponse("List of Builders!", HttpStatus.OK, buildersList);
		// return (ResponseEntity<List<BuilderDTO>>)
		// ResponseEntity.ok().headers(headers).body(buildersList);
	}

	@PostMapping(value = "/RegisterCustomer")
	public ResponseEntity<Object> registerBuilder(@RequestBody CustomerDTO customerDTO) {
		System.out.println("BuilderDirectory" + new Gson().toJson(customerDTO));
		CustomerDTO registeredCustomerDTO = new CustomerDTO();
		Customer registeredCustomersEntity = new Customer();
		registeredCustomersEntity = customerService.registerCustomer(customerDTO);

		registeredCustomerDTO = customerService.setCustomerDTO(registeredCustomersEntity);
		customerService.ceateImageDirectoryForCustomer(registeredCustomerDTO);
		return generateResponse("List of Builders!", HttpStatus.OK, registeredCustomerDTO);
	}

	@PostMapping(value = "/CreateCustomerRequirement")
	public ResponseEntity<Object> CreateCustomerRequirement(@RequestParam("planPDFFileFormat") MultipartFile planPDFFileFormat,
			@RequestParam("landImagePNGorJPGFileFormat") MultipartFile landImagePNGorJPGFileFormat,
			@RequestParam("createRequirementDTO") String createRequirementDTO) throws JsonMappingException, JsonProcessingException {
		System.out.println("ProjectDetails" + new Gson().toJson(createRequirementDTO));
		ObjectMapper objectMapper = new ObjectMapper();

		CustomerRequirementDTO customerRequirementDTO = new CustomerRequirementDTO();
		customerRequirementDTO = objectMapper.readValue(createRequirementDTO, CustomerRequirementDTO.class);

		CustomerRequirementDTO newCustomerRequirementDTOAddded = new CustomerRequirementDTO();
		CustomerRequirement customerRequirementEntity = new CustomerRequirement();
		   /*Add new Customer Requirement entry with new requirement id created*/
		customerRequirementEntity = customerService.CreateCustomerRequirement(customerRequirementDTO);
		/*Set the newly created customer requirement id to DTO object*/
		newCustomerRequirementDTOAddded = customerService.setCustomerRequirementDTO(customerRequirementEntity);
		/*Based on the Newly created Custmer requirement id create the image directory for Customer requirement and save the multipart files in the directory*/
		customerService.ceateImageDirectoryForCustomerRequirement(newCustomerRequirementDTOAddded, planPDFFileFormat, landImagePNGorJPGFileFormat);
		/*Now again add the image path in customer requirement table*/
		customerRequirementEntity = customerService.CreateCustomerRequirement(newCustomerRequirementDTOAddded);
		newCustomerRequirementDTOAddded = customerService.setCustomerRequirementDTO(customerRequirementEntity);
		return generateResponse("List of Builders!", HttpStatus.OK, newCustomerRequirementDTOAddded);
	}

	@GetMapping(value = "/AllStates")
	public ResponseEntity<List<StateDTO>> getAllStatesAndrespectiveDistricts() {
		List<StateDTO> allStatesDTO = new ArrayList<StateDTO>();
		allStatesDTO = builderService.getAllStates();
		final HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		return (ResponseEntity<List<StateDTO>>) ResponseEntity.ok().headers(headers).body(allStatesDTO);
	}

	// @CrossOrigin(origins = "http://localhost", methods = {RequestMethod.POST,
	// RequestMethod.OPTIONS},
	// allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin",
	// "Access-Control-Request-Method", "Access-Control-Request-Headers"},
	// exposedHeaders = {"Access-Control-Allow-Origin",
	// "Access-Control-Allow-Credentials"})
	// <<<<<<@PostMapping(value = "/addNewProject", headers =
	// "content-type=multipart/*", consumes = {
	// MediaType.MULTIPART_FORM_DATA_VALUE})
	@PostMapping(value = "/addNewProject")
	public ResponseEntity<Object> addNewProject(@RequestParam("image") MultipartFile file,
			@RequestParam("projectDTO") String projectDTOString) throws IOException {
		System.out.println("ProjectDetails" + new Gson().toJson(projectDTOString));
		ObjectMapper objectMapper = new ObjectMapper();

		ProjectsDTO projectDTO = new ProjectsDTO();
		projectDTO = objectMapper.readValue(projectDTOString, ProjectsDTO.class);

		ProjectsDTO newProjectAddded = new ProjectsDTO();
		Projects newProjectEntity = new Projects();
		newProjectEntity = builderService.addNewProject(projectDTO);

		int projectid = newProjectEntity.getProjectId();
		List<ProjectsAvailableAmenitiesDTO> projectsAvailableAmenitiesDTOWithProjectId = projectDTO
				.getProjectsAvailableAmenities().stream()
				.peek(projectsAvailableAmenitiesDTO -> projectsAvailableAmenitiesDTO.setProjectId(projectid))
				.collect(Collectors.toList());
		List<ProjectsAvailableAmenities> projectsAvailableAmenities = projectsAvailableAmenitiesDTOWithProjectId
				.stream()
				.map(projectsAvailableAmenitiesDTO -> builderService.copyProjectsBasicAvailableAmenitiesDTOToEntity(
						projectsAvailableAmenitiesDTO, new ProjectsAvailableAmenities()))
				.collect(Collectors.toList());

		for (ProjectsAvailableAmenities availavleAmenitiesForProject : projectsAvailableAmenities) {
			builderService.registerProjectsAvailableAminities(availavleAmenitiesForProject);
		}
		;
		Object uriVariables = null;
		newProjectAddded = builderService.setProjectDTO(newProjectEntity,
				builderService.getBuildersById(newProjectEntity.getBuilderId()));
		builderService.ceateImageDirectoryForProject(newProjectAddded, file);
		// Save Project Image Directory path in DB
		newProjectEntity = builderService.addNewProject(newProjectAddded);
		newProjectAddded = builderService.setProjectDTO(newProjectEntity,
				builderService.getBuildersById(newProjectEntity.getBuilderId()));
		// throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, newProjectAddded);

		/*
		 * ProjectsDTO newProjectAddded = new ProjectsDTO();
		 * System.out.println("addNewProject" +file); System.out.println("addNewProject"
		 * +file); HttpHeaders responseHeaders = new HttpHeaders(); //need for
		 * cross-domain requests responseHeaders.add("Access-Control-Allow-Origin",
		 * "http://localhost"); responseHeaders.add("Access-Control-Allow-Headers",
		 * "Access-Control-Allow-Headers,Content-Type,Accept,X-Access-Token,X-Key,Authorization,X-Requested-With,Origin,Access-Control-Allow-Origin,Access-Control-Allow-Credentials,content-type=multipart/*"
		 * ); //this one is needed, if your browser should send cookies
		 * responseHeaders.add("Access-Control-Allow-Credentials", "true");
		 * //responseHeaders.setContentLength(resp.getBytes("UTF-8").length); return
		 * generateResponse("List of Projects!", HttpStatus.OK, new ProjectsDTO())
		 * .ok().headers(responseHeaders).
		 * body("Response with header using ResponseEntity");
		 */
	}

	@PostMapping(value = "/addNewPicture")
	public ResponseEntity<Object> addNewPicture(@RequestParam("image") MultipartFile file,
			@RequestParam("pictureDTO") String pictureDTOString) throws IOException {
		System.out.println("pictureDTO" + new Gson().toJson(pictureDTOString));
		ObjectMapper objectMapper = new ObjectMapper();

		PictureDTO pictureDTO = new PictureDTO();
		pictureDTO = objectMapper.readValue(pictureDTOString, PictureDTO.class);

		PictureDTO newPictureAddded = new PictureDTO();
		Picture newPictureEntity = new Picture();
		builderService.ceateImageDirectoryForPicture(pictureDTO, Integer.toString(pictureDTO.getBuilderId()), file);
		newPictureEntity = builderService.addNewPicture(pictureDTO);
		Object uriVariables = null;
		newPictureAddded = builderService.setPictureDTO(newPictureEntity);
		return generateResponse("List of Builders!", HttpStatus.OK, newPictureAddded);
	}

	@PostMapping(value = "/SendOTP")
	public ResponseEntity<Object> SendOTP(@RequestBody BuilderDTO builderDTO) {
		BuilderDTO loginBuilder = new BuilderDTO();
		System.out.println("builderDTO:::::Test" + new Gson().toJson(builderDTO));
		loginBuilder = builderService.sendOTP(builderDTO);
		Object uriVariables = null;
		// throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, loginBuilder);
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

	/*
	 * @GetMapping(value = "/{builderId}") public ResponseEntity<Object>
	 * getCar(@PathVariable String builderId) throws ResourceNotFoundException{
	 * buildersList = builderService.getBuilderById(builderId); //return new
	 * ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK); return
	 * generateResponse("List of Builders!", HttpStatus.OK, buildersList); }
	 */

	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
