package com.powzy.action.game;

import static com.powzy.OfyService.ofy;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.map.ObjectMapper;

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
import com.powzy.jsonmodel.BoolResponse;
import com.powzy.jsonmodel.GymCheckin;
import com.powzy.jsonmodel.GymStatusModel;
import com.powzy.jsonmodel.LevelStatusResponse;
import com.powzy.jsonmodel.PowzyGameInitiate;
import com.powzy.util.UnauthorizedException;

public class GymGameAction {
	public static final int gameAction = 0;
	public static final int POST_LEVEL = 1;
	public static final int POST_CHECKIN = 2;
	public static final int POST_START_LEVEL = 3;
	
	public static final int GET_FETCH_LEVEL = 5;
	public static final int GET_CURRENT_STATUS = 6;
	
	public static String managePostRequest(String jsonString, int gameAction) {
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
		switch(gameAction) {
			
			case POST_CHECKIN:
				result = gymPostCheckin(jsonString, mapper);
				break;
				
			case POST_START_LEVEL:
				result = gymStartLevel(jsonString, mapper);
				break;
		}
		return result;
	}
	
	public static String manageGetRequest(int gameAction, 
			MultivaluedMap<String, String> queryParams) {
		switch(gameAction) {
			case GET_FETCH_LEVEL:
				return fetchGymStatus(queryParams);
				
			case GET_CURRENT_STATUS:
				break;
		}
		return null;
	}
//	
//	public static String gymPostCheckin(String jsonstring, ObjectMapper mapper) {
//		
//	}
	public static String fetchGymStatus(MultivaluedMap<String, String> queryParams) {
		String usergameIdStr = queryParams.getFirst("userGameId");
		String levelStr = queryParams.getFirst("level");
		//String userIdStr = queryParams.getFirst("userId");
		String gameLaunchIdStr = queryParams.getFirst("gameLaunchId");
		try {
			Long userGameId = Long.parseLong(usergameIdStr);
			Integer levelId = Integer.parseInt(levelStr);
			//Long userId = Long.parseLong(userIdStr);
			Long gameLaunchId = Long.parseLong(gameLaunchIdStr);
			
			
			Key<GameLaunch> parent = Key.create(GameLaunch.class, gameLaunchId);
			UserGame ug = ofy().load().type(UserGame.class).parent(parent).id(userGameId).now();
			if(ug==null) return null;
			
			if(levelId==null)
				levelId = -1;
			GymStatusModel resp = new GymStatusModel();
			//get userstatus
			Key<UserStatus> userstatus = ug.getUserStatus();
			GymUserStatus gus = ofy().load().type(GymUserStatus.class).id(userstatus.getId()).now();
			resp.setLastPacts(ug.getLevel().size());
			resp.setTotalEarning(gus.getTotalEarning());
			LevelStatusResponse levelResp = new LevelStatusResponse();
			Level level;
			
			if(levelId<0) {
				levelId = ug.getLevel().size()-1;
			} 
			
			level = ug.getLevel().get(levelId);
			
			Key<GameParam> gpKey = level.getUserGameParam();
			GymGameParam gp = (GymGameParam) ofy().load().key(gpKey).now();
			
			Key<GameStatus> gusKey = level.getUserGameStatus();
			GymGameStatus ggs = (GymGameStatus) ofy().load().key(gusKey).now();
			
			levelResp.setWorkOutDays(ggs.getWorkOutDays());
			levelResp.setIsCompleted(ggs.isCompleted()?1:0);
			levelResp.setCommittedWorkDays(gp.getCommitedWorkOutdays());
			levelResp.setEarning(gp.getWager());
			levelResp.setLevel(levelId);
			resp.setLevel(levelResp);
			ByteArrayOutputStream sos = new ByteArrayOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(sos, resp);
			} catch (Exception e) {
				throw new UnauthorizedException("object parser error");
			}
			return sos.toString();
		} catch (Exception e) {
			return "wrong query parameters";
		}
		
	}
	
	public static String gymPostCheckin(String jsonString, ObjectMapper mapper) {
		GymCheckin req;
		try {
			req = mapper.readValue(jsonString, GymCheckin.class);
		} catch(Exception e) {
			throw new UnauthorizedException();
		}
		
		Long userGameId = req.getUserGameId();
		Long checkInTime = req.getCurrentDate();
		
		//get the last level
		Key<GameLaunch> parent = Key.create(GameLaunch.class, req.getGameLaunchId());
		UserGame ug = ofy().load().type(UserGame.class).parent(parent).id(userGameId).now();
		int levelSize = ug.getLevel().size();
		Level lastLevel = ug.getLevel().get(levelSize-1);
		Key<GameParam> key = lastLevel.getUserGameParam();
		GymGameParam param = ofy().load().type(GymGameParam.class).id(key.getId()).now();
		Date startDate = param.getStartDate();
		Date currentDate = new Date(checkInTime);
		String result=null;
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		Long timeDiff = currentDate.getTime() - startDate.getTime() ;
		int day = (int) (timeDiff/(24*60*60*1000));
		if(day<0 || day > 6) {
			BoolResponse resp = new BoolResponse(false);
			try {
				mapper.writeValue(sos, resp);
			} catch (Exception e) {
				throw new UnauthorizedException("object parser error");
			}
			return sos.toString();
		}
		//if(currentDate.getTime())
		//no need to check in if not promised
		Integer[] commitedWorkoutdays =   param.getCommitedWorkOutdays();
		//total committed dasy
		int totalComited = 0;
		for(int i =0 ;i< commitedWorkoutdays.length; i++) {
			if(commitedWorkoutdays[i]==1)
				totalComited++;
		}
		
		if(commitedWorkoutdays[day]==0) {
			BoolResponse resp = new BoolResponse(false);
			try {
				mapper.writeValue(sos, resp);
			} catch (Exception e) {
				throw new UnauthorizedException("object parser error");
			}
			return sos.toString();
		}
		
		//get Game status
		Key<GameStatus> gsKey = lastLevel.getUserGameStatus();
		GymGameStatus ggs = ofy().load().type(GymGameStatus.class).id(gsKey.getId()).get();
		Integer[] workeddays = ggs.getWorkOutDays();
		workeddays[day]=1;
		ggs.setWorkOutDays(workeddays);
		
		int countWorkCompleted=0;
		for(int i = 0; i< 7 ;i++) {
			if(workeddays[i]==null)
				continue;
			if(workeddays[i]==1)
				countWorkCompleted++;
		}
		int countWorkLeft = totalComited - countWorkCompleted;
		// gymUserStatus 
		Key<UserStatus> gusKey = ug.getUserStatus();
		GymUserStatus gus = ofy().load().type(GymUserStatus.class).id(gusKey.getId()).now();
		
		// countworkLeft = 1
		if(countWorkLeft ==0) {
			//game is completed;
			ggs.setEarning(param.getWager());
			ggs.setCompleted(true);
			lastLevel.setLastProgress(100.00);
			int totalwager = param.getWager()+gus.getTotalEarning();
			gus.setTotalEarning(totalwager);
			
			//
		} else {
			
			//game is incomplete;
		}
		//save ggs
		ofy().save().entity(ggs);
		
		//
		//total commited days;
		double totalProgress = ((double)countWorkCompleted/totalComited)*100;
		lastLevel.setLastProgress(totalProgress);
		gus.setTotalProgress(totalProgress);
		
		
		//save user status
		ofy().save().entity(gus);
		//save user gameStatus
		ofy().save().entity(ug).now();
		
		BoolResponse resp = new BoolResponse(true);
		try {
			mapper.writeValue(sos, resp);
		} catch (Exception e) {
			throw new UnauthorizedException("object parser error");
		}
		return sos.toString();
	}
	
	public static String gymStartLevel(String jsonString, ObjectMapper mapper) {
		//convert Powzy game Initiate
		PowzyGameInitiate pgi ;
		try {
			pgi=mapper.readValue(jsonString, PowzyGameInitiate.class);
		} catch (Exception e) {
			throw new UnauthorizedException();
		}
		Long userGameId = pgi.getUserGameId();
		//get the user game
		//get the last level
		Key<GameLaunch> parent = Key.create(GameLaunch.class, pgi.getGameLaunchId());
		UserGame ug = ofy().load().type(UserGame.class).parent(parent).id(userGameId).now();
		if(ug==null) {
			Long userId = null;
			Long gameLaunchId = null;
			
			userId = pgi.getUserId();
			gameLaunchId = pgi.getGameLaunchId();
			
			ug = new UserGame();
			ug.setUser(Users.key(pgi.getUserId()));
			Key<GameLaunch> glKey = GameLaunch.key(pgi.getGameLaunchId());
			ug.setGameLaunch(glKey);
			
			Level newLevel = getNewLevel(pgi);
			List<Level> levelList = new ArrayList<Level>();
			levelList.add(newLevel);
			ug.setLevel(levelList);
			//save usergame
			GymUserStatus status = new GymUserStatus();
			status.setLastLevel(1);
			status.setTotalEarning(0);
			status.setTotalProgress(0.00);
			Key<GymUserStatus> gymKey = ofy().save().entity(status).now();
			
			ug.setUserStatus(
					Key.create(UserStatus.class, gymKey.getId())
					);
			//user status saved
			ofy().save().entity(ug).now();
			//get Game alunch and add user
			Ref<Users> user = Ref.create(Key.create(Users.class, userId));
			Ref<GameLaunch> glRef = Ref.create(GameLaunch.key(gameLaunchId));
			GameLaunch gl = glRef.get();
			gl.getParticipants().add(user);
			ofy().save().entity(gl).now();
		} else {
			//add new level and save
			Level newLevel = getNewLevel(pgi);
			
			List<Level> levelList = ug.getLevel();
			levelList.add(newLevel);
			ug.setLevel(levelList);
			
			//userstatus
			//get user status
			Key<UserStatus> ustatus = ug.getUserStatus();
			
			GymUserStatus status = ofy().load().type(GymUserStatus.class).id(ustatus.getId()).now();
			status.setLastLevel(levelList.size()-1);
			status.setTotalEarning(0);
			status.setTotalProgress(0.00);
			ofy().save().entity(status).now();
			ofy().save().entity(ug).now();
		}
		//return user status
		String result=null;
		UserStatus us = ofy().load().type(UserStatus.class).id(ug.getUserStatus().getId()).now();
		
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		try {
			mapper.writeValue(sos, us);
		} catch (Exception e) {
			throw new UnauthorizedException("object parser error");
		}
		return sos.toString();
	}
	
	static Level getNewLevel(PowzyGameInitiate pgi) {
		GameParam gp=null;
		//parse parameter of gym game
		gp = PowzyGameInitiate.convertToParam((PowzyGameInitiate)pgi);
		if(gp==null) return null;
		Level newLevel = new Level();
		
		Key<GameParam> gameParamKey = ofy().save().entity(gp).now();
		newLevel.setUserGameParam(gameParamKey);
		GymGameStatus gymStatus = new GymGameStatus(false);
		Key<GymGameStatus> key = ofy().save().entity(gymStatus).now();
		//ref to gameStatus
		newLevel.setUserGameStatus(Key.create(GameStatus.class, key.getId()));
		newLevel.setLastProgress(0.00);
		return newLevel;
	}
	
	static int getLeftDays(int startday, PowzyGameInitiate pgi) {
		Integer[] days = pgi.getCommitedWorkOutdays();
		int count = 0;
		for(int i=startday; i< 7; i++) {
			if(days[i]==1){
				count++;
			}
		}
		return count;
	}
}
