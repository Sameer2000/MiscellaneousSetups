package com.app.src.configs;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("dev")
@EnableTransactionManagement
public class DevConfigs
{
   @Value("${spring.mysql.datasource.className}")
   private String className;

   @Value("${spring.mysql.datasource.url}")
   private String url;

   @Value("${spring.mysql.datasource.password}")
   private String password;

   @Value("${spring.mysql.datasource.username}")
   private String username;

   @Value("${spring.jpa.hibernate.packagesToScan}")
   private String packagesToScan;

   @Value("${spring.datasource.testWhileIdle}")
   private String testWhileIdle;

   @Value("${spring.datasource.validationQuery}")
   private String validationQuery;

   @Value("${spring.jpa.show-sql}")
   private String showSql;

   @Value("${spring.jpa.hibernate.ddl-auto}")
   private String ddlAuto;

   @Value("${spring.jpa.hibernate.naming-strategy}")
   private String namingStategy;

   @Value("${spring.jpa.properties.hibernate.dialect}")
   private String dialect;

   @Bean(destroyMethod = "close")
   public DataSource getDataSource()
   {
      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setDriverClassName( className );
      dataSource.setUrl( url );
      dataSource.setUsername( username );
      dataSource.setPassword( password );
      return dataSource;
   }

   @Bean
   public LocalSessionFactoryBean getSessionFactory()
   {
      LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
      Properties properties = new Properties();
      properties.setProperty( "hibernate.hbm2ddl.auto", ddlAuto );
      properties.setProperty( "hibernate.dbcp.testWhileIdle", testWhileIdle );
      properties.setProperty( "hibernate.dbcp.validationQuery", validationQuery );
      properties.setProperty( "hibernate.show_sql", showSql );
      properties.setProperty( "hibernate.connection.pool_size", "2" );
      properties.setProperty( "hibernate.dialect", dialect );
      
      sessionFactory.setDataSource( getDataSource() );
      sessionFactory.setPackagesToScan( packagesToScan );;
      sessionFactory.setHibernateProperties( properties );
      return sessionFactory;
   }

   @Bean
   public HibernateTransactionManager getTransactionManager( SessionFactory sessionFactory )
   {
      HibernateTransactionManager txManager = new HibernateTransactionManager();
      txManager.setSessionFactory( sessionFactory );
      return txManager;
   }

   @Bean
   public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
   {
      return new PersistenceExceptionTranslationPostProcessor();
   }

}
