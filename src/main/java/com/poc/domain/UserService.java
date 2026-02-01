package com.poc.domain;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poc.domain.exceptions.DataNotFoundException;
import com.poc.persistence.entities.Currency;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.MasterAccount;
import com.poc.persistence.entities.UserInfo;
import com.poc.persistence.repositories.CurrencyRepository;
import com.poc.persistence.repositories.IbanConfigsRepository;
import com.poc.persistence.repositories.MasterAccountRepository;
import com.poc.persistence.repositories.UserInfoRepository;
import com.poc.web.models.UserInfoUpdateModel;

@Service
public class UserService {

	@Autowired
	private IbanConfigsRepository ibanConfigsRepository;
	
	@Autowired
	private MasterAccountRepository masterAccountRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Value("${page.size}")
    private int pageSize;
	
	@Transactional
	public void createUserInfo(UserInfo userInfo) {
		
		userInfo = userInfoRepository.save(userInfo);
		
		Currency gbp = currencyRepository.findByCode("GBP");
		
		MasterAccount masterAccount = new MasterAccount();
		masterAccount.setUserInfo(userInfo);
		masterAccount.setBalance(0);
		masterAccount.setCurrency(gbp);
		
		Random random = new Random(System.currentTimeMillis());        
		long generatedNumber = Math.abs(random.nextLong());		        
		String generatedNumberString = generatedNumber + "";
		String accountNumber = generatedNumberString.substring(0, 8);
		masterAccount.setAccountNumber(accountNumber);
		
		masterAccountRepository.save(masterAccount);
	}
	
	public MasterAccount getUserInfoByNationalId(String nationalId) {
		
		MasterAccount masterAccount = masterAccountRepository.getByNationalId(nationalId);
		if (masterAccount == null) {
			throw new DataNotFoundException();
		}		
		
		return masterAccount;
	}
	
	@Transactional
	public void updateUserInfo(int id, UserInfoUpdateModel userInfoUpdateModel) {
		
		userInfoRepository.updateUserInfo(userInfoUpdateModel.getCellPhone(), userInfoUpdateModel.getEmail(), 
											userInfoUpdateModel.getMailingAddress(), id);
	}
	
	@Transactional
	public void removeUserInfo(int id) {	
		
		masterAccountRepository.delete(id);
	}
	
	public Page<MasterAccount> getPageOfUserInfo(int pageIndex) {
		
		Pageable pageRequest = new PageRequest(pageIndex, pageSize);
		Page<MasterAccount> page = masterAccountRepository.findAll(pageRequest);
		
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
