package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NullResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7207636344791517876L;
	
	String response;
}
