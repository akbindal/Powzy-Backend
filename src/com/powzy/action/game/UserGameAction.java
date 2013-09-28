package com.powzy.action.game;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * redirect the request to relevant game
 * @author ABindal
 *
 */
@Path("/usergame")
public class UserGameAction {
	@Context
	HttpServletResponse _currentResponse;
	
	@POST
	@Path("/submit/{gameTypeId}/{gameAction}")
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public String putGame(String jsonString,
			@PathParam("gameTypeId") Integer gameTypeId,
			@PathParam("gameAction") int gameAction,
			@Context HttpServletRequest httpRequest) {
		//String baseUrl = _currentResponse.g
		//String url = getURL(httpRequest);
		switch(gameTypeId) {
			case 1:
				// call the games
				return GymGameAction.managePostRequest(jsonString, gameAction);
			case 2:
			//	url = "jkljklj ";
				
				break;
			default: 
		//		url = null;
				break;
		}
		return null;
	}
	
	@GET
	@Path("/get/{gameTypeId}/{gameAction}")
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public String getGame(String jsonString,
			@PathParam("gameTypeId") Integer gameTypeId,
			@PathParam("gameAction") int gameAction) {
		//String baseUrl = _currentResponse.g
		
		switch(gameTypeId) {
			case 1:
				// call the games
			//	return GymGameAction.manageGetRequest(jsonString, gameAction);
			case 2:
				//url = "jkljklj ";
				break;
			default: 
			//	url = null;
				break;
		}
		return null;
	}
	
	

}
