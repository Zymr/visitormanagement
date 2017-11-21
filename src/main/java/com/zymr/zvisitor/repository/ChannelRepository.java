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

import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dbo.SlackChannel.SLACKCHANNEL_FIELDS;

public interface ChannelRepository extends MongoRepository<SlackChannel, String>{

	SlackChannel findByChannelId(String channelId);

	@Query("{ '"+SLACKCHANNEL_FIELDS.ID+"': ?0}")
	SlackChannel findById(String id);
}
