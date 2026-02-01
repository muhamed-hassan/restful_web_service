package com.poc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poc.persistence.entities.Currency;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.MasterAccount;
import com.poc.persistence.entities.UserInfo;
import com.poc.persistence.repositories.CurrencyRepository;
import com.poc.persistence.repositories.IbanConfigsRepository;
import com.poc.persistence.repositories.MasterAccountRepository;
import com.poc.persistence.repositories.UserInfoRepository;
import com.poc.web.error_handler.exceptions.DataNotFoundException;
import com.poc.web.models.BriefUserInfoReadModel;
import com.poc.web.models.PageOfMasterAccounts;
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
	
	@Transactional
	public void createUser(UserInfo userInfo) {
		
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
	
	public MasterAccount getUser(String nationalId) {
		
		MasterAccount masterAccount = masterAccountRepository.getByNationalId(nationalId);
		if (masterAccount == null) {
			throw new DataNotFoundException();
		}		
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findOne(1);
		masterAccount.setIban(toIban(ibanConfigs, masterAccount.getAccountNumber()));
		
		return masterAccount;
	}
	
	@Transactional
	public void updateUser(UserInfoUpdateModel userInfoUpdateModel) {
		
		UserInfo userInfo = userInfoRepository.findByNationalId(userInfoUpdateModel.getNationalId());
		
		userInfo.setCellPhone(userInfoUpdateModel.getCellPhone());
		userInfo.setEmail(userInfoUpdateModel.getEmail());
		userInfo.setMailingAddress(userInfoUpdateModel.getMailingAddress());
		
		userInfoRepository.save(userInfo);
	}
	
	@Transactional
	public void removeUser(String nationalId) {	
		
		MasterAccount masterAccount = masterAccountRepository.getByNationalId(nationalId);
		masterAccountRepository.delete(masterAccount);
	}
	
	public PageOfMasterAccounts getUsers(int pageIndex) {
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findOne(1);
		
		int pageSize = 10;
		Pageable pageRequest = new PageRequest(pageIndex, pageSize);
		Page<MasterAccount> page = masterAccountRepository.findAll(pageRequest);
		
		if (!page.hasContent()) {
			throw new DataNotFoundException();
		}
		
		List<MasterAccount> content = page.getContent();
		ArrayList<BriefUserInfoReadModel> userInfoReadModels = new ArrayList<BriefUserInfoReadModel>();
		for (int cursor = 0; cursor < content.size(); cursor++) {
			
			MasterAccount currentElement = content.get(cursor);
			
			currentElement.setIban(toIban(ibanConfigs, currentElement.getAccountNumber()));
			
			BriefUserInfoReadModel userInfoReadModel = new BriefUserInfoReadModel();
			userInfoReadModel.setId(currentElement.getId());
			userInfoReadModel.setName(currentElement.getUserInfo().getName());
			userInfoReadModel.setNationalId(currentElement.getUserInfo().getNationalId());
			userInfoReadModel.setIban(currentElement.getIban());
			userInfoReadModel.setBalance(currentElement.getBalance());
			
			userInfoReadModels.add(userInfoReadModel);
		}
		
		PageOfMasterAccounts pageOfMasterAccounts = new PageOfMasterAccounts();
		pageOfMasterAccounts.setData(userInfoReadModels);
		pageOfMasterAccounts.setTotalElements((int) page.getTotalElements());
		pageOfMasterAccounts.setTotalPages(page.getTotalPages());
		pageOfMasterAccounts.setFirstPage(page.isFirstPage());
		pageOfMasterAccounts.setLastPage(page.isLastPage());
		
		return pageOfMasterAccounts;
	}
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}

}
