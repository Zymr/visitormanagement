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

import java.util.Optional;

public interface LocationRepository extends MongoRepository<Location, String> {

	@Query("{ '"+LOCATION_FIELDS.ID+"': ?0}")
	Optional<Location> findById(String id);
	
	Location findByLocation(String locationId);
	
	long countById(String id);
}
