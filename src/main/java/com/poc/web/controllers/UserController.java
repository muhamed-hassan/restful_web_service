package com.poc.web.controllers;

import java.text.DateFormat;
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
import com.poc.persistence.entities.UserInfo;
import com.poc.web.models.BriefUserInfoReadModel;
import com.poc.web.models.DetailedUserInfoReadModel;
import com.poc.web.models.PageOfUserInfo;
import com.poc.web.models.UserInfoCreateModel;
import com.poc.web.models.UserInfoUpdateModel;
import com.poc.web.validators.Validator;

@RequestMapping("users")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator; 
	
	@Autowired
	private DateFormat dateFormat;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createUserInfo(@RequestBody UserInfoCreateModel userInfoCreateModel) {
		
		validator.validate(userInfoCreateModel);	
		
		userService.createUserInfo(userInfoCreateModel);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{nationalId}")
	public ResponseEntity<DetailedUserInfoReadModel> getUserInfoByNationalId(@PathVariable String nationalId) {
			
		validator.validateNationalId(nationalId);
		
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		UserInfo userInfo = userService.getUserInfoByNationalId(nationalId);
		
		DetailedUserInfoReadModel userInfoReadModel = new DetailedUserInfoReadModel();
		userInfoReadModel.setName(userInfo.getName());		
		userInfoReadModel.setNationalId(userInfo.getNationalId());
		userInfoReadModel.setDateOfBirth(dateFormat.format(userInfo.getDateOfBirth()));
		userInfoReadModel.setCellPhone(userInfo.getCellPhone());
		userInfoReadModel.setEmail(userInfo.getEmail());
		userInfoReadModel.setMailingAddress(userInfo.getMailingAddress());
		userInfoReadModel.setIban(toIban(ibanConfigs, userInfo.getAccountNumber()));
		userInfoReadModel.setBalance(userInfo.getBalance());
		
		return new ResponseEntity<DetailedUserInfoReadModel>(userInfoReadModel, HttpStatus.OK);
	}
		
	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<Object> updateUserInfo(@PathVariable int id, @RequestBody UserInfoUpdateModel userInfoUpdateModel) {

		validator.validate(userInfoUpdateModel);
		
		userService.updateUserInfo(id, userInfoUpdateModel);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	public ResponseEntity<Object> removeUserInfo(@PathVariable int id) {
		
		userService.removeUserInfo(id);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PageOfUserInfo> getPageOfUserInfo(@RequestParam int pageIndex) {
			
		IbanConfigs ibanConfigs = userService.getIbanConfigs();
		Page<UserInfo> page = userService.getPageOfUserInfo(pageIndex);
		
		List<UserInfo> content = page.getContent();
		HashSet<BriefUserInfoReadModel> userInfoReadModels = new HashSet<BriefUserInfoReadModel>();
		for (int cursor = 0; cursor < content.size(); cursor++) {
			
			UserInfo currentElement = content.get(cursor);
						
			BriefUserInfoReadModel userInfoReadModel = new BriefUserInfoReadModel();
			userInfoReadModel.setId(currentElement.getId());
			userInfoReadModel.setName(currentElement.getName());
			userInfoReadModel.setNationalId(currentElement.getNationalId());
			userInfoReadModel.setIban(toIban(ibanConfigs, currentElement.getAccountNumber()));
			userInfoReadModel.setBalance(currentElement.getBalance());
			
			userInfoReadModels.add(userInfoReadModel);
		}
		
		PageOfUserInfo pageOfUserInfo = new PageOfUserInfo();
		pageOfUserInfo.setData(userInfoReadModels);
		pageOfUserInfo.setTotalElements((int) page.getTotalElements());
		pageOfUserInfo.setTotalPages(page.getTotalPages());
		pageOfUserInfo.setFirstPage(page.isFirstPage());
		pageOfUserInfo.setLastPage(page.isLastPage());
		
		return new ResponseEntity<PageOfUserInfo>(pageOfUserInfo, HttpStatus.OK);
	}
	
	/* *************************************************************************************************** */
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}
	
}
