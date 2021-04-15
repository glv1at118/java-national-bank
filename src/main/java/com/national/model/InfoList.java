package com.national.model;

public class InfoList {
	private String name;
	private String password;
	private String type;
	private int user_id;
	private int account_id;
	private long number;
	private double balance;
	private String state;

	public InfoList(String name, String password, String type, int user_id, int account_id, long number, double balance,
			String state) {
		super();
		this.name = name;
		this.password = password;
		this.type = type;
		this.user_id = user_id;
		this.account_id = account_id;
		this.number = number;
		this.balance = balance;
		this.state = state;
	}

	public String toString() {
		String userAndAccountInfo = "==>> User name: " + this.name + ", User password: " + this.password + ", User type: "
				+ this.type + ", User id: " + this.user_id + ", Account id: " + this.account_id + ", Account number: "
				+ this.number + ", Account balance: " + this.balance + ", Account state: " + this.state + " <<==";
		return userAndAccountInfo;
	}

	public String toBriefString() {
		String userAndAccountInfo = "==>> User name: " + this.name + ", User type: " + this.type + ", Account number: "
				+ this.number + ", Account balance: " + this.balance + ", Account state: " + this.state + " <<==";
		return userAndAccountInfo;
	}

}
