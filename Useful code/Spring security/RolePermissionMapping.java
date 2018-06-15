package com.specterex.domain.auth;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


/**
 * Role permission mapping domain
 *
 */

@Entity
public class RolePermissionMapping {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String role;
	@OneToMany(fetch = FetchType.EAGER,cascade = { CascadeType.ALL })
	@NotNull
	private Set<Permission> permissions;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public RolePermissionMapping() {
		super();
	}
	public RolePermissionMapping(String role, Set<Permission> permissions) {
		super();
		this.role = role;
		this.permissions = permissions;
	}

}
