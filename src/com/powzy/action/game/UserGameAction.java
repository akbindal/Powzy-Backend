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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.googlecode.objectify.Key;
import com.powzy.game.GameLaunch;
import com.powzy.jsonmodel.GameLaunchInput;
import static com.powzy.OfyService.ofy;

/**
 * redirect the request to relevant game
 * @author ABindal
 *
 */
@Path("/usergame")
public class UserGameAction {
	
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
	@Produces(MediaType.APPLICATION_JSON)
	public String getGame(@PathParam("gameTypeId") Integer gameTypeId,
			@PathParam("gameAction") int gameAction, @Context UriInfo uriInfo) {
		//String baseUrl = _currentResponse.g
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		
		switch(gameTypeId) {
			case 1:
				// call the games
				return GymGameAction.manageGetRequest(gameAction, 
						queryParams);
			case 2:
				//url = "jkljklj ";
				break;
			default: 
			//	url = null;
				break;
		}
		return null;
	}
	
	/**
	 * company launching the game
	 * @param glInput
	 * @return
	 */
	@POST
	@Path("/launch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GameLaunchInput launchGame(GameLaunchInput glInput) {
		GameLaunch gl = GameLaunchInput.getGameLaunch(glInput);
		Key<GameLaunch> key = ofy().save().entity(gl).now();
		glInput.setId(key.getId());
		return glInput;
	}

	

}
