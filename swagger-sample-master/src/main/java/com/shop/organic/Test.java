package com.shop.organic;
import java.util.*; 

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String value = "8008";
		System.out.println(value.replace("\"",""));
		

		int length = 4; 
        //System.out.println(OTP(length)); 
        
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
