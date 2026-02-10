package com.poc.domain;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.Page;
import com.poc.persistence.entities.UserInfo;
import com.poc.persistence.repositories.IbanConfigsRepository;
import com.poc.persistence.repositories.UserInfoRepository;
import com.poc.web.models.UserInfoUpdateModel;

@Service
public class UserService {

	@Autowired
	private IbanConfigsRepository ibanConfigsRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Transactional
	public void saveBankAccount(UserInfo userInfo) {
		
		userInfo.getBankAccountInfo().setBalance(0);
		
		Random random = new Random(System.currentTimeMillis());        
		long generatedNumber = Math.abs(random.nextLong());		        
		String generatedNumberString = generatedNumber + "";
		String accountNumber = generatedNumberString.substring(0, 8);
		userInfo.getBankAccountInfo().setAccountNumber(accountNumber);
		
		userInfoRepository.save(userInfo);
	}
	
	public Object[] getBriefViewOfUserInfoForUpdateByNationalId(String nationalId) {
		
		Object[] rawUserInfo = userInfoRepository.findBriefViewForUpdateByNationalId(nationalId);
		
		return rawUserInfo;
	}
	
	public Object[] getDetailedViewOfUserInfoByNationalId(String nationalId) {
		
		Object[] rawUserInfo = userInfoRepository.findDetailedViewByNationalId(nationalId);	
		
		return rawUserInfo;
	}
	
	public Page getPageOfBankAccounts(int pageIndex) {
				
		Page page = userInfoRepository.findPage(pageIndex);

		return page;
	}
	
	public IbanConfigs getIbanConfigs() {
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findById(1);
		
		return ibanConfigs;
	}

	@Transactional
	public void updateBankAccount(int id, UserInfoUpdateModel userInfoUpdateModel) {
		
		userInfoRepository.update(id, userInfoUpdateModel);
	}
	
	@Transactional
	public void removeBankAccount(int id) {	
		
		userInfoRepository.delete(id);
	}

}
