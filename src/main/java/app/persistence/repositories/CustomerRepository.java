package app.persistence.repositories;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import app.persistence.entities.Customer;
import app.persistence.entities.Page;
import app.persistence.repositories.exceptions.DataNotFoundException;
import app.web.models.CustomerUpdateModel;

@Repository
public class CustomerRepository extends BaseRepository {
	
	@Value("${page.size}")
    private int pageSize;
	
	public void save(Customer customer) {
		
		entityManager.persist(customer);
	}
	
	public Object[] findBriefViewForUpdateByNationalId(String nationalId) {
		
		String query = "SELECT customer.id, customer.contactInfo.cellPhone, customer.contactInfo.email, customer.contactInfo.mailingAddress " + 
					   "FROM   Customer customer " +
					   "WHERE  customer.nationalId = :nationalId";
		
		Object[] rawRecord;
		try {
			
			rawRecord = entityManager.createQuery(query, Object[].class)
										.setParameter("nationalId", nationalId)
										.getSingleResult();
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}
		
		return rawRecord;
	}
	
	public Object[] findDetailedViewByNationalId(String nationalId) {
		
		String query = "SELECT customer.id, customer.name, customer.nationalId, customer.dateOfBirth, " + 
		               "       customer.contactInfo.cellPhone, customer.contactInfo.email, customer.contactInfo.mailingAddress, " + 
				       "       customer.bankAccountInfo.accountNumber, customer.bankAccountInfo.balance " +
					   "FROM   Customer customer " +
					   "WHERE  customer.nationalId = :nationalId";
		
		Object[] rawRecord;
		try {
			
			rawRecord = entityManager.createQuery(query, Object[].class)
										.setParameter("nationalId", nationalId)
										.getSingleResult();
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}
		
		return rawRecord;
	}

	public Page findPage(int pageIndex) {
		
		String dataQuery = "SELECT customer.name, customer.nationalId, " + 
		                   "       customer.bankAccountInfo.accountNumber, customer.bankAccountInfo.balance " +
						   "FROM   Customer customer";
		
		// pageIndex is zero based => will be translated to the index of the first requested element later
		int firstElementIndex = pageIndex * pageSize;
		
		List<Object[]> rawData = entityManager.createQuery(dataQuery, Object[].class)
												.setFirstResult(firstElementIndex)
												.setMaxResults(pageSize)
												.getResultList();		
		if (rawData.isEmpty()) {
			throw new DataNotFoundException();
		}
		
		String countQuery = "SELECT COUNT(*) " + 
		                    "FROM   Customer customer";
		
		long totalElements = entityManager.createQuery(countQuery, Long.class)
											.getSingleResult();
		
		int totalPages = (int) Math.ceil((totalElements * 1.0) / pageSize);		
		boolean isFirstPage = (pageIndex == 0);
		boolean isLastPage = ((pageIndex + 1) == totalPages);
		
		Page page = new Page();
		page.setData(rawData);
		page.setFirstPage(isFirstPage);
		page.setLastPage(isLastPage);
		
		return page;
	}
	
	public void update(int id, CustomerUpdateModel userInfoUpdateModel) {
		
		String updateStatement = "UPDATE Customer customer " + 
								 "SET    customer.contactInfo.cellPhone = :cellPhoneParam, " + 
				                 "       customer.contactInfo.email = :emailParam, " + 
								 "       customer.contactInfo.mailingAddress = :mailingAddressParam " +
								 "WHERE  customer.id = :idParam";

		entityManager.createQuery(updateStatement)
						.setParameter("cellPhoneParam", userInfoUpdateModel.getCellPhone())
						.setParameter("emailParam", userInfoUpdateModel.getEmail())
						.setParameter("mailingAddressParam", userInfoUpdateModel.getMailingAddress())
						.setParameter("idParam", id)
						.executeUpdate();
	}
	
	public void delete(int id) {
		
		String deleteStatement = "DELETE FROM Customer customer " + 
				 				 "WHERE  customer.id = :idParam";

		entityManager.createQuery(deleteStatement)
						.setParameter("idParam", id)
						.executeUpdate();
	}
	
}
