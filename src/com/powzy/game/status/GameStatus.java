package com.powzy.game.status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
@NoArgsConstructor
public class GameStatus {
	@Id
	Long gameLevelStatus;
	

	@Getter @Setter
	boolean isCompleted;
	
	public GameStatus(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
