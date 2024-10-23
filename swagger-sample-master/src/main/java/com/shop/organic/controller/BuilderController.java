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
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;
import com.shop.organic.entity.car.State;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.BuilderService;
import org.springframework.scheduling.annotation.Async;

@CrossOrigin(origins = "http://localhost")
@Validated
@RestController
@RequestMapping("/Builder")
public class BuilderController {

	@Autowired
	private BuilderService builderService;

	@Autowired
	private HttpServletRequest request;

	List<BuilderDTO> buildersList = null;

	@PostMapping(value = "/Builders")
	// @HystrixCommand(fallbackMethod = "fallbackRetrieveAllBuilders")
	public ResponseEntity<Object> getAllBuilders(
			@RequestParam("amenitiesAndSpecificationsId") String amenitiesAndSpecificationsId)
			throws JsonMappingException, JsonProcessingException {
		// throw new RuntimeException("Not Available");

		AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsDTO = new AmenitiesAndSpecificationsDTO();
		List<BuilderDTO> buildersList = null;
		List<BuilderDTO> interiorDesignersList = null;
		List<BuilderDTO> electriciansList = null;
		List<BuilderDTO> plumbersList = null;
		List<BuilderDTO> paintersList = null;
		List<BuilderDTO> solarPlantersList = null;
		Map<String, List<BuilderDTO>> responseBuildersMap = new HashMap<String, List<BuilderDTO>>();
		ObjectMapper objectMapper = new ObjectMapper();
		amenitiesAndSpecificationsDTO = objectMapper.readValue(amenitiesAndSpecificationsId,
				AmenitiesAndSpecificationsDTO.class);
		for (int i = 1; i <= 6; i++) {
			if (i == 1) {
				buildersList = builderService.findBuildersList(1);
				responseBuildersMap.put("buildersList", buildersList);
			}
			if (i == 2) {
				interiorDesignersList = builderService.findBuildersList(2);
				responseBuildersMap.put("interiorDesignersList", interiorDesignersList);
			}
			if (i == 3) {
				electriciansList = builderService.findBuildersList(3);
				responseBuildersMap.put("electriciansList", electriciansList);
			}
			if (i == 4) {
				plumbersList = builderService.findBuildersList(4);
				responseBuildersMap.put("plumbersList", plumbersList);
			}
			if (i == 5) {
				paintersList = builderService.findBuildersList(5);
				responseBuildersMap.put("paintersList", paintersList);
			}
			/*if (i == 6) {
				solarPlantersList = builderService.findBuildersList(6);
				responseBuildersMap.put("solarPlantersList", solarPlantersList);
			}*/
		}

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

		return generateResponse("List of Builders!", HttpStatus.OK, responseBuildersMap);
		// return (ResponseEntity<List<BuilderDTO>>)
		// ResponseEntity.ok().headers(headers).body(buildersList);

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

	@PostMapping(value = "/RegisterBuilder")
	public ResponseEntity<Object> registerBuilder(@RequestBody BuilderDTO builderDTO) {
		System.out.println("BuilderDirectory" + new Gson().toJson(builderDTO));
		BuilderDTO registeredBuilder = new BuilderDTO();
		registeredBuilder = builderService.registerBuilder(builderDTO);
		int builderid = registeredBuilder.getBuilderId();
		List<BuildersAvailableAmenitiesDTO> buildersAvailableAmenitiesDTOWithBuilderId = builderDTO
				.getBuildersAvailableAmenities().stream()
				.peek(buildersAvailableAmenitiesDTO -> buildersAvailableAmenitiesDTO.setBuilderId(builderid))
				.collect(Collectors.toList());
		List<BuildersAvailableAmenities> buildersAvailableAmenities = buildersAvailableAmenitiesDTOWithBuilderId
				.stream()
				.map(buildersAvailableAmenitiesDTO -> builderService.copyBuildersBasicAvailableAmenitiesDTOToEntity(
						buildersAvailableAmenitiesDTO, new BuildersAvailableAmenities()))
				.collect(Collectors.toList());
		for (BuildersAvailableAmenities availavleAmenities : buildersAvailableAmenities) {
			builderService.registerBuildersAvailableAminities(availavleAmenities);
		}
		;

		Object uriVariables = null;
		builderService.ceateImageDirectoryForBuilder(registeredBuilder);
		// throw new RuntimeException("Not Available");
		// carList = carService.findCarList();
		// return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		// return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, registeredBuilder);
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
		newProjectAddded = builderService.addNewProject(projectDTO);

		int projectid = newProjectAddded.getProjectId();
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
		builderService.ceateImageDirectoryForProject(newProjectAddded, file);
		// Save Project Image Directory path in DB
		newProjectAddded = builderService.addNewProject(newProjectAddded);
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
		builderService.ceateImageDirectoryForPicture(pictureDTO, Integer.toString(pictureDTO.getBuilderId()), file);
		newPictureAddded = builderService.addNewPicture(pictureDTO);
		return generateResponse("List of Builders!", HttpStatus.OK, newPictureAddded);
	}

	@PostMapping(value = "/UploadBuildersEstimatePDF")
	public ResponseEntity<Object> UploadBuildersEstimatePDF(
			@RequestParam("uploadBuildersEstimatePDF") MultipartFile file,
			@RequestParam("buildersEstimate") String buildersEstimateString) throws IOException {
		System.out.println("pictureDTO" + new Gson().toJson(buildersEstimateString));
		ObjectMapper objectMapper = new ObjectMapper();

		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimateDTO = objectMapper.readValue(buildersEstimateString, BuildersEstimateDTO.class);

		BuildersEstimateDTO uploadedEstimate = new BuildersEstimateDTO();
		builderService.ceateImageDirectoryForBuildersEstimate(buildersEstimateDTO, file, null);
		uploadedEstimate = builderService.uploadBuildersEstimatePDF(buildersEstimateDTO);
		return generateResponse("List of Builders!", HttpStatus.OK, uploadedEstimate);
	}

	@PostMapping(value = "/ProvideBuildersEstimate")
	public ResponseEntity<Object> ProvideBuildersEstimate(@RequestParam("uploadBuildersEstimatePDF") MultipartFile file,
			@RequestParam("buildersEstimate") String buildersEstimateString,
			@RequestParam("customerId") String customerId) throws IOException {
		System.out.println("pictureDTO" + new Gson().toJson(buildersEstimateString));
		ObjectMapper objectMapper = new ObjectMapper();

		BuildersEstimateDTO buildersEstimateDTO = new BuildersEstimateDTO();
		buildersEstimateDTO = objectMapper.readValue(buildersEstimateString, BuildersEstimateDTO.class);

		BuildersEstimateDTO uploadedEstimate = new BuildersEstimateDTO();
		builderService.ceateImageDirectoryForBuildersEstimate(buildersEstimateDTO, file, customerId);
		uploadedEstimate = builderService.uploadBuildersEstimatePDF(buildersEstimateDTO);
		return generateResponse("List of Builders!", HttpStatus.OK, uploadedEstimate);
	}

	@PostMapping(value = "/GetAllOpenReqirements")
	public ResponseEntity<Object> GetAllOpenReqirements(@RequestBody BuilderDTO builderDTO) throws IOException {
		List<CustomerRequirementDTO> openCustomerRequirementDTO = new ArrayList<CustomerRequirementDTO>();
		CustomerDTO allCustomerRequirements = new CustomerDTO();
		openCustomerRequirementDTO = builderService.getAllOpenTenders(builderDTO);
		allCustomerRequirements.setCustomerRequirement(openCustomerRequirementDTO);
		return generateResponse("List of Builders!", HttpStatus.OK, allCustomerRequirements);
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
