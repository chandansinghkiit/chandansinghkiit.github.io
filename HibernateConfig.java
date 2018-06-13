package com.mystyle.onlineshoppingbackend.daoimpl;

//Set-ExecutionPolicy Unrestricted


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages={"com.mystyle.onlineshoppingbackend.dto"})
@ComponentScan(basePackages={"com.mystyle.onlineshoppingbackend.daoimpl"})
@EnableTransactionManagement
public class HibernateConfig
{
	
	
  /*  private final static String DATABASE_URL="jdbc:h2:tcp://localhost/~/onlineshoppingcart";
	private final static String DATABASE_DRIVER="org.h2.driver";
	private final static String DATABASE_DIALECT="org.hibernate.dialect.H2Dialect";
	private final static String DATABASE_USERNAME="sa";
	private final static String DATABASE_PASSWORD="";*/
	
	

	private final static String DATABASE_DRIVER="com.mysql.jdbc.Driver";
	private final static String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/onlineshopping";
	private final static String DATABASE_DIALECT="org.hibernate.dialect.MySQLDialect";
	private final static String DATABASE_USERNAME="root";
	private final static String DATABASE_PASSWORD="chandan";
	
	//dataSource bean
	
	@Bean
	public DataSource  getDataSource()
	{
		BasicDataSource  dataSource = new BasicDataSource();
		
		//providing the database connection information
		
		dataSource.setDriverClassName(DATABASE_DRIVER);
		dataSource.setUrl(DATABASE_URL);
		dataSource.setUsername(DATABASE_USERNAME);
		dataSource.setPassword(DATABASE_PASSWORD);
		
		return dataSource;
		
	}
	
	@Qualifier(value="SessionFactory")
	@Bean
	public  SessionFactory getSessionFactory(DataSource dataSource)
	{
		
		LocalSessionFactoryBuilder builder=new LocalSessionFactoryBuilder(dataSource);
		builder.addProperties(getHibernateProperties());
		builder.scanPackages("com.mystyle.onlineshoppingbackend.dto");
		
		
		return builder.buildSessionFactory();
	}

	//All the hibernate properties will returned in this method
	private  Properties getHibernateProperties() {
		
		Properties  properties = new Properties();
		
		properties.put("hibernate.dailect",DATABASE_DIALECT);
		properties.put("hibernate.show_sql","true");
		properties.put("hibernate.format_sql","true");
		System.out.println("inside  Properties");
		
		return properties;
	}
	
	//transactionManger bean
	@Bean
	public  HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager transationManager= new HibernateTransactionManager(sessionFactory);
		return transationManager;
		
	}


	
	
}