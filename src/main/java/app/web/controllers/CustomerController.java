package app.web.controllers;

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
import app.persistence.entities.Customer;
import app.persistence.entities.Page;
import app.web.models.BriefCustomerReadModel;
import app.web.models.CustomerCreateModel;
import app.web.models.CustomerReadModelForUpdate;
import app.web.models.CustomerUpdateModel;
import app.web.models.DetailedCustomerReadModel;
import app.web.models.PageModel;
import app.web.transformers.CustomerTransformer;
import app.web.validators.CustomerValidator;

@RequestMapping("customers")
@RestController
public class CustomerController {
		
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerValidator customerValidator; 
	
	@Autowired
	private CustomerTransformer customerTransformer;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> openBankAccount(@RequestBody CustomerCreateModel customerCreateModel) {
		
		customerValidator.validateCustomerCreateModel(customerCreateModel);	
		
		Customer customer = customerTransformer.toEntity(customerCreateModel);
		
		customerService.openBankAccount(customer);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "brief-view-for-update/{nationalId}")
	public ResponseEntity<CustomerReadModelForUpdate> getBriefViewForUpdateByNationalId(@PathVariable String nationalId) {
			
		customerValidator.validateNationalId(nationalId);
		
		Object[] rawRecord = customerService.getBriefViewForUpdateByNationalId(nationalId);
		
        CustomerReadModelForUpdate customerReadModelForUpdate = customerTransformer.toCustomerReadModelForUpdate(rawRecord);
        
		return new ResponseEntity<CustomerReadModelForUpdate>(customerReadModelForUpdate, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "detailed-view/{nationalId}")
	public ResponseEntity<DetailedCustomerReadModel> getDetailedViewByNationalId(@PathVariable String nationalId) {
			
		customerValidator.validateNationalId(nationalId);
				
		Object[] rawRecord = customerService.getDetailedViewByNationalId(nationalId);
				
		DetailedCustomerReadModel detailedCustomerReadModel = customerTransformer.toDetailedCustomerReadModel(rawRecord);
		
		return new ResponseEntity<DetailedCustomerReadModel>(detailedCustomerReadModel, HttpStatus.OK);
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PageModel<BriefCustomerReadModel>> getPageOfCustomers(@RequestParam int pageIndex) {
			
		Page<Object[]> page = customerService.getPageOfCustomers(pageIndex);
		
		PageModel<BriefCustomerReadModel> pageOfCustomers = customerTransformer.toPageModel(page);
		
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
	
}
