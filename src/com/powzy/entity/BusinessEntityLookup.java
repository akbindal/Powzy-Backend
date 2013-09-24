package com.powzy.entity;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
@NoArgsConstructor
public class BusinessEntityLookup  {
	/***normal version***/
	@Id
	@Getter @Setter
	String normal;
	
	/** Use this method to normalize email addresses for lookup */
	public static String normalize(String email) {
		return email.toLowerCase();
	}
	
	/***pretty version***/
	@Getter @Setter
	String email;
	
	@Getter @Setter
	String password;
	
	@Getter @Setter
	Long created;
	
	
	@Index
	@Load
	Ref<BusinessEntity> entity;
	/****/
	
	/** */
	public BusinessEntityLookup(String email, String password,
			BusinessEntity entity) {
		this.setEmail(email);
		this.setCreated(new Date().getTime());
		this.password = password; 
		this.normal = normalize(email);
		this.entity = Ref.create(entity);
	}
	
	public BusinessEntity getEntity() {
		return entity.get();
	}
}
