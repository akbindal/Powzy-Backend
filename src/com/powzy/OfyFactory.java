package com.powzy;


import com.googlecode.objectify.ObjectifyFactory;
import com.powzy.entity.*;

public class OfyFactory extends ObjectifyFactory {
	public OfyFactory() {
		long time = System.currentTimeMillis();
		
		this.register(Users.class);
		this.register(BusinessEntity.class);
		this.register(UserEmailLookup.class);
		this.register(Category.class);
	//	this.register(ContactDetail.class);
		this.register(BusinessEntityLookup.class);
		//this.register(OpeningTime.class);
		long millis = System.currentTimeMillis() - time;
		//Log.info("Registration took "+millis+"millis");
	}
	
	@Override
	public Ofy begin() {
		return new Ofy(this);
	}
}
