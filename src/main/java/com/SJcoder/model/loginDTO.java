package com.SJcoder.model;

public class loginDTO {
	
	private String userName;
	private String password;
	
	public loginDTO() {
		super();
	}
	@Override
	public String toString() {
		return "LoginDTO :" + userName + ", password=" + password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}
