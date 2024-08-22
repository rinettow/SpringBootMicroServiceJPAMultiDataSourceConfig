package com.shop.organic.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.shop.organic" })
public class WebConfig implements WebMvcConfigurer {

	/*
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/api/**") .allowedOrigins("http://localhost:3000")
	 * .allowedMethods("PUT", "DELETE", "GET") .allowedHeaders("header1", "header2",
	 * "header3") .exposedHeaders("header1", "header2")
	 * .allowCredentials(false).maxAge(3600); }
	 */

	@Value("${appName.allowedApi}")
	private String myAllowedApi;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("Access-Control-Allow-Origin",
				// registry.addMapping("/addNewProject")
				// .allowedHeaders("Access-Control-Allow-Origin",
				"*", "Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE", "Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept").allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedOrigins(myAllowedApi);

		/*
		 * registry.addMapping("/**") .allowedOrigins("*") .allowedMethods("GET",
		 * "POST", "PUT", "PATCH", "DELETE", "OPTIONS") .allowedHeaders("*")
		 * .exposedHeaders("Authorization") .allowCredentials(true) .maxAge(3600);
		 */

	}

	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
		converters.add(byteArrayHttpMessageConverter());
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		return converter;
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
		return arrayHttpMessageConverter;
	}

	private List<MediaType> getSupportedMediaTypes() {
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.IMAGE_JPEG);
		list.add(MediaType.IMAGE_PNG);
		list.add(MediaType.APPLICATION_OCTET_STREAM);
		return list;
	}

}
