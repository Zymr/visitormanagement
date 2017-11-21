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

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.zymr.zvisitor.dbo.config.PropertyConfiguration;

public interface ConfigurationRepository  extends MongoRepository<PropertyConfiguration, String> {
	
	@Query(value = "{}")
	PropertyConfiguration findOne(Sort sort);

}
