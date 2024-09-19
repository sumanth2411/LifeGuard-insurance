//package com.techlabs.app.dto;
//
//public class JWTAuthResponse {
//	private String accessToken;
//	private String tokenType = "Bearer";
//
//	public JWTAuthResponse() {
//		super();
//	}
//
//	public JWTAuthResponse(String accessToken, String tokenType) {
//		super();
//		this.accessToken = accessToken;
//		this.tokenType = tokenType;
//	}
//
//	public String getAccessToken() {
//		return accessToken;
//	}
//
//	public void setAccessToken(String accessToken) {
//		this.accessToken = accessToken;
//	}
//
//	public String getTokenType() {
//		return tokenType;
//	}
//
//	public void setTokenType(String tokenType) {
//		this.tokenType = tokenType;
//	}
//
//}
//

//package com.techlabs.app.dto;
//
//public class JWTAuthResponse {
//	private String accessToken;
//	private String tokenType = "Bearer";
//	private String role;
//	// private Long customerId;
//
//	// No-args constructor
//	public JWTAuthResponse() {
//	}
//
//	public JWTAuthResponse(String accessToken, String tokenType, String role) {
//		super();
//		this.accessToken = accessToken;
//		this.tokenType = tokenType;
//		this.role = role;
//	}
//
//	public String getAccessToken() {
//		return accessToken;
//	}
//
//	public void setAccessToken(String accessToken) {
//		this.accessToken = accessToken;
//	}
//
//	public String getTokenType() {
//		return tokenType;
//	}
//
//	public void setTokenType(String tokenType) {
//		this.tokenType = tokenType;
//	}
//
//	public String getRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}
//
//	@Override
//	public String toString() {
//		return "JWTAuthResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", role=" + role + "]";
//	}
//
//	
//
//}




//package com.techlabs.app.dto;
//
//public class JWTAuthResponse {
//	private String accessToken;
//	private String tokenType = "Bearer";
//	private String role;
//	private Long customerId;
//	private Long agentId;
//
//	// No-args constructor
//	public JWTAuthResponse() {
//	}
//
//	public JWTAuthResponse(String accessToken, String tokenType, String role, Long customerId, Long agentId) {
//		super();
//		this.accessToken = accessToken;
//		this.tokenType = tokenType;
//		this.role = role;
//		this.customerId = customerId;
//		this.agentId = agentId;
//	}
//
//	public Long getAgentId() {
//		return agentId;
//	}
//
//	public void setAgentId(Long agentId) {
//		this.agentId = agentId;
//	}
//
//	public Long getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Long customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getAccessToken() {
//		return accessToken;
//	}
//
//	public void setAccessToken(String accessToken) {
//		this.accessToken = accessToken;
//	}
//
//	public String getTokenType() {
//		return tokenType;
//	}
//
//	public void setTokenType(String tokenType) {
//		this.tokenType = tokenType;
//	}
//
//	public String getRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}
//
//	@Override
//	public String toString() {
//		return "JWTAuthResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", role=" + role + "]";
//	}
//}


package com.techlabs.app.dto;

public class JWTAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;
    private Long customerId;
    private Long agentId;
    private String username;
    private String message; // New field

    // No-args constructor
    public JWTAuthResponse() {
    }

    // All-args constructor
    public JWTAuthResponse(String accessToken, String tokenType, String role, Long customerId, Long agentId, String message) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.role = role;
        this.customerId = customerId;
        this.agentId = agentId;
        this.message = message;
    }

    // Getters and Setters
    
    

    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Override toString() to include all fields
    @Override
    public String toString() {
        return "JWTAuthResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", role=" + role
                + ", customerId=" + customerId + ", agentId=" + agentId + ", message=" + message + "]";
    }
}
