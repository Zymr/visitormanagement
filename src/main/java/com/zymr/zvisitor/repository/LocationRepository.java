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

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.dbo.Location.LOCATION_FIELDS;

public interface LocationRepository extends MongoRepository<Location, Integer> {

	@Query("{ '"+LOCATION_FIELDS.ID+"': ?0}")
	Location findById(String id);
	
	@Query(value = "{}", fields = "{ '"+LOCATION_FIELDS.LOCATION_NAME+"' : 1 }")
	Location findByLocation(String locationId);
}
