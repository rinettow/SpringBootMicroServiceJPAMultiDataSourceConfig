package com.shop.organic;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.organic.dto.MaterialRequirementItemsEstimateDTO; 

public class Test {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		String value = "8008";
		System.out.println(value.replace("\"",""));
		String availCatg = "[6,2,3,4]";
		System.out.println(availCatg.substring(1, availCatg.length()-1));
		int length = 4; 
        //System.out.println(OTP(length)); 
        String req = "[{\"materialRequirementItemId\":6,\"materialRequirementId\":2,\"materialSupplierId\":13,\"totalPrice\":4800,\"customerBuilderAceptedDeclined\":\"ON_HOLD\",\"deliveryCharge\":600}][{\"materialRequirementItemId\":6,\"materialRequirementId\":2,\"materialSupplierId\":13,\"totalPrice\":4800,\"customerBuilderAceptedDeclined\":\"ON_HOLD\",\"deliveryCharge\":600}]";
        ObjectMapper objectMapper = new ObjectMapper();

		List<MaterialRequirementItemsEstimateDTO> MaterialRequirementItemsEstimates = new ArrayList<MaterialRequirementItemsEstimateDTO>();
		MaterialRequirementItemsEstimates = (List<MaterialRequirementItemsEstimateDTO>) objectMapper.readValue(req, MaterialRequirementItemsEstimateDTO.class);
        char[] otpGeneratedForBuilder = OTP(length);
        System.out.println(otpGeneratedForBuilder); 
		//String[] otpGeneratedForBuilderStringArr = new String[otpGeneratedForBuilder.length];
		String otpGeneratedForBuilderConcated = null;
		for (int i = 0; i < otpGeneratedForBuilder.length; i++) {
	        //ints[i] = Character.getNumericValue(otpGeneratedForBuilder[i]);
			System.out.println(String.valueOf(otpGeneratedForBuilder[i])); 
			otpGeneratedForBuilderConcated = otpGeneratedForBuilderConcated + String.valueOf(otpGeneratedForBuilder[i]);
			//otpGeneratedForBuilderConcated.concat(String.valueOf(otpGeneratedForBuilder[i]));
			//otpGeneratedForBuilderStringArr[i] = String.valueOf(otpGeneratedForBuilder[i]);
	    }
		
		System.out.println(otpGeneratedForBuilderConcated.substring(4)); 
	}
	
	static char[] OTP(int len) 
    { 
        //System.out.println("Generating OTP using random() : "); 
       // System.out.print("You OTP is : "); 
  
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

}
