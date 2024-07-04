package com.example.feedforward_courier.models.server.command;

public class CommandId {
	private String superapp;
	 private String miniapp;
	 private String id;
	 
	 
	 
	 public CommandId() {
		
	}

	public CommandId(String superAppName, String miniapp, String id) {
			this.superapp = superAppName;
			this.miniapp = miniapp;
			this.id = id;
		}
	 
	public String getSuperAppName() {
		return superapp;
	}
	public void setSuperAppName(String superAppName) {
		this.superapp = superAppName;
	}
	public String getMiniapp() {
		return miniapp;
	}
	public void setMiniapp(String miniapp) {
		this.miniapp = miniapp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CommandId [superAppName=" + superapp + ", miniapp=" + miniapp + ", id=" + id + "]";
	}

}
