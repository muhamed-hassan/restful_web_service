package app.domain;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.persistence.entities.Customer;
import app.persistence.entities.IbanConfigs;
import app.persistence.entities.Page;
import app.persistence.repositories.CustomerRepository;
import app.persistence.repositories.IbanConfigsRepository;
import app.web.models.CustomerUpdateModel;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private IbanConfigsRepository ibanConfigsRepository;
	
	@Transactional
	public void openBankAccount(Customer customer) {
		
		customer.getBankAccountInfo().setBalance(0);
		
		Random random = new Random(System.currentTimeMillis());        
		long generatedNumber = Math.abs(random.nextLong());		        
		String generatedNumberString = generatedNumber + "";
		String accountNumber = generatedNumberString.substring(0, 8);
		customer.getBankAccountInfo().setAccountNumber(accountNumber);
		
		customerRepository.save(customer);
	}
	
	public Object[] getBriefViewForUpdateByNationalId(String nationalId) {
		
		Object[] rawRecord = customerRepository.findBriefViewForUpdateByNationalId(nationalId);
		
		return rawRecord;
	}
	
	public Object[] getDetailedViewByNationalId(String nationalId) {
		
		Object[] rawRecord = customerRepository.findDetailedViewByNationalId(nationalId);	
		
		return rawRecord;
	}
	
	public Page<Object[]> getPageOfCustomers(int pageIndex) {
				
		Page<Object[]> page = customerRepository.findPage(pageIndex);

		return page;
	}	

	@Transactional
	public void updateBankAccount(int id, CustomerUpdateModel customerUpdateModel) {
		
		customerRepository.update(id, customerUpdateModel);
	}
	
	@Transactional
	public void removeBankAccount(int id) {	
		
		customerRepository.delete(id);
	}
	
	public IbanConfigs getIbanConfigs() {
		
		IbanConfigs ibanConfigs = ibanConfigsRepository.findById(1);
		
		return ibanConfigs;
	}

}
