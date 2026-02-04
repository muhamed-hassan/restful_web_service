package com.poc.persistence.entities;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_info")
@Entity
public class UserInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
		
	private String name;
	
	@Column(name = "national_id")
	private String nationalId;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Embedded
	private ContactInfo contactInfo;
	
	@Embedded
	private BankAccountInfo bankAccountInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public BankAccountInfo getBankAccountInfo() {
		return bankAccountInfo;
	}

	public void setBankAccountInfo(BankAccountInfo bankAccountInfo) {
		this.bankAccountInfo = bankAccountInfo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		UserInfo other = (UserInfo) object;
		return id == other.getId();
	}
	
}
