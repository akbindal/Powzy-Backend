package com.powzy.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

public class WrongActionException extends WebApplicationException {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Create a HTTP 401 (Unauthorized) exception.
    */
    public WrongActionException() {
        super(Response.status(Status.EXPECTATION_FAILED).build());
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public WrongActionException(String message) {
        super(Response.status(Status.EXPECTATION_FAILED).entity(message).type("text/plain").build());
    }

}
