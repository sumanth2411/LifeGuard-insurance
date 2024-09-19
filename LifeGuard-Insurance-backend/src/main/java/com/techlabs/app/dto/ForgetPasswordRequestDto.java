package com.techlabs.app.dto;

import lombok.Data;

@Data
public class ForgetPasswordRequestDto {
  private String usernameOrEmail;

public String getUsernameOrEmail() {
	return usernameOrEmail;
}

public void setUsernameOrEmail(String usernameOrEmail) {
	this.usernameOrEmail = usernameOrEmail;
}
  
  
}