package com.shop.organic.proxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="currency-exchange-service", url="localhost:8000")
//Enabling feign
@FeignClient(name = "spring-cloud-config-server")
//enabling ribbon
@RibbonClient(name = "spring-cloud-config-server")
public interface RibbonLoadbalancerProxy {

	@GetMapping("/RibbonLoadbalancedController") // where {from} and {to} are path variable
	public String RibbonLoadbalancedController(); // from map to USD and to map to INR

}
