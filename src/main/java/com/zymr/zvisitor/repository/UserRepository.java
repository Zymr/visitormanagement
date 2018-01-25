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

import com.zymr.zvisitor.dbo.Users;


public interface UserRepository extends MongoRepository<Users,String>{

	public Users findByEmail(String email);
	
	Long countByEmail(String email);
}
