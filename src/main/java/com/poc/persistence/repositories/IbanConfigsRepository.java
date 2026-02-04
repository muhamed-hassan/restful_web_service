package com.poc.persistence.repositories;

import org.springframework.stereotype.Repository;

import com.poc.persistence.entities.IbanConfigs;

@Repository
public class IbanConfigsRepository extends BaseRepository {
	
	public IbanConfigs findById(int id) {
		
		IbanConfigs ibanConfigs = entityManager.find(IbanConfigs.class, id);
		
		return ibanConfigs;
	}

}
