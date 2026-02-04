package com.poc.persistence.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseRepository {

	@PersistenceContext
    protected EntityManager entityManager;
	
}
