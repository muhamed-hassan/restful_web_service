package app.persistence.repositories;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import app.persistence.entities.IbanConfiguration;
import app.persistence.repositories.exceptions.DataNotFoundException;

@Repository
public class IbanConfigurationRepository extends BaseRepository {
	
	public IbanConfiguration findById(int id) {
		
		String query = "SELECT ibanConfiguration " + 
					   "FROM   IbanConfiguration ibanConfiguration " +
					   "WHERE  ibanConfiguration.id = :idParam";

		IbanConfiguration ibanConfiguration;
		try {
			
			ibanConfiguration = entityManager.createQuery(query, IbanConfiguration.class)
												.setParameter("idParam", id)
												.getSingleResult();
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}
	
		return ibanConfiguration;
	}

}
