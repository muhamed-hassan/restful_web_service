package com.poc.web.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.domain.UserService;
import com.poc.persistence.entities.IbanConfigs;
import com.poc.persistence.entities.MasterAccount;
import com.poc.persistence.entities.UserInfo;
import com.poc.web.models.BriefUserInfoReadModel;
import com.poc.web.models.DetailedUserInfoReadModel;
import com.poc.web.models.PageOfMasterAccounts;
import com.poc.web.models.UserInfoCreateModel;
import com.poc.web.models.UserInfoUpdateModel;
import com.poc.web.validators.Validator;

@RequestMapping("users")
@RestController
public class UserController {
	
	@Autowired
	private DateFormat dateFormat;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator; 
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createUserInfo(@RequestBody UserInfoCreateModel userInfoCreateModel) throws ParseException {
		
		validator.validate(userInfoCreateModel);	
		
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userInfoCreateModel.getName());
		userInfo.setNationalId(userInfoCreateModel.getNationalId());				
		userInfo.setDateOfBirth(dateFormat.parse(userInfoCreateModel.getDateOfBirth()));
		userInfo.setCellPhone(userInfoCreateModel.getCellPhone());
		userInfo.setEmail(userInfoCreateModel.getEmail());
		userInfo.setMailingAddress(userInfoCreateModel.getMailingAddress());
		
		userService.createUserInfo(userInfo);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{nationalId}")
	public ResponseEntity<DetailedUserInfoReadModel> getUserInfoByNationalId(@PathVariable String nationalId) {
			
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		MasterAccount masterAccount = userService.getUserInfoByNationalId(nationalId);
		
		DetailedUserInfoReadModel userInfoReadModel = new DetailedUserInfoReadModel();
		userInfoReadModel.setName(masterAccount.getUserInfo().getName());		
		String dateOfBirth = dateFormat.format(masterAccount.getUserInfo().getDateOfBirth());
		userInfoReadModel.setDateOfBirth(dateOfBirth);
		userInfoReadModel.setIban(toIban(ibanConfigs, masterAccount.getAccountNumber()));
		userInfoReadModel.setBalance(masterAccount.getBalance());
		userInfoReadModel.setCurrency(masterAccount.getCurrency().getCode());		
		userInfoReadModel.setNationalId(masterAccount.getUserInfo().getNationalId());
		userInfoReadModel.setCellPhone(masterAccount.getUserInfo().getCellPhone());
		userInfoReadModel.setEmail(masterAccount.getUserInfo().getEmail());
		userInfoReadModel.setMailingAddress(masterAccount.getUserInfo().getMailingAddress());
		
		return new ResponseEntity<DetailedUserInfoReadModel>(userInfoReadModel, HttpStatus.OK);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody UserInfoUpdateModel userInfoUpdateModel) {

		validator.validate(userInfoUpdateModel);
		
		userService.updateUserInfo(id, userInfoUpdateModel);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	public ResponseEntity<Object> removeUser(@PathVariable int id) {
		
		userService.removeUserInfo(id);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PageOfMasterAccounts> getPageOfUserInfo(@RequestParam int pageIndex) {
			
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		Page<MasterAccount> page = userService.getPageOfUserInfo(pageIndex);
		
		List<MasterAccount> content = page.getContent();
		HashSet<BriefUserInfoReadModel> userInfoReadModels = new HashSet<BriefUserInfoReadModel>();
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
		
		return new ResponseEntity<PageOfMasterAccounts>(pageOfMasterAccounts, HttpStatus.OK);
	}
	
	/* *************************************************************************************************** */
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}
	
}
