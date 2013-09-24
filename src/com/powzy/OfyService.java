package com.powzy;

import com.googlecode.objectify.ObjectifyService;

public class OfyService {
	static 
	{
		ObjectifyService.setFactory(new OfyFactory());
	}
	
	public static Ofy ofy() {
		return (Ofy)ObjectifyService.ofy();
	}
	
	public static OfyFactory factory() {
		return (OfyFactory)ObjectifyService.factory();
	}
}
