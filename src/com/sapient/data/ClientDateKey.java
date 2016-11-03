package com.sapient.data;

public class ClientDateKey {

	private String clientId;
	private int date;
	
	public ClientDateKey(String clientId, int date) {
		this.clientId = clientId;
		this.date = date;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

}
