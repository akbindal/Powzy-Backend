package com.powzy.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.Users;
import com.powzy.game.entity.GameType;
import com.powzy.game.param.GameParam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@XmlRootElement
public class GameLaunch implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5752479859310701813L;
	
	public static Key<GameLaunch> key(Long id) {
		return Key.create(GameLaunch.class, id);
	}
	@Id
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	@Load  @Index 
	Ref<BusinessEntity> owner;
	
	@Getter @Setter
	String shortDesc;
	
	@Getter @Setter
	String longDesc;
	
	@Getter @Setter
	String rules;
	
	@Getter @Setter
	Long dateCreated;
	
	
	/**
	 * Business Game, Chair Game
	 */
	@Getter @Setter
	@Load Ref<GameType> gameType;
	
	/**
	 * 
	 */
	@Getter @Setter
	String gameLogoUrl;
	
	/**
	 * this game param is shared by user game param
	 * and 
	 */
	@Getter @Setter
	Ref<GameParam> bussGameParam;
	
	@Getter @Setter
	Set<Ref<Users>> participants = new HashSet<Ref<Users>>();
	
//feed for the business
}
