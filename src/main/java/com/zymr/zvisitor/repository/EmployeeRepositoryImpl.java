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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dbo.Employee.EMPLOYEE_FIELDS;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
	@Autowired
	MongoTemplate mongoTemplate;
	
	public static final String key = "$set";

	@Override
	public void insertAll(List<Employee> listObjects) {
		mongoTemplate.insertAll(listObjects);
	}

	public Update update(Employee emlpoyee) {
		DBObject empDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(emlpoyee);
		empDBObject.removeField(EMPLOYEE_FIELDS.ID);
		return Update.fromDBObject(new BasicDBObject(key, empDBObject)); 
	}

	public void upsert(Employee employee, String columnName) {
		Update update = update(employee);
		Query query = new Query(Criteria.where(columnName).is(employee.getSlackId())); 
		mongoTemplate.upsert(query, update, Employee.class);
	}
	
	public List<Employee> findByLocation(String location) {
		Query query = new Query(Criteria.where(EMPLOYEE_FIELDS.LOCATION).is(location)); 
		return mongoTemplate.find(query, Employee.class);
	}
}
