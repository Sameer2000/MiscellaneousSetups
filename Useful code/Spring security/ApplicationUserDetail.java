package com.specterex.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.specterex.domain.User;
import com.specterex.domain.auth.Permission;
/**
 * A class that implements spring security's UserDetails interface.
 * <br>
 * This class plays a crucial role in providing access to a user for a secured 
 * API
 *
 */
public class ApplicationUserDetail implements UserDetails{

	private static final long serialVersionUID = 1L;
	private User user;
	private Set<Permission> permissionList;
	
	public ApplicationUserDetail(User user,Set<Permission> permissionList){
		this.user = user;
		this.permissionList = permissionList;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList=new ArrayList<GrantedAuthority>();
		if(user.getBlockedPermissions()!=null){
			permissionList.removeAll(user.getBlockedPermissions());			
		}
		for(Permission permissionInfo: permissionList){
			GrantedAuthority authority = new SimpleGrantedAuthority(permissionInfo.getPermissionName());
			authList.add(authority);
		}
		return authList;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
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
		return true;
	}
	
}
