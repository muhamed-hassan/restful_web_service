package app.web.controllers;

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

import app.domain.CustomerService;
import app.persistence.entities.BankAccountInfo;
import app.persistence.entities.ContactInfo;
import app.persistence.entities.Customer;
import app.persistence.entities.IbanConfigs;
import app.persistence.entities.Page;
import app.web.models.BriefCustomerReadModel;
import app.web.models.CustomerCreateModel;
import app.web.models.CustomerReadModelForUpdate;
import app.web.models.CustomerUpdateModel;
import app.web.models.DetailedCustomerReadModel;
import app.web.models.PageModel;
import app.web.validators.CustomerValidator;

@RequestMapping("customers")
@RestController
public class CustomerController {
		
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerValidator customerValidator; 
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> openBankAccount(@RequestBody CustomerCreateModel customerCreateModel) {
		
		customerValidator.validateCustomerCreateModel(customerCreateModel);	
		
		Customer customer = new Customer();
		customer.setName(customerCreateModel.getName());
		customer.setNationalId(customerCreateModel.getNationalId());
		try {			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			customer.setDateOfBirth(dateFormat.parse(customerCreateModel.getDateOfBirth()));			
		} catch (ParseException e) {}
		
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setCellPhone(customerCreateModel.getCellPhone());		
		contactInfo.setEmail(customerCreateModel.getEmail());
		contactInfo.setMailingAddress(customerCreateModel.getMailingAddress());
		customer.setContactInfo(contactInfo);
		
		BankAccountInfo bankAccountInfo = new BankAccountInfo();		
		customer.setBankAccountInfo(bankAccountInfo);
		
		customerService.openBankAccount(customer);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "brief-view-for-update/{nationalId}")
	public ResponseEntity<CustomerReadModelForUpdate> getBriefViewForUpdateByNationalId(@PathVariable String nationalId) {
			
		customerValidator.validateNationalId(nationalId);
		
		Object[] rawRecord = customerService.getBriefViewForUpdateByNationalId(nationalId);
		
        CustomerReadModelForUpdate customerReadModelForUpdate = new CustomerReadModelForUpdate();	
        customerReadModelForUpdate.setId((int) rawRecord[0]);
        customerReadModelForUpdate.setCellPhone((String) rawRecord[1]);
        customerReadModelForUpdate.setEmail((String) rawRecord[2]);
        customerReadModelForUpdate.setMailingAddress((String) rawRecord[3]);		
		
		return new ResponseEntity<CustomerReadModelForUpdate>(customerReadModelForUpdate, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "detailed-view/{nationalId}")
	public ResponseEntity<DetailedCustomerReadModel> getDetailedViewByNationalId(@PathVariable String nationalId) {
			
		customerValidator.validateNationalId(nationalId);
		
		IbanConfigs ibanConfigs = customerService.getIbanConfigs();
		Object[] rawRecord = customerService.getDetailedViewByNationalId(nationalId);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		DetailedCustomerReadModel detailedCustomerReadModel = new DetailedCustomerReadModel();
		detailedCustomerReadModel.setId((int) rawRecord[0]);
		detailedCustomerReadModel.setName((String) rawRecord[1]);		
		detailedCustomerReadModel.setNationalId((String) rawRecord[2]);
		detailedCustomerReadModel.setDateOfBirth(dateFormat.format((Date) rawRecord[3]));	
		detailedCustomerReadModel.setCellPhone((String) rawRecord[4]);
		detailedCustomerReadModel.setEmail((String) rawRecord[5]);
		detailedCustomerReadModel.setMailingAddress((String) rawRecord[6]);		
		detailedCustomerReadModel.setIban(toIban(ibanConfigs, (String) rawRecord[7]));
		detailedCustomerReadModel.setBalance((float) rawRecord[8]);
		
		return new ResponseEntity<DetailedCustomerReadModel>(detailedCustomerReadModel, HttpStatus.OK);
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PageModel<BriefCustomerReadModel>> getPageOfCustomers(@RequestParam int pageIndex) {
			
		IbanConfigs ibanConfigs = customerService.getIbanConfigs();
		Page page = customerService.getPageOfCustomers(pageIndex);
		
		List<Object[]> rawData = page.getData();
		HashSet<BriefCustomerReadModel> briefCustomerReadModels = new HashSet<BriefCustomerReadModel>();
		for (int cursor = 0; cursor < rawData.size(); cursor++) {
			
			Object[] currentRawRecord = rawData.get(cursor);
						
			BriefCustomerReadModel briefCustomerReadModel = new BriefCustomerReadModel();
			briefCustomerReadModel.setName((String) currentRawRecord[0]);
			briefCustomerReadModel.setNationalId((String) currentRawRecord[1]);
			briefCustomerReadModel.setIban(toIban(ibanConfigs, (String) currentRawRecord[2]));
			briefCustomerReadModel.setBalance((float) currentRawRecord[3]);
			
			briefCustomerReadModels.add(briefCustomerReadModel);
		}
		
		PageModel<BriefCustomerReadModel> pageOfCustomers = new PageModel<BriefCustomerReadModel>();
		pageOfCustomers.setData(briefCustomerReadModels);
		pageOfCustomers.setFirstPage(page.isFirstPage());
		pageOfCustomers.setLastPage(page.isLastPage());
		
		return new ResponseEntity<PageModel<BriefCustomerReadModel>>(pageOfCustomers, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<Object> updateBankAccount(@PathVariable int id, @RequestBody CustomerUpdateModel customerUpdateModel) {

		customerValidator.validateCustomerUpdateModel(customerUpdateModel);
		
		customerService.updateBankAccount(id, customerUpdateModel);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	public ResponseEntity<Object> removeBankAccount(@PathVariable int id) {
		
		customerService.removeBankAccount(id);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	/* *************************************************************************************************** */
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}
	
}
