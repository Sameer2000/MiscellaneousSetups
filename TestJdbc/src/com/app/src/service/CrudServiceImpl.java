package com.app.src.service;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import com.app.src.factory.ConnectionFactory;
import com.app.src.template.CrudTemplate;

public class CrudServiceImpl<T extends Serializable> implements CrudService<T>{
	
	private Connection connection = null;
	
	public CrudServiceImpl() {
		this.connection = ConnectionFactory.getConnection();
	}
	
	@Override
	public int save(Integer amount, String personName) {
		if(CrudTemplate.saveTemplate(connection, amount, personName)){
			return 1;
		}
		else{
			return 0;
		}
	}

	@Override
	public int update(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object find(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
