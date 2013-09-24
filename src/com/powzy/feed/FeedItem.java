package com.powzy.feed;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
@NoArgsConstructor
@XmlRootElement
public class FeedItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8641636087466551851L;
	
	@Id
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	Integer activityType;
}
