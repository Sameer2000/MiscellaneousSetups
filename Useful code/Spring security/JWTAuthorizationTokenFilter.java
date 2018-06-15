package com.oofy.ufy.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oofy.ufy.entities.User;
import com.oofy.ufy.enums.TokenType;
import com.oofy.ufy.service.IUserService;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * JWTAuthorizationTokenFilter.
 */
public class JWTAuthorizationTokenFilter extends OncePerRequestFilter
{

   /** The logger. */
   private final Logger logger = LoggerFactory.getLogger( this.getClass() );
   
   /** The token header. */
   private String tokenHeader;

   /**
    * Instantiates a new JWT authorization token filter.
    *
    * @param tokenHeader the token header
    */
   public JWTAuthorizationTokenFilter( String tokenHeader )
   {
      this.tokenHeader = tokenHeader;
   }

   /**
    * Same contract as for {@code doFilter}, but guaranteed to be just invoked once
    * per request within a single request thread. See
    * {@link #shouldNotFilterAsyncDispatch()} for details.
    * <p>
    * Provides HttpServletRequest and HttpServletResponse arguments instead of the
    * default ServletRequest and ServletResponse ones.
    *
    * @param request the request
    * @param response the response
    * @param filterChain the filter chain
    * @throws ServletException the servlet exception
    * @throws IOException Signals that an I/O exception has occurred.
    */
   @Override
   protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException
   {

      logger.debug( "processing authentication for '{}'", request.getRequestURL() );

      final String requestHeader = request.getHeader( this.tokenHeader );

      String username = null;
      String authToken = null;
      User user = null;
      if ( requestHeader != null )
      {
         authToken = requestHeader;
         try
         {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext( servletContext );
            IUserService userService = webApplicationContext.getBean( IUserService.class );
            user = userService.getUserByToken( authToken, TokenType.LOGIN );
            if ( user != null )
            {
               username = user.getEmail();
            }
         }
         catch ( IllegalArgumentException e )
         {
            logger.error( "an error occured during getting username from token", e );
         }
         catch ( ExpiredJwtException e )
         {
            logger.warn( "the token is expired and not valid anymore", e );
         }
      }
      else
      {
         logger.warn( "couldn't find bearer string, will ignore the header" );
      }

      logger.debug( "checking authentication for user '{}'", username );
      if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null )
      {
         logger.debug( "security context was null, so authorizating user" );

         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( user, user.getPassword(), user.getAuthorities() );
         authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
         logger.info( "authorizated user '{}', setting security context", username );
         SecurityContextHolder.getContext().setAuthentication( authentication );
      }

      filterChain.doFilter( request, response );

   }
}
