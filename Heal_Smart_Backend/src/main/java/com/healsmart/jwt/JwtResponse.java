package com.healsmart.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class JwtResponse {

    private String jwtToken;

	public JwtResponse(String token) {
		// TODO Auto-generated constructor stub
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	
}