package com.xplaptop.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.xplaptop.common.entity.user.Role;
import com.xplaptop.common.entity.user.User;

public class XPLaptopUserDetails implements UserDetails{
	
	private final User user;
	private final String userName;
	private final String password;
	private final List<GrantedAuthority> lGrantedAuthorities;
	
	public XPLaptopUserDetails(User user) {
		this.user = user;
		this.userName = user.getEmail();
		this.password = user.getPassword();
		this.lGrantedAuthorities = new ArrayList<>();
		for(Role role : user.getRoles()) {
			this.lGrantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return lGrantedAuthorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.user.isEnabled();
	}
	
	public String getFullName() {
		return user.getFirstName() +" "+user.getLastName();
	}
	
	public String getPhotos() {
		return user.getPhotos();
	}
	
	public String getPhotosPath() {
		return user.photoPath();
	}
	
	public Integer getId() {
		return user.getId();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setFirstName(lastName);
	}
	
}
