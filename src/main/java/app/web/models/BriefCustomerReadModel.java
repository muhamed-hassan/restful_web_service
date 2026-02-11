package app.web.models;

import java.util.Objects;

public class BriefCustomerReadModel {
	
	private String name;

	private String nationalId;
	
	private String iban;
	
	private float balance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nationalId);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		BriefCustomerReadModel other = (BriefCustomerReadModel) object;
		return nationalId == other.getNationalId();
	}
	
}
