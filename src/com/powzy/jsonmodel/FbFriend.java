package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
