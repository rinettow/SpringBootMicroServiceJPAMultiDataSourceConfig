package com.shop.organic;

import java.io.File;

//import org.apache.commons.net.ftp.FTPFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
/*import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;*/

@SpringBootApplication
@EnableHystrix
@EnableEurekaServer
@EnableDiscoveryClient
@EnableFeignClients("com.shop.organic.controller")  
public class SwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwaggerApplication.class, args);
	}
	
	

}
