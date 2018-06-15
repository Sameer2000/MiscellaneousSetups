package com.specterex.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.specterex.domain.auth.AuthenticationToken;
import com.specterex.domain.auth.RolePermissionMapping;
import com.specterex.repository.auth.AuthenticationTokenRepository;
import com.specterex.repository.auth.RolePermissionMappingRepository;



/**
 * A filter that check for token and validate that token and put valid token
 * user in SecurityContextHolder, This will help us to get current loggedin user
 * 
 *
 */
@Component
public class TokenAuthenticationFilter extends GenericFilterBean {
	private AuthenticationTokenRepository authenticationTokenRepository;
	private RolePermissionMappingRepository rolePermissionMappingRepository;

	public TokenAuthenticationFilter(AuthenticationTokenRepository authenticationTokenRepository,
			RolePermissionMappingRepository rolePermissionMappingRepository) {
		this.authenticationTokenRepository = authenticationTokenRepository;
		this.rolePermissionMappingRepository = rolePermissionMappingRepository;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
		httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization, Content-Type,*");
		if (httpRequest.getMethod().equals("OPTIONS")) {
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		
		// extract token from header
		final String token = httpRequest.getHeader("Authorization");
		if (token != null) {
			// check whether token is valid
			AuthenticationToken authToken = authenticationTokenRepository.findByToken(token);
			if (authToken != null) {
				// Get allowed current user permissions
				RolePermissionMapping mappedPermisions = rolePermissionMappingRepository.findByRole(authToken.getUser().getRole());
				// Add user to SecurityContextHolder
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						authToken.getUser(), null,new ApplicationUserDetail(authToken.getUser(), mappedPermisions.getPermissions()).getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(httpRequest, httpResponse);
	}

}
