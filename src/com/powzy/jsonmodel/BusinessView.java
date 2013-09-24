package com.powzy.jsonmodel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.Category;
import com.powzy.entity.ContactDetail;
import com.powzy.entity.Location;
import com.powzy.entity.OpeningTime;
import com.powzy.entity.Users;

import lombok.Getter;
import lombok.Setter;


@XmlRootElement
public class BusinessView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2731950046711659884L;

	@Getter @Setter
	Long id;
	
	@Getter @Setter
	String name;
	
	@Getter @Setter
	String email;
	
	@Getter @Setter
	String password;
	
	@Getter @Setter
	String businessDescription;
	
	@Getter @Setter
	Location location;
	
	@Getter @Setter
	Set<Long> categories;
	
	@Getter @Setter
	String isLove;
	
	@Getter @Setter
	ContactDetail contactDetail;
	
	@Getter @Setter
	OpeningTime openingTime;
	
	@Getter @Setter
	String logoUrl;
	
	@Getter @Setter
	String imageUrl;
	
	private BusinessView() {
		this.id=null; this.name=null; this.businessDescription=null;
		this.location=null; this.categories=null;
		this.isLove=null; this.contactDetail= null;
		this.email=null;
	}
	
	public static BusinessView getShortDescription(BusinessEntity entity, 
			Key<Users> userKey) {
		BusinessView view = new BusinessView();
		view.setId(entity.getId());
		view.setName(entity.getName());
		view.setLocation(entity.getLocation());
		view.setOpeningTime(entity.getOpeningTime());
		//if entity contains users key in lovers
		view.setLogoUrl(entity.getLogoUrl());
		Set<Long> catSet = new HashSet<Long>();
		for(Ref<Category> ref: entity.getCategories()) {
			catSet.add(ref.get().getType());
		}
		
		view.setCategories(catSet);
		if(entity.getLovers().contains(userKey))
			view.isLove = "true";
		else
			view.isLove = "false";
		return view;
	}
	
	public static BusinessView getLongDescription(BusinessEntity entity, 
			Long userId) {
		BusinessView view = new BusinessView();
		view.setId(entity.getId());
		view.setName(entity.getName());
		view.setBusinessDescription(entity.getBusinessDescription());
		view.setEmail(entity.getEmail());
		view.setLocation(entity.getLocation());
		view.setOpeningTime(entity.getOpeningTime());
		view.setLogoUrl(entity.getLogoUrl());
		view.setImageUrl(entity.getImageUrl());
		//
		Set<Long> catSet = new HashSet<Long>();
		for(Ref<Category> ref: entity.getCategories()) {
			catSet.add(ref.get().getType());
		}
		
		view.setCategories(catSet);
		
		//if entity contains users key in lovers
		Key<Users> key = Users.key(userId);
		if(entity.getLovers().contains(key))
			view.isLove = "true";
		else
			view.isLove = "false";
		return view;
	}
}
