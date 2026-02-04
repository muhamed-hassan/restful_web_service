package com.poc.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Random;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poc.domain.exceptions.DataNotFoundException;
import com.poc.persistence.entities.BankAccountInfo;
import com.poc.persistence.entities.ContactInfo;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.Page;
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
	
	@Autowired
	private DateFormat dateFormat;
	
	@Transactional
	public void createUserInfo(UserInfoCreateModel userInfoCreateModel) {
		
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userInfoCreateModel.getName());
		userInfo.setNationalId(userInfoCreateModel.getNationalId());
		
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setCellPhone(userInfoCreateModel.getCellPhone());		
		contactInfo.setEmail(userInfoCreateModel.getEmail());
		contactInfo.setMailingAddress(userInfoCreateModel.getMailingAddress());
		userInfo.setContactInfo(contactInfo);
		
		try {
			
			userInfo.setDateOfBirth(dateFormat.parse(userInfoCreateModel.getDateOfBirth()));
			
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
				
		BankAccountInfo bankAccountInfo = new BankAccountInfo();
		Random random = new Random(System.currentTimeMillis());        
		long generatedNumber = Math.abs(random.nextLong());		        
		String generatedNumberString = generatedNumber + "";
		String accountNumber = generatedNumberString.substring(0, 8);
		bankAccountInfo.setAccountNumber(accountNumber);
		bankAccountInfo.setBalance(0);
		userInfo.setBankAccountInfo(bankAccountInfo);
		
		userInfoRepository.save(userInfo);
	}
	
	public Object[] getBriefViewForUpdateOfUserInfoByNationalId(String nationalId) {
		
		Object[] rawUserInfo;
		try {
			
			rawUserInfo = userInfoRepository.findBriefViewForUpdateByNationalId(nationalId);
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}	
		
		return rawUserInfo;
	}
	
	public Object[] getDetailedViewOfUserInfoByNationalId(String nationalId) {
		
		Object[] rawUserInfo;
		try {
			
			rawUserInfo = userInfoRepository.findDetailedViewByNationalId(nationalId);
			
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}	
		
		return rawUserInfo;
	}
	
	public Page getPageOfUserInfo(int pageIndex) {
				
		Page page = userInfoRepository.findPage(pageIndex);
		
		if (page.getData().isEmpty()) {
			throw new DataNotFoundException();
		}

		return page;
	}
	
	public IbanConfigs getIbanConfigs() {
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findById(1);
		return ibanConfigs;
	}

	@Transactional
	public void updateUserInfo(int id, UserInfoUpdateModel userInfoUpdateModel) {
		
		userInfoRepository.update(id, userInfoUpdateModel);
	}
	
	@Transactional
	public void removeUserInfo(int id) {	
		
		userInfoRepository.delete(id);
	}

}
