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

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dbo.Employee.EMPLOYEE_FIELDS;

public interface EmployeeRepository extends MongoRepository<Employee, Integer>, EmployeeRepositoryCustom {

	Employee findByslackId(String salckId);
	
	@Query("{ '"+EMPLOYEE_FIELDS.ID+"': ?0}")
	Employee findById(String id);
		
	@Query(value = "{}", fields = "{ '"+EMPLOYEE_FIELDS.SLACK_ID+"' : 1 }")
    List<Employee> findAll(Sort sort);
	
	@Query(value = "{}", fields = "{ '"+EMPLOYEE_FIELDS.SLACK_UPDATEDTIME+"' : 1}")
	Employee findOne(Sort sort);
	
}
