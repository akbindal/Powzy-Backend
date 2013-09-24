package com.powzy.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class UserDevice {
	@Id
	Long customerId;
	
	@Getter
	Long timestamp;
	
	@Getter
	String osDetail;
	
	@Getter
	String deviceName;
	
	@Getter
	String powzyAppVersion;
	
	public UserDevice(Long customerId, String osDetail, 
			String deviceName, String appVersion) {
		this.customerId = customerId;
		this.osDetail = osDetail;
		this.deviceName = deviceName;
		this.powzyAppVersion = appVersion;
		this.timestamp = new Date().getTime();
	}
}
