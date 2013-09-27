package com.powzy.action.game;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import com.powzy.entity.Users;
import com.powzy.game.GameLaunch;
import com.powzy.game.Level;
import com.powzy.game.UserGame;
import com.powzy.game.param.GameParam;
import com.powzy.game.param.GymGameParam;
import com.powzy.game.status.GameStatus;
import com.powzy.game.status.GymGameStatus;
import com.powzy.game.status.GymUserStatus;
import com.powzy.game.status.UserStatus;
import com.powzy.jsonmodel.GameLaunchInput;
import com.powzy.jsonmodel.PowzyGameInitiate;
import com.powzy.jsonmodel.UserGameInitiate;
import com.powzy.util.UnauthorizedException;

import static com.powzy.OfyService.ofy;

@Path("/gameplay")
public class GamePlay {
	
	/**
	 * company launching the game
	 * @param glInput
	 * @return
	 */
	@POST
	@Path("/put")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GameLaunchInput launchGame(GameLaunchInput glInput) {
		GameLaunch gl = GameLaunchInput.getGameLaunch(glInput);
		Key<GameLaunch> key = ofy().save().entity(gl).now();
		glInput.setId(key.getId());
		return glInput;
	}
	
	/**
	 * user launch the game
	 * if it has not played the game before ?
	 * @param ugi
	 * @return
	 */
	@POST
	@Path("/submit/{gameTypeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserStatus submitGame(String userGameJson, 
			@PathParam("gameTypeId") Integer gameTypeId) {
		
		ObjectMapper mapper = new ObjectMapper();
		//create the GameLaunch
		//get the game Launch Ref to refer it after and add the user as participants
		Long userId = null;
		Long gameLaunchId = null;
		
		switch(gameTypeId) {
			case 1:
			{
				//convert Powzy game Initiate
				PowzyGameInitiate pgi ;
				try {
					pgi=mapper.readValue(userGameJson, PowzyGameInitiate.class);
				} catch (Exception e) {
					throw new UnauthorizedException();
				}
				userId = pgi.getUserId();
				gameLaunchId = pgi.getGameLaunchId();
				
				UserGame ug = new UserGame();
				ug.setUser(Users.key(pgi.getUserId()));
				Key<GameLaunch> glKey = GameLaunch.key(pgi.getGameLaunchId());
				ug.setGameLaunch(glKey);
				
				GameParam gp=null;
				//parse parameter of gym game
				gp = PowzyGameInitiate.convertToParam((PowzyGameInitiate)pgi);
				if(gp==null) return null;
				Level newLevel = new Level();
				List<Level> levelList = new ArrayList<Level>();
				levelList.add(newLevel);
				Key<GameParam> gameParamKey = ofy().save().entity(gp).now();
				newLevel.setUserGameParam(Ref.create(gameParamKey));
				GymGameStatus gymStatus = new GymGameStatus(false);
				Key<GymGameStatus> key = ofy().save().entity(gymStatus).now();
				//ref to gameStatus
				newLevel.setUserGameStatus(Ref.create(Key.create(GameStatus.class, key.getId())));
				newLevel.setLastProgress(0.00);
				ug.setLevel(levelList);
				//save usergame
				GymUserStatus status = new GymUserStatus();
				status.setLastLevel(1);
				status.setLeftWorkout(7);
				status.setTotalEarning(0);
				status.setTotalProgress(0.00);
				
				Key<GymUserStatus> gymKey = ofy().save().entity(status).now();
				
				ug.setUserStatus(
						Ref.create(Key.create(UserStatus.class, gymKey.getId()))
						);
				
				ofy().save().entity(ug).now();
				//get Game alunch and add user
				Ref<Users> user = Ref.create(Key.create(Users.class, userId));
				Ref<GameLaunch> glRef = Ref.create(GameLaunch.key(gameLaunchId));
				GameLaunch gl = glRef.get();
				gl.getParticipants().add(user);
				ofy().save().entity(gl).now();
				//return user status
				return ug.getUserStatus().get();
			}
			case 2:
				break;
			default:
		}
		return null;
	}
	
}
