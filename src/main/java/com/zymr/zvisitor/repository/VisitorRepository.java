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

import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dbo.Visitor.VISITOR_FIELDS;

public interface VisitorRepository extends MongoRepository<Visitor, Integer> {

	@Query(value = "{}", fields = "{ '"+VISITOR_FIELDS.EMPLOYEE_ID+"' : 1 }")
    List<Visitor> findAll(Sort sort);
}
