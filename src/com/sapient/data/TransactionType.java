package com.sapient.data;

public enum TransactionType {

	BUY(0), SELL(1), DEPOSIT(2), WITHDRAW(3);

	private int id;

	TransactionType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
