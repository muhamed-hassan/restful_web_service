package app.web.validators;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import app.web.error_handler.WebValidationException;
import app.web.models.CustomerCreateModel;
import app.web.models.CustomerUpdateModel;

@Component
public class CustomerValidator {
	
	private String nameField = "name";
	private String nameFieldIsRequiredMsg = "Name is required";
	private String nameFieldValidationRegex = "[a-zA-Z\\ ]{5,250}";
	private String nameFieldIsInvalidMsg = "Name contains invalid characters and shall contain letters only with maximum of 250";	
	
	private String nationalIdField = "nationalId";
	private String nationalIdFieldIsRequiredMsg = "National ID is required";
	private String nationalIdFieldValidationRegex = "[0-9]{14}";
	private String nationalIdFieldIsInvalidMsg = "National ID contains invalid characters and shall contain 14 digits only";
	
	private String dateOfBirthField = "dob";
	private String dateOfBirthFieldIsRequiredMsg = "Date of birth is required";
	private String dateOfBirthFieldValidationRegex = "((19[7-9][0-9])|(2[0-9][0-9][0-9]))-(0[1-9]|1[0-2])-(0[1-9]|([1-2][0-9])|3[0-1])";
	private String dateOfBirthFieldIsInvalidMsg = "Date of birth contains invalid characters and shall be in the form of yyyy-MM-dd";
	
	private String mobileField = "mobile";
	private String mobileFieldIsRequiredMsg = "Mobile is required";
	private String mobileFieldValidationRegex = "(07)[0-9]{9}";
	private String mobileFieldIsInvalidMsg = "Mobile contains invalid characters and shall be in this form 07123456789";
	
	private String emailField = "email";
	private String emailFieldIsRequiredMsg = "Email is required";
	private String emailFieldValidationRegex = "[a-zA-Z0-9_\\.]+(\\@)[a-zA-Z0-9]+(\\.)[a-zA-Z]+";
	private String emailFieldIsInvalidMsg = "Email format is invalid";
	
	private String mailingAddressField = "mailingAddress";
	private String mailingAddressFieldIsRequiredMsg = "Mailing address is required";
	private String mailingAddressFieldValidationRegex = "[a-zA-Z0-9-,\\ ]{15,250}";
	private String mailingAddressFieldIsInvalidMsg = "Mailing address contains invalid characters and shall contain alphanumeric characters only with maximum of 250";
	
	
	public void validateCustomerCreateModel(CustomerCreateModel customerCreateModel) {
		
		HashMap<String, String> errorInformation = new HashMap<String, String>();
		
		String name = customerCreateModel.getName();
		if (name == null || name.isEmpty()) {			
			errorInformation.put(nameField, nameFieldIsRequiredMsg);			
		} else {			
			name = name.trim();
			if (!name.matches(nameFieldValidationRegex)) {
				errorInformation.put(nameField, nameFieldIsInvalidMsg);
			}			
		}
		
		String nationalId = customerCreateModel.getNationalId();
		if (nationalId == null || nationalId.isEmpty()) {			
			errorInformation.put(nationalIdField, nationalIdFieldIsRequiredMsg);			
		} else {			
			nationalId = nationalId.trim();
			if (!nationalId.matches(nationalIdFieldValidationRegex)) {
				errorInformation.put(nationalIdField, nationalIdFieldIsInvalidMsg);
			}			
		}
		
		String dateOfBirth = customerCreateModel.getDateOfBirth();
		if (dateOfBirth == null || dateOfBirth.isEmpty()) {			
			errorInformation.put(dateOfBirthField, dateOfBirthFieldIsRequiredMsg);			
		} else {			
			dateOfBirth = dateOfBirth.trim();
			if (!dateOfBirth.matches(dateOfBirthFieldValidationRegex)) {
				errorInformation.put(dateOfBirthField, dateOfBirthFieldIsInvalidMsg);
			}			
		}
		
		String mobile = customerCreateModel.getMobile();
		if (mobile == null || mobile.isEmpty()) {			
			errorInformation.put(mobileField, mobileFieldIsRequiredMsg);			
		} else {			
			mobile = mobile.trim();
			if (!mobile.matches(mobileFieldValidationRegex)) {
				errorInformation.put(mobileField, mobileFieldIsInvalidMsg);
			}			
		}
		
		String email = customerCreateModel.getEmail();
		if (email == null || email.isEmpty()) {			
			errorInformation.put(emailField, emailFieldIsRequiredMsg);			
		} else {			
			email = email.trim();
			if (!email.matches(emailFieldValidationRegex)) {
				errorInformation.put(emailField, emailFieldIsInvalidMsg);
			}			
		}
		
		String mailingAddress = customerCreateModel.getMailingAddress();
		if (mailingAddress == null || mailingAddress.isEmpty()) {			
			errorInformation.put(mailingAddressField, mailingAddressFieldIsRequiredMsg);			
		} else {			
			mailingAddress = mailingAddress.trim();
			if (!mailingAddress.matches(mailingAddressFieldValidationRegex)) {
				errorInformation.put(mailingAddressField, mailingAddressFieldIsInvalidMsg);
			}			
		}
		
		if (!errorInformation.isEmpty()) {			
			WebValidationException webValidationException = new WebValidationException();
			webValidationException.setErrorInformation(errorInformation);
			throw webValidationException;
		}
	}
	
	public void validateNationalId(String nationalId) {
		
		HashMap<String, String> errorInformation = new HashMap<String, String>();
		
		if (nationalId.equals("0")) {			
			errorInformation.put(nationalIdField, nationalIdFieldIsRequiredMsg);			
		} else {			
			nationalId = nationalId.trim();
			if (!nationalId.matches(nationalIdFieldValidationRegex)) {
				errorInformation.put(nationalIdField, nationalIdFieldIsInvalidMsg);
			}			
		}
		
		if (!errorInformation.isEmpty()) {			
			WebValidationException webValidationException = new WebValidationException();
			webValidationException.setErrorInformation(errorInformation);
			throw webValidationException;
		}
	}
	
	public void validateCustomerUpdateModel(CustomerUpdateModel customerUpdateModel) {
		
		HashMap<String, String> errorInformation = new HashMap<String, String>();
		
		String mobile = customerUpdateModel.getMobile();
		if (mobile == null || mobile.isEmpty()) {			
			errorInformation.put(mobileField, mobileFieldIsRequiredMsg);			
		} else {			
			mobile = mobile.trim();
			if (!mobile.matches(mobileFieldValidationRegex)) {
				errorInformation.put(mobileField, mobileFieldIsInvalidMsg);
			}			
		}
		
		String email = customerUpdateModel.getEmail();
		if (email == null || email.isEmpty()) {			
			errorInformation.put(emailField, emailFieldIsRequiredMsg);			
		} else {			
			email = email.trim();
			if (!email.matches(emailFieldValidationRegex)) {
				errorInformation.put(emailField, emailFieldIsInvalidMsg);
			}			
		}
		
		String mailingAddress = customerUpdateModel.getMailingAddress();
		if (mailingAddress == null || mailingAddress.isEmpty()) {			
			errorInformation.put(mailingAddressField, mailingAddressFieldIsRequiredMsg);			
		} else {			
			mailingAddress = mailingAddress.trim();
			if (!mailingAddress.matches(mailingAddressFieldValidationRegex)) {
				errorInformation.put(mailingAddressField, mailingAddressFieldIsInvalidMsg);
			}			
		}
		
		if (!errorInformation.isEmpty()) {			
			WebValidationException webValidationException = new WebValidationException();
			webValidationException.setErrorInformation(errorInformation);
			throw webValidationException;
		}
	}
	
}
