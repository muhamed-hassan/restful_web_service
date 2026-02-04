package com.poc.persistence.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.poc.persistence.entities.Page;
import com.poc.persistence.entities.UserInfo;
import com.poc.web.models.UserInfoUpdateModel;

@Repository
public class UserInfoRepository extends BaseRepository {
	
	@Value("${page.size}")
    private int pageSize;
	
	public void save(UserInfo userInfo) {
		
		entityManager.persist(userInfo);
	}
	
	public Object[] findBriefViewForUpdateByNationalId(String nationalId) {
		
		String query = "SELECT userInfo.id, userInfo.contactInfo.cellPhone, userInfo.contactInfo.email, userInfo.contactInfo.mailingAddress " + 
					   "FROM   UserInfo userInfo " +
					   "WHERE  userInfo.nationalId = :nationalId";
		
		Object[] rawUserInfo = entityManager.createQuery(query, Object[].class)
											.setParameter("nationalId", nationalId)
											.getSingleResult();
		
		return rawUserInfo;
	}
	
	public Object[] findDetailedViewByNationalId(String nationalId) {
		
		String query = "SELECT userInfo.id, userInfo.name, userInfo.nationalId, userInfo.dateOfBirth, " + 
		               "       userInfo.contactInfo.cellPhone, userInfo.contactInfo.email, userInfo.contactInfo.mailingAddress, " + 
				       "       userInfo.bankAccountInfo.accountNumber, userInfo.bankAccountInfo.balance " +
					   "FROM   UserInfo userInfo " +
					   "WHERE  userInfo.nationalId = :nationalId";
		
		Object[] rawUserInfo = entityManager.createQuery(query, Object[].class)
											.setParameter("nationalId", nationalId)
											.getSingleResult();
		
		return rawUserInfo;
	}

	public Page findPage(int pageIndex) {
		
		String dataQuery = "SELECT userInfo.name, userInfo.nationalId, " + 
		                   "       userInfo.bankAccountInfo.accountNumber, userInfo.bankAccountInfo.balance " +
						   "FROM   UserInfo userInfo";
		
		// pageIndex is zero based => will be translated to the index of the first requested element later
		int firstElementIndex = pageIndex * pageSize;
		
		List<Object[]> rawData = entityManager.createQuery(dataQuery, Object[].class)
												.setFirstResult(firstElementIndex)
												.setMaxResults(pageSize)
												.getResultList();
		
		String countQuery = "SELECT COUNT(*) " + 
		                    "FROM   UserInfo userInfo";
		
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
	
	public void update(int id, UserInfoUpdateModel userInfoUpdateModel) {
		
		String updateStatement = "UPDATE UserInfo userInfo " + 
								 "SET    userInfo.contactInfo.cellPhone = :cellPhoneParam, " + 
				                 "       userInfo.contactInfo.email = :emailParam, " + 
								 "       userInfo.contactInfo.mailingAddress = :mailingAddressParam " +
								 "WHERE  userInfo.id = :idParam";

		entityManager.createQuery(updateStatement)
						.setParameter("cellPhoneParam", userInfoUpdateModel.getCellPhone())
						.setParameter("emailParam", userInfoUpdateModel.getEmail())
						.setParameter("mailingAddressParam", userInfoUpdateModel.getMailingAddress())
						.setParameter("idParam", id)
						.executeUpdate();
	}
	
	public void delete(int id) {
		
		String deleteStatement = "DELETE FROM UserInfo userInfo " + 
				 				 "WHERE  userInfo.id = :idParam";

		entityManager.createQuery(deleteStatement)
						.setParameter("idParam", id)
						.executeUpdate();
	}
	
}
