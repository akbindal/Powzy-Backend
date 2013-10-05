package com.powzy.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.googlecode.objectify.annotation.Embed;

import lombok.Getter;
import lombok.Setter;

@Embed
@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Location implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3970521879056383116L;

	/**
	 * 
	 */

	@Getter @Setter
	Double longitude;
	
	@Getter @Setter
	Double latitude;
}
