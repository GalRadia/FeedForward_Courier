package com.example.feedforward_courier.models.server.command;

public class InvokedBy {
private UserId userId;
	
	public InvokedBy() {
	}
	
	public InvokedBy(String superAppName, String email ) {
		this.userId = new UserId(superAppName,email);
	}
	
	public InvokedBy(UserId userId) {
		super();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "InvokedBy [userId=" + userId + "]";
	}

}
