package com.national.model;

public class User {
	private String userName;
	private String passWord;
	private String userType;

	public User(String userName, String passWord, String userType) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userType = userType;
	}

	public User(String userName, String userType) {
		super();
		this.userName = userName;
		this.userType = userType;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public String getUserType() {
		return this.userType;
	}

	public String toString() {
		return "User name: " + this.userName + ", User type: " + this.userType + ", Password: " + this.passWord;
	}

	public String toStringWithoutPw() {
		return "User name: " + this.userName + ", User type: " + this.userType;
	}
}
