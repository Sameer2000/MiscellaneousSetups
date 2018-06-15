package com.oofy.ufy.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.oofy.ufy.config.EnvironmentConfiguration;
import com.oofy.ufy.service.CustomUserDetailService;

/**
 * WebSecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

   /** The custom user detail service. */
   @Autowired
   CustomUserDetailService customUserDetailService;

   /** The unauthorized handler. */
   @Autowired
   private JWTAuthenticationEntryPoint unauthorizedHandler;

   /** The token header. */
   @Value("${jwt.header}")
   private String tokenHeader;

   /** The authentication path. */
   @Value("${jwt.route.authentication.path}")
   private String authenticationPath;

   /** The env. */
   @Autowired
   EnvironmentConfiguration env;

   /**
    * Configure global.
    *
    * @param auth the auth
    * @throws Exception the exception
    */
   @Autowired
   public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception
   {
      auth.userDetailsService( customUserDetailService ).passwordEncoder( passwordEncoderBean() );
   }

   /**
    * Password encoder bean.
    *
    * @return the password encoder
    */
   @Bean
   public PasswordEncoder passwordEncoderBean()
   {
      return new BCryptPasswordEncoder();
   }

   /* (non-Javadoc)
    * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
    */
   @Bean
   @Override
   public AuthenticationManager authenticationManagerBean() throws Exception
   {
      return super.authenticationManagerBean();
   }

   /* (non-Javadoc)
    * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
    */
   @Override
   protected void configure( HttpSecurity httpSecurity ) throws Exception
   {
      httpSecurity

            // we don't need CSRF because our token is invulnerable
            .csrf().disable().exceptionHandling().authenticationEntryPoint( unauthorizedHandler ).and()

            // don't create session
            .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and().requestMatchers().and().authorizeRequests()

            .antMatchers( HttpMethod.POST, "/api/v1/user", "/api/v1/authentication/**", "/api/v1/reset-password-request", "/api/v1/reset-password", "/api/v1/admin/deploy" ).permitAll().antMatchers( HttpMethod.GET, "/templates/**", "/api/v1/user/activate", "/api/v1/admin/**" ).permitAll().antMatchers( HttpMethod.GET, "/api/v1/user/**" ).authenticated();

      // Custom JWT based security filter
      JWTAuthorizationTokenFilter authenticationTokenFilter = new JWTAuthorizationTokenFilter( tokenHeader );
      httpSecurity.addFilterBefore( authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class );

   }

}
