/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.repository;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.zymr.zvisitor.dbo.Employee;

public interface EmployeeRepositoryCustom {

	void insertAll(List<Employee> listObjects);
	
	Update update(Employee emlpoyee);
	
	void upsert(Employee employee, String columnName);
	
	List<Employee> findByLocation(String location);
}
