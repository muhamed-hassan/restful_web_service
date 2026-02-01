package com.poc.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poc.persistence.entities.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
	
	public UserInfo findByNationalId(String nationalId);
	
	@Modifying
    @Query("UPDATE UserInfo userInfo "
    		+ "SET userInfo.cellPhone = :cellPhoneParam, userInfo.email = :emailParam, userInfo.mailingAddress = :mailingAddressParam "
    		+ "WHERE userInfo.id = :idParam")
	public int updateUserInfo(@Param("cellPhoneParam") String cellPhoneParam, @Param("emailParam") String emailParam,
								@Param("mailingAddressParam") String mailingAddressParam, @Param("idParam") int idParam);
	
}
