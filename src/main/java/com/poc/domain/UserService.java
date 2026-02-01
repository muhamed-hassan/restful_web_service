package com.poc.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poc.domain.exceptions.DataNotFoundException;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.UserInfo;
import com.poc.persistence.repositories.IbanConfigsRepository;
import com.poc.persistence.repositories.UserInfoRepository;
import com.poc.web.models.UserInfoCreateModel;
import com.poc.web.models.UserInfoUpdateModel;

@Service
public class UserService {

	@Autowired
	private IbanConfigsRepository ibanConfigsRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Value("${page.size}")
    private int pageSize;
	
	@Autowired
	private DateFormat dateFormat;
	
	@Transactional
	public void createUserInfo(UserInfoCreateModel userInfoCreateModel) {
		
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userInfoCreateModel.getName());
		userInfo.setNationalId(userInfoCreateModel.getNationalId());						
		userInfo.setCellPhone(userInfoCreateModel.getCellPhone());
		userInfo.setEmail(userInfoCreateModel.getEmail());
		userInfo.setMailingAddress(userInfoCreateModel.getMailingAddress());
		userInfo.setBalance(0);
		
		try {
			userInfo.setDateOfBirth(dateFormat.parse(userInfoCreateModel.getDateOfBirth()));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
				
		Random random = new Random(System.currentTimeMillis());        
		long generatedNumber = Math.abs(random.nextLong());		        
		String generatedNumberString = generatedNumber + "";
		String accountNumber = generatedNumberString.substring(0, 8);
		userInfo.setAccountNumber(accountNumber);
		
		userInfoRepository.save(userInfo);
	}
	
	public UserInfo getUserInfoByNationalId(String nationalId) {
		
		UserInfo userInfo = userInfoRepository.findByNationalId(nationalId);
		if (userInfo == null) {
			throw new DataNotFoundException();
		}		
		
		return userInfo;
	}
	
	@Transactional
	public void updateUserInfo(int id, UserInfoUpdateModel userInfoUpdateModel) {
		
		userInfoRepository.updateUserInfo(userInfoUpdateModel.getCellPhone(), userInfoUpdateModel.getEmail(), 
											userInfoUpdateModel.getMailingAddress(), id);
	}
	
	@Transactional
	public void removeUserInfo(int id) {	
		
		userInfoRepository.delete(id);
	}
	
	public Page<UserInfo> getPageOfUserInfo(int pageIndex) {
		
		Pageable pageRequest = new PageRequest(pageIndex, pageSize);
		Page<UserInfo> page = userInfoRepository.findAll(pageRequest);
		
		if (!page.hasContent()) {
			throw new DataNotFoundException();
		}

		return page;
	}
	
	public IbanConfigs getIbanConfigs() {
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findOne(1);
		return ibanConfigs;
	}

}
