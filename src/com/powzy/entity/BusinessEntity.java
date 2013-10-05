package com.powzy.entity;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Cache
@NoArgsConstructor
@XmlRootElement
public class BusinessEntity implements Serializable {
	
	public static Key<BusinessEntity> key(long id) {
		return Key.create(BusinessEntity.class, id);
	}
	
	public static BusinessEntity getNullEntity() {
		BusinessEntity entity = new BusinessEntity();
		entity.setId(-1l);
		entity.setEmail(null);
		return entity;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1006655893624124639L;
	
	@Id
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	String name;
	
	@Getter @Setter
	@Index String email;
	
	@Getter @Setter
	String businessDescription;
	
	@Getter @Setter
	String blobKey;
	
	@Getter @Setter
	ContactDetail contactDetail;
	
	@Getter @Setter
	Location location;
	
	@Getter @Setter
	@Load @Index 
	Set<Ref<Category>> categories = new HashSet<Ref<Category>>();
	
	
	@Getter @Setter
	@Index  Set<Key<Users>> lovers = new HashSet<Key<Users>>();
	
	@Getter @Setter
	@Ignore String password; //not persisted here ... just for easier json parsing
	
	@Getter @Setter
	String logoUrl;
	
	@Getter @Setter
	String imageUrl;
	
	public BusinessEntity(String email) {
		this.email = email;
	}
	
	//put users
	//get user ids
	public Collection<Users> getAllLovers()  {
		Map<Key<Users>, Users> lovers = 
				ofy().load().keys(this.lovers);
		return lovers.values();
	}
	
	
	public boolean setLover(Long userId, Boolean status) {
		Key<Users> loverKey = Key.create(Users.class, userId);
		if(status)this.lovers.add(loverKey);
		else this.lovers.remove(loverKey);
		return true;
	}
	
	
	/*
	// add image support
	 * 
	@Getter @Setter
	String blobKey;
	String uploadKey;
	*/
	//Embedded class
	@Getter @Setter 
	OpeningTime openingTime; 
	

}	
