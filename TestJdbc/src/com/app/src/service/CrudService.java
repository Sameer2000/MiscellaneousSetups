package com.app.src.service;

import java.io.Serializable;
import java.util.List;

public interface CrudService<T extends Serializable> {

	public abstract int save(Integer amount, String personName);
	
	public abstract int update(Object object);
	
	public abstract void delete(Object object);
	
	public abstract Object find(T id);
	
	public abstract List<Object> findAll();
	
}
