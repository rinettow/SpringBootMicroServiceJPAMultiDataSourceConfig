package com.shop.organic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shop.organic.dto.BuilderDTO;
import com.shop.organic.dto.ProjectsDTO;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.BuilderService;


@CrossOrigin(origins = "http://localhost")
@Validated
@RestController
@RequestMapping("/Builder")
public class BuilderController {
	
	@Autowired
	private BuilderService builderService;
	
	@Autowired
    private HttpServletRequest request;
	
	List<BuilderDTO> buildersList=null;
	
	@GetMapping(value = "/Builders")
	@HystrixCommand(fallbackMethod="fallbackRetrieveAllBuilders")
	public ResponseEntity<List<BuilderDTO>> getAllBuilders() {
		throw new RuntimeException("Not Available");
		//carList = carService.findCarList();
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		//return generateResponse("List of Cars!", HttpStatus.OK, carList);
	}
	
	public ResponseEntity<List<BuilderDTO>> fallbackRetrieveAllBuilders() {
		buildersList = builderService.findBuildersList();
		Object uriVariables = null;
		final HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		//headers.setContentType(MediaType.valueOf(myFileData.getContentType()));
	    //headers.setContentType(MediaType.IMAGE_JPEG);
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
	    /*ResponseEntity<String> responseEntity=new RestTemplate().getForEntity("http://localhost:8888/Car/RibbonLoadbalancedController", String.class, uriVariables);
	    String response=responseEntity.getBody();*/

		//return generateResponse("List of Builders!", HttpStatus.OK, buildersList);
		return (ResponseEntity<List<BuilderDTO>>) ResponseEntity.ok().headers(headers).body(buildersList);
	}
	
	@PostMapping(value = "/RegisterBuilder")
	public ResponseEntity<Object> registerBuilder(@RequestBody BuilderDTO builderDTO) {
		System.out.println("BuilderDirectory" + new Gson().toJson(builderDTO));
		BuilderDTO registeredBuilder = new BuilderDTO();
		Builder registeredBuilderEntity = new Builder();
		registeredBuilderEntity = builderService.registerBuilder(builderDTO);
		Object uriVariables = null;
		registeredBuilder= builderService.setBuilderDTO(registeredBuilderEntity);
		builderService.ceateImageDirectoryForBuilder(registeredBuilder);
		//throw new RuntimeException("Not Available");
		//carList = carService.findCarList();
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		//return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, registeredBuilder);
	}
	
	//@CrossOrigin(origins = "http://localhost", methods = {RequestMethod.POST, RequestMethod.OPTIONS}, 
	//allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
	//<<<<<<@PostMapping(value = "/addNewProject", headers = "content-type=multipart/*", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
	@PostMapping(value = "/addNewProject")
	public ResponseEntity<Object> addNewProject(@RequestParam("image") MultipartFile file,
			@RequestParam("projectDTO") String projectDTOString) throws IOException{
		System.out.println("ProjectDetails" + new Gson().toJson(projectDTOString));
		ObjectMapper objectMapper = new ObjectMapper();
		
		ProjectsDTO projectDTO = new ProjectsDTO();
		projectDTO= objectMapper.readValue(projectDTOString, ProjectsDTO.class);
		
		ProjectsDTO newProjectAddded = new ProjectsDTO();
		Projects newProjectEntity = new Projects();
		newProjectEntity = builderService.addNewProject(projectDTO);
		Object uriVariables = null;
		newProjectAddded= builderService.setProjectDTO(newProjectEntity);
		builderService.ceateImageDirectoryForProject(newProjectAddded, file);
		// Save Project Image Directory path in DB 
		newProjectEntity = builderService.addNewProject(newProjectAddded);
		newProjectAddded= builderService.setProjectDTO(newProjectEntity);
		//throw new RuntimeException("Not Available");
		//carList = carService.findCarList();
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		//return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, newProjectAddded);
		
		/*ProjectsDTO newProjectAddded = new ProjectsDTO();
		System.out.println("addNewProject" +file);
		System.out.println("addNewProject" +file);
		HttpHeaders responseHeaders = new HttpHeaders();
	    //need for cross-domain requests
	    responseHeaders.add("Access-Control-Allow-Origin", "http://localhost");
	    responseHeaders.add("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,Content-Type,Accept,X-Access-Token,X-Key,Authorization,X-Requested-With,Origin,Access-Control-Allow-Origin,Access-Control-Allow-Credentials,content-type=multipart/*");
	    //this one is needed, if your browser should send cookies
	    responseHeaders.add("Access-Control-Allow-Credentials", "true");
	    //responseHeaders.setContentLength(resp.getBytes("UTF-8").length);
	    return generateResponse("List of Projects!", HttpStatus.OK, new ProjectsDTO())
	    		.ok().headers(responseHeaders).body("Response with header using ResponseEntity");*/
	}
	
	
	@PostMapping(value = "/SendOTP")
	public ResponseEntity<Object> SendOTP(@RequestBody BuilderDTO builderDTO) {
		BuilderDTO loginBuilder = new BuilderDTO();
		System.out.println("builderDTO:::::Test" + new Gson().toJson(builderDTO));
		loginBuilder = builderService.sendOTP(builderDTO);
		Object uriVariables = null;
		//throw new RuntimeException("Not Available");
		//carList = carService.findCarList();
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		//return generateResponse("List of Cars!", HttpStatus.OK, carList);
		return generateResponse("List of Builders!", HttpStatus.OK, loginBuilder);
	}
	
	
	@GetMapping(value = "/{builderId}")
	public ResponseEntity<Object> getCar(@PathVariable String builderId) throws ResourceNotFoundException{
		buildersList = builderService.getBuilderById(builderId);
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		return generateResponse("List of Builders!", HttpStatus.OK, buildersList);
	}
	
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("message", message);
            map.put("status", status.value());
            map.put("data", responseObj);

            return new ResponseEntity<Object>(map,status);
    }

}
