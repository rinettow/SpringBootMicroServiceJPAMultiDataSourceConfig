package com.shop.organic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.organic.dto.CarDTO;
import com.shop.organic.dto.CategoryDTO;
import com.shop.organic.dto.testDto;
import com.shop.organic.exception.ResourceNotFoundException;
import com.shop.organic.service.CarService;
import com.shop.organic.service.CategoryService;

@Validated
@RestController
@RequestMapping("/Car")
public class CarController {
	
	@Autowired
	private CarService carService;
	
	List<CarDTO> carList=null;
	
	@GetMapping(value = "/Cars")
	public ResponseEntity<Object> getAllCars() {
		carList = carService.findCarList();
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		return generateResponse("List of Cars!", HttpStatus.OK, carList);
	}
	
	@GetMapping(value = "/{carId}")
	public ResponseEntity<Object> getCar(@PathVariable String carId) throws ResourceNotFoundException{
		carList = carService.getCarById(carId);
		//return new ResponseEntity<List<CategoryDTO>>(list, HttpStatus.OK);
		return generateResponse("List of Cars!", HttpStatus.OK, carList);
	}
	
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("message", message);
            map.put("status", status.value());
            map.put("data", responseObj);

            return new ResponseEntity<Object>(map,status);
    }

}
