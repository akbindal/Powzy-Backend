package com.powzy.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

public class JsonRequestException extends WebApplicationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1059486863930013834L;
	
	
    public JsonRequestException() {
        super(Response.status(Status.BAD_REQUEST).build());
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public JsonRequestException(String message) {
        super(Response.status(Status.BAD_REQUEST).entity(message).type("text/plain").build());
    }

}
