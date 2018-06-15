package com.oofy.ufy.factory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

/**
 * A factory for creating JWTToken objects.
 */
@Component
public class JWTTokenFactory implements Serializable
{

   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = 6347139304711367136L;

   /** The Constant CLAIM_KEY_USERNAME. */
   static final String CLAIM_KEY_USERNAME = "sub";

   /** The Constant CLAIM_KEY_CREATED. */
   static final String CLAIM_KEY_CREATED = "iat";

   /** The clock. */
   private Clock clock = DefaultClock.INSTANCE;

   /** The secret. */
   @Value("${jwt.secret}")
   private String secret;

   /** The expiration. */
   @Value("${jwt.expiration}")
   private Long expiration;

   /**
    * Gets the email from token.
    *
    * @param token the token
    * @return the email from token
    */
   public String getEmailFromToken( String token )
   {
      return getClaimFromToken( token, Claims::getSubject );
   }

   /**
    * Gets the issued at date from token.
    *
    * @param token the token
    * @return the issued at date from token
    */
   public Date getIssuedAtDateFromToken( String token )
   {
      return getClaimFromToken( token, Claims::getIssuedAt );
   }

   /**
    * Gets the expiration date from token.
    *
    * @param token the token
    * @return the expiration date from token
    */
   public Date getExpirationDateFromToken( String token )
   {
      return getClaimFromToken( token, Claims::getExpiration );
   }

   /**
    * Gets the claim from token.
    *
    * @param <T> the generic type
    * @param token the token
    * @param claimsResolver the claims resolver
    * @return the claim from token
    */
   public < T > T getClaimFromToken( String token, Function< Claims, T > claimsResolver )
   {
      final Claims claims = getAllClaimsFromToken( token );
      return claimsResolver.apply( claims );
   }

   /**
    * Gets the all claims from token.
    *
    * @param token the token
    * @return the all claims from token
    */
   private Claims getAllClaimsFromToken( String token )
   {
      return Jwts.parser().setSigningKey( secret ).parseClaimsJws( token ).getBody();
   }

   /**
    * Checks if is token expired.
    *
    * @param token the token
    * @return the boolean
    */
   private Boolean isTokenExpired( String token )
   {
      final Date expiration = getExpirationDateFromToken( token );
      return expiration.before( clock.now() );
   }

   /**
    * Checks if is created before last password reset.
    *
    * @param created the created
    * @param lastPasswordReset the last password reset
    * @return the boolean
    */
   private Boolean isCreatedBeforeLastPasswordReset( Date created, Date lastPasswordReset )
   {
      return ( lastPasswordReset != null && created.before( lastPasswordReset ) );
   }

   /**
    * Ignore token expiration.
    *
    * @param token the token
    * @return the boolean
    */
   private Boolean ignoreTokenExpiration( String token )
   {
      // here you specify tokens, for that the expiration is ignored
      return false;
   }

   /**
    * Generate token.
    *
    * @param email the email
    * @return the string
    */
   public String generateToken( String email )
   {
      Map< String, Object > claims = new HashMap<>();
      return doGenerateToken( claims, email );
   }

   /**
    * Do generate token.
    *
    * @param claims the claims
    * @param subject the subject
    * @return the string
    */
   private String doGenerateToken( Map< String, Object > claims, String subject )
   {
      final Date createdDate = clock.now();
      final Date expirationDate = calculateExpirationDate( createdDate );

      return Jwts.builder().setClaims( claims ).setSubject( subject ).setIssuedAt( createdDate ).setExpiration( expirationDate ).signWith( SignatureAlgorithm.HS512, secret ).compact();
   }

   /**
    * Can token be refreshed.
    *
    * @param token the token
    * @param lastPasswordReset the last password reset
    * @return the boolean
    */
   public Boolean canTokenBeRefreshed( String token, Date lastPasswordReset )
   {
      final Date created = getIssuedAtDateFromToken( token );
      return !isCreatedBeforeLastPasswordReset( created, lastPasswordReset ) && ( !isTokenExpired( token ) || ignoreTokenExpiration( token ) );
   }

   /**
    * Refresh token.
    *
    * @param token the token
    * @return the string
    */
   public String refreshToken( String token )
   {
      final Date createdDate = clock.now();
      final Date expirationDate = calculateExpirationDate( createdDate );

      final Claims claims = getAllClaimsFromToken( token );
      claims.setIssuedAt( createdDate );
      claims.setExpiration( expirationDate );

      return Jwts.builder().setClaims( claims ).signWith( SignatureAlgorithm.HS512, secret ).compact();
   }

   /**
    * Calculate expiration date.
    *
    * @param createdDate the created date
    * @return the date
    */
   private Date calculateExpirationDate( Date createdDate )
   {
      return new Date( createdDate.getTime() + expiration * 1000 );
   }
}
