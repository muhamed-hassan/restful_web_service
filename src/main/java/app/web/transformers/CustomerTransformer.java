package app.web.transformers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.domain.CustomerService;
import app.persistence.entities.BankAccountInfo;
import app.persistence.entities.ContactInfo;
import app.persistence.entities.Customer;
import app.persistence.entities.IbanConfigs;
import app.persistence.entities.Page;
import app.web.models.BriefCustomerReadModel;
import app.web.models.CustomerCreateModel;
import app.web.models.CustomerReadModelForUpdate;
import app.web.models.DetailedCustomerReadModel;
import app.web.models.PageModel;

@Component
public class CustomerTransformer {
	
	@Autowired
	private CustomerService customerService;
	
	public Customer toEntity(CustomerCreateModel customerCreateModel) {
		
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
		
		return customer;
	}
	
	public CustomerReadModelForUpdate toCustomerReadModelForUpdate(Object[] rawRecord) {
		
		CustomerReadModelForUpdate customerReadModelForUpdate = new CustomerReadModelForUpdate();	
        customerReadModelForUpdate.setId((int) rawRecord[0]);
        customerReadModelForUpdate.setCellPhone((String) rawRecord[1]);
        customerReadModelForUpdate.setEmail((String) rawRecord[2]);
        customerReadModelForUpdate.setMailingAddress((String) rawRecord[3]);
        
		return customerReadModelForUpdate;
	}
	
	public DetailedCustomerReadModel toDetailedCustomerReadModel(Object[] rawRecord) {
		
		IbanConfigs ibanConfigs = customerService.getIbanConfigs();
		
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
		
		return detailedCustomerReadModel;
	}
	
	public PageModel<BriefCustomerReadModel> toPageModel(Page page) {
		
		IbanConfigs ibanConfigs = customerService.getIbanConfigs();
		
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
		
		return pageOfCustomers;
	}

	/* *************************************************************************************************** */
	
	private String toIban(IbanConfigs ibanConfigs, String accountNumber) {
		
		return ibanConfigs.getCountryCode() + ibanConfigs.getCheckDigits() + ibanConfigs.getBankCode() + 
				ibanConfigs.getSortCode() + accountNumber;
	}
	
}
