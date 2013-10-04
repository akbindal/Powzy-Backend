package com.powzy.action.game;

import static com.powzy.OfyService.ofy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.googlecode.objectify.Key;
import com.powzy.game.GameLaunch;
import com.powzy.jsonmodel.GameLaunchInput;

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
			@Context HttpServletResponse response) {
		//String baseUrl = _currentResponse.g
		//String url = getURL(httpRequest);
		response.addHeader("Access-Control-Allow-Origin", "*");
		//response.addHeader("Access-Control-Allow-Origin", "http://aplus1games.com, http:");
		response.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST");
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
			@PathParam("gameAction") int gameAction, @Context UriInfo uriInfo,
			@Context HttpServletResponse resp) {
		//String baseUrl = _currentResponse.g
		resp.addHeader("Access-Control-Allow-Origin", "*");
		//resp.addHeader("Access-Control-Allow-Origin", "http://aplus1games.com");
		resp.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST");
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		
		switch(gameTypeId) {
			case 1:
				// call the games
				return GymGameAction.manageGetRequest(gameAction, 
						queryParams);
			case 2:
				
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
