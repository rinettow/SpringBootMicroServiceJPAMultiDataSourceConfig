package com.shop.organic.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CreateEntityManager {
	
	@Autowired
	@Qualifier("builderEntityManagerFactory")  
    EntityManagerFactory builderEm;
	
	@Autowired
	@Qualifier("categoryEntityManagerFactory")  
    EntityManagerFactory categoryEm;
     
    public EntityManager getEntityManager(String enviroment) {
        EntityManagerFactory entityManager = null;
        if(enviroment.equals("builder")) {
            entityManager = this.builderEm;
        }else if(enviroment.equals("category")){
        	 entityManager = this.categoryEm;
        }
        return entityManager.createEntityManager();
    }
	

}
