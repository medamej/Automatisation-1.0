package com.automatisation.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class SignupRequest {
	@NotNull
	private String username;

	@NotNull
	private String email;
	
	@NotNull
	private String password;
	
	
	private Set<String> role;
    @NotNull

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<String> getRole() {
		return role;
	}
	public void setRole(Set<String> role) {
		this.role = role;
	}

	
	
//    private Long employe_id;
//	
//    @NotNull
//    @Size(min = 3, max = 50)
//    private String username;
// 
//    @NotNull
//    @Size(max = 50)
//    //@Email
//    private String email;
//    @NotNull
//    private Set<String> role;
//    @NotNull
//    private String matricule; 
//    @NotNull
//    private String nom;
//    @NotNull
//    private String prenom;
//    @NotNull
//    private Long pole_id;
//    
//    
//    @Size(min = 6, max = 40)
//    private String password;
  
}

