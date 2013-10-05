package com.powzy.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@NoArgsConstructor
@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3287758369869201388L;
	
	@Id
	@Getter @Setter
	Long type;
	
	@Getter @Setter
	String title;
}