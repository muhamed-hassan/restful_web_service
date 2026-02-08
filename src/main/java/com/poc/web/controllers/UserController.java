package com.poc.web.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.domain.UserService;
import com.poc.persistence.entities.BankAccountInfo;
import com.poc.persistence.entities.ContactInfo;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.Page;
import com.poc.persistence.entities.UserInfo;
import com.poc.web.models.BriefUserInfoReadModel;
import com.poc.web.models.DetailedUserInfoReadModel;
import com.poc.web.models.PageOfBankAccounts;
import com.poc.web.models.UserInfoCreateModel;
import com.poc.web.models.UserInfoReadModelForUpdate;
import com.poc.web.models.UserInfoUpdateModel;
import com.poc.web.validators.Validator;

@RequestMapping("users")
@RestController
public class UserController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator; 
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> saveBankAccount(@RequestBody UserInfoCreateModel userInfoCreateModel) {
		
		validator.validateUserInfoCreateModel(userInfoCreateModel);	
		
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userInfoCreateModel.getName());
		userInfo.setNationalId(userInfoCreateModel.getNationalId());
		try {			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			userInfo.setDateOfBirth(dateFormat.parse(userInfoCreateModel.getDateOfBirth()));			
		} catch (ParseException e) {}
		
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setCellPhone(userInfoCreateModel.getCellPhone());		
		contactInfo.setEmail(userInfoCreateModel.getEmail());
		contactInfo.setMailingAddress(userInfoCreateModel.getMailingAddress());
		userInfo.setContactInfo(contactInfo);
		
		BankAccountInfo bankAccountInfo = new BankAccountInfo();		
		userInfo.setBankAccountInfo(bankAccountInfo);
		
		userService.saveBankAccount(userInfo);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "brief-view-for-update/{nationalId}")
	public ResponseEntity<UserInfoReadModelForUpdate> getBriefViewForUpdateByNationalId(@PathVariable String nationalId) {
			
		validator.validateNationalId(nationalId);
		
		Object[] rawUserInfo = userService.getBriefViewOfUserInfoForUpdateByNationalId(nationalId);
		
        UserInfoReadModelForUpdate userInfoReadModelForUpdate = new UserInfoReadModelForUpdate();	
        userInfoReadModelForUpdate.setId((int) rawUserInfo[0]);
        userInfoReadModelForUpdate.setCellPhone((String) rawUserInfo[1]);
        userInfoReadModelForUpdate.setEmail((String) rawUserInfo[2]);
        userInfoReadModelForUpdate.setMailingAddress((String) rawUserInfo[3]);		
		
		return new ResponseEntity<UserInfoReadModelForUpdate>(userInfoReadModelForUpdate, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "detailed-view/{nationalId}")
	public ResponseEntity<DetailedUserInfoReadModel> getDetailedViewByNationalId(@PathVariable String nationalId) {
			
		validator.validateNationalId(nationalId);
		
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		Object[] rawUserInfo = userService.getDetailedViewOfUserInfoByNationalId(nationalId);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		DetailedUserInfoReadModel userInfoReadModel = new DetailedUserInfoReadModel();
		userInfoReadModel.setId((int) rawUserInfo[0]);
		userInfoReadModel.setName((String) rawUserInfo[1]);		
		userInfoReadModel.setNationalId((String) rawUserInfo[2]);
		userInfoReadModel.setDateOfBirth(dateFormat.format((Date) rawUserInfo[3]));	
		userInfoReadModel.setCellPhone((String) rawUserInfo[4]);
		userInfoReadModel.setEmail((String) rawUserInfo[5]);
		userInfoReadModel.setMailingAddress((String) rawUserInfo[6]);		
		userInfoReadModel.setIban(toIban(ibanConfigs, (String) rawUserInfo[7]));
		userInfoReadModel.setBalance((float) rawUserInfo[8]);
		
		return new ResponseEntity<DetailedUserInfoReadModel>(userInfoReadModel, HttpStatus.OK);
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PageOfBankAccounts> getPageOfBankAccounts(@RequestParam int pageIndex) {
			
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		Page page = userService.getPageOfBankAccounts(pageIndex);
		
		List<Object[]> rawData = page.getData();
		HashSet<BriefUserInfoReadModel> userInfoReadModels = new HashSet<BriefUserInfoReadModel>();
		for (int cursor = 0; cursor < rawData.size(); cursor++) {
			
			Object[] currentElement = rawData.get(cursor);
						
			BriefUserInfoReadModel userInfoReadModel = new BriefUserInfoReadModel();
			userInfoReadModel.setName((String) currentElement[0]);
			userInfoReadModel.setNationalId((String) currentElement[1]);
			userInfoReadModel.setIban(toIban(ibanConfigs, (String) currentElement[2]));
			userInfoReadModel.setBalance((float) currentElement[3]);
			
			userInfoReadModels.add(userInfoReadModel);
		}
		
		PageOfBankAccounts pageOfUserInfo = new PageOfBankAccounts();
		pageOfUserInfo.setData(userInfoReadModels);
		pageOfUserInfo.setFirstPage(page.isFirstPage());
		pageOfUserInfo.setLastPage(page.isLastPage());
		
		return new ResponseEntity<PageOfBankAccounts>(pageOfUserInfo, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<Object> updateBankAccount(@PathVariable int id, @RequestBody UserInfoUpdateModel userInfoUpdateModel) {

		validator.validateUserInfoUpdateModel(userInfoUpdateModel);
		
		userService.updateBankAccount(id, userInfoUpdateModel);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	public ResponseEntity<Object> removeBankAccount(@PathVariable int id) {
		
		userService.removeBankAccount(id);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	/* *************************************************************************************************** */
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}
	
}
