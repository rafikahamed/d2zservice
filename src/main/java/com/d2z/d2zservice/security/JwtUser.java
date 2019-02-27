package com.d2z.d2zservice.security;

import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {

    private final int user_id;
    private final String username;
    private final String password;
    private final String email;
    private final Date lastPasswordResetDate;

    public JwtUser(
    	 int user_id,
          String username,
          String email,
          String password, 
          Date lastPasswordResetDate
    ) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @JsonIgnore
    public int getUser_id() {
		return user_id;
	}

	@Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}