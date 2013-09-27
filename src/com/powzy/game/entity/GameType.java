package com.powzy.game.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.powzy.game.param.GameParam;

@Entity
@NoArgsConstructor
@XmlRootElement
public class GameType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3749645904657690237L;

	@Id
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	@Index String title;
	
}