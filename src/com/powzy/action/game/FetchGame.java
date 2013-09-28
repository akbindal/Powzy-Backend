package com.powzy.action.game;

import static com.powzy.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.Users;
import com.powzy.game.GameLaunch;
import com.powzy.game.UserGame;
import com.powzy.game.entity.GameType;
import com.powzy.game.status.UserStatus;
import com.powzy.jsonmodel.GameBrandRow;


@Path("/games")
public class FetchGame {
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GameBrandRow> fetchGameByBrand(@QueryParam("uid")Long userId, 
			@QueryParam("bid") Long businessId)  {
		Key<BusinessEntity> owner = 
				Key.create(BusinessEntity.class, businessId);
		/*
		Ref<BusinessEntity> owner = 
			Ref.create(Key.create(BusinessEntity.class, businessId));
		*/
		//get GameLaunches by Bid
		List<GameLaunch> gameLaunchList = 
				ofy().load().type(GameLaunch.class)
				.filter("owner =", owner).list();
		
		if(gameLaunchList==null)
			return new ArrayList<GameBrandRow>();
		
		List<GameBrandRow> gameRow = new ArrayList<GameBrandRow>();
		
		//get gameStatus by Uid
		for(GameLaunch gl: gameLaunchList) {
			GameBrandRow gRow = new GameBrandRow();
			Ref<Users> user = 
					Ref.create(Key.create(Users.class, userId));
			//get all the filterfor gameBrandRow
			GameType gt = gl.getGameType().get();
			gRow.setGameTypeId(gt.getId());
			gRow.setGameLaunchId(gl.getId());
			gRow.setShortDesc(gl.getShortDesc());
			gRow.setGameTitle(gt.getTitle());
			gRow.setGameUrl(gl.getGameLogoUrl());
			gRow.setUserGameId(-1l);
			if(gl.getParticipants().contains(user)) {
				Key<GameLaunch> parent = Key.create(GameLaunch.class, gl.getId());
				UserGame ug = ofy().load().type(UserGame.class).ancestor(parent).
						filter("user =", user).first().now();
				if(ug==null) {
					gameRow.add(gRow);
					continue;
				}
				
				UserStatus uStatus = ofy().load()
						.type(UserStatus.class)
						.id(ug.getUserStatus()
								.getId()).now();
				gRow.setLastLevel(uStatus.getLastLevel());
				gRow.setProgress(uStatus.getTotalProgress());
				gRow.setUserGameId(ug.getUserGameId());
			} 
			gameRow.add(gRow);
		}
		
		return gameRow;
	}
}
