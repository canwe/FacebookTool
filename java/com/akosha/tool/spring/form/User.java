package com.akosha.tool.spring.form;

import javax.persistence.Entity;

import com.restfb.Facebook;

@Entity
public class User 
{
	private String userId;
	
	private String userName;
	
	private String userPicLink;
	
	
	public String getUserId() {
		return userId;
	}

	public String getUserId(String userId) {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPicLink() {
		return userPicLink;
	}

	public void setUserPicLink(String userPicLink) {
		this.userPicLink = userPicLink;
	}
	

}
