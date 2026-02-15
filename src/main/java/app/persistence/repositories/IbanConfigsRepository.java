package app.persistence.repositories;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import app.persistence.entities.IbanConfigs;
import app.persistence.repositories.exceptions.DataNotFoundException;

@Repository
public class IbanConfigsRepository extends BaseRepository {
	
	public IbanConfigs findById(int id) {
		
		String query = "SELECT ibanConfigs " + 
					   "FROM   IbanConfigs ibanConfigs " +
					   "WHERE  ibanConfigs.id = :idParam";

		IbanConfigs ibanConfigs;
		try {
			
			ibanConfigs = entityManager.createQuery(query, IbanConfigs.class)
										.setParameter("idParam", id)
										.getSingleResult();
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}
	
		return ibanConfigs;
	}

}
