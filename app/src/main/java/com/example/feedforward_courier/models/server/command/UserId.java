package com.example.feedforward_courier.models.server.command;

public class UserId {
	private String superapp;
	private String email;
	
	public UserId(String superAppName, String email) {
		this.superapp = superAppName;
		this.email = email;
	}

	public UserId() {
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superAppName) {
		this.superapp = superAppName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserIdBoundry [superAppName=" + superapp + ", email=" + email + "]";
	}

	
}
