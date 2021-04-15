package com.national.model;

public class Account {
	private long number;
	private double balance;
	private String state;

	public Account(long number, double balance, String state) {
		super();
		this.number = number;
		this.balance = balance;
		this.state = state;
	}

	public long getNumber() {
		return this.number;
	}

	public double getBalance() {
		return this.balance;
	}

	public String getState() {
		return this.state;
	}

	public String toString() {
		return "Account number: " + this.number + ", Account balance: " + this.balance + ", Account state: "
				+ this.state;
	}
}
