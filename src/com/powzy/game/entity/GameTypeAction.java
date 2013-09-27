package com.powzy.game.entity;

import static com.powzy.OfyService.ofy;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.googlecode.objectify.Key;


@Path("/gameType")
public class GameTypeAction {
	@POST
	@Path("/put")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GameType putGameType(final GameType gameType) {
		GameType gt = new GameType();
		gt.title=gameType.getTitle();
		gt.id = gameType.getId();
		Key<GameType> key = ofy().save().entity(gt).now();
		return null;
	}
}
