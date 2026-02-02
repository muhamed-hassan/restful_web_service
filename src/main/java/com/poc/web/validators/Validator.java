package com.poc.web.validators;

import org.springframework.stereotype.Component;

import com.poc.web.models.UserInfoCreateModel;
import com.poc.web.models.UserInfoUpdateModel;

//https://en.wikipedia.org/wiki/Fail-fast approach is used to report validation errors
@Component
public class Validator {
		
	public void validateUserInfoCreateModel(UserInfoCreateModel userInfoCreateModel) {
		
		String name = userInfoCreateModel.getName();
		if (name == null) {
			throw new IllegalArgumentException("name is required");
		}
		name = name.trim();
		if (!name.matches("[a-zA-Z\\ ]{5,250}")) {
			throw new IllegalArgumentException("name contains invalid characters and shall contain letters only with maximum of 250");
		}
		
		validateNationalId(userInfoCreateModel.getNationalId());
		
		String dateOfBirthStr = userInfoCreateModel.getDateOfBirth();
		if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) {
			throw new IllegalArgumentException("dateOfBirth is required");
		}	
		
		validateCellPhone(userInfoCreateModel.getCellPhone());
		
		validateEmail(userInfoCreateModel.getEmail());
		
		validateMailingAddress(userInfoCreateModel.getMailingAddress());
	}	
	
	public void validateUserInfoUpdateModel(UserInfoUpdateModel userInfoUpdateModel) {
		
		validateCellPhone(userInfoUpdateModel.getCellPhone());
		
		validateEmail(userInfoUpdateModel.getEmail());
		
		validateMailingAddress(userInfoUpdateModel.getMailingAddress());
	}
	
	public void validateNationalId(String nationalId) {
		
		if (nationalId == null) {
			throw new IllegalArgumentException("nationalId is required");
		}
		nationalId = nationalId.trim();
		if (!nationalId.matches("[0-9]{14}")) {
			throw new IllegalArgumentException("nationalId contains invalid characters and shall contain 14 digits only");
		}
	}
	
	/* *************************************************************************************************** */
	/* *************************************************************************************************** */
	
	private void validateCellPhone(String cellPhone) {
		
		if (cellPhone == null) {
			throw new IllegalArgumentException("cellPhone is required");
		}
		cellPhone = cellPhone.trim();
		if (!cellPhone.matches("(07)[0-9]{9}")) {
			throw new IllegalArgumentException("cellPhone contains invalid characters and shall be in this form 07123456789");
		}
	}
	
	private void validateEmail(String email) {
		
		if (email == null) {
			throw new IllegalArgumentException("email is required");
		}
		email = email.trim();
		if (!email.matches("[a-zA-Z0-9_\\.]+(\\@)[a-zA-Z0-9]+(\\.)[a-zA-Z]+")) {
			throw new IllegalArgumentException("email format is invalid");
		}
	}
	
	private void validateMailingAddress(String mailingAddress) {
		
		if (mailingAddress == null) {
			throw new IllegalArgumentException("mailingAddress is required");
		}
		mailingAddress = mailingAddress.trim();
		if (!mailingAddress.matches("[a-zA-Z0-9-,\\ ]{15,250}")) {
			throw new IllegalArgumentException("mailingAddress contains invalid characters and shall contain alphanumeric characters only with maximum of 250");
		}
	}
	
}
