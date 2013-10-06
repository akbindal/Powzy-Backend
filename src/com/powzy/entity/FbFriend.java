package com.powzy.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.annotation.Embed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embed
@XmlRootElement
@NoArgsConstructor
public class FbFriend implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 4541718779972625963L;
	
	@Getter @Setter
	String name;
	
	@Getter @Setter
	String id;
}
