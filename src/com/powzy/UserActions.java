package com.powzy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//http://stackoverflow.com/questions/12166382/java-lang-incompatibleclasschangeerror-implementing-class-deploying-to-app-engi
//has to move datanucleus dependency from v2 to v1
@Path("/useraction")
public class UserActions {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJsonString() {
		return "{\"key\":\"value\"}";
	}
}
