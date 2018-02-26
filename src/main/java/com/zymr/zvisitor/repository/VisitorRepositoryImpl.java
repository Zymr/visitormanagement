package com.zymr.zvisitor.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dbo.Visitor.VISITOR_FIELDS;
import com.zymr.zvisitor.dto.VisitorQueryDTO;

public class VisitorRepositoryImpl implements VisitorRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Page<Visitor> get(VisitorQueryDTO visitorQueryDTO, Pageable pageable) {
		Query query = new Query().with(pageable);
		visitorQueryDTO.getFindByInParams()
					   .entrySet()
					   .stream()
					   .forEach(e-> query.addCriteria(Criteria.where(e.getKey()).in(e.getValue())));
		query.addCriteria(Criteria.where(VISITOR_FIELDS.CREATED_TIME).gte(visitorQueryDTO.getFindByGte()).lte(visitorQueryDTO.getFindByLte()));
		List<Visitor> tickets =  mongoTemplate.find(query, Visitor.class);
		return PageableExecutionUtils.getPage(
				tickets, 
				pageable, 
				() -> mongoTemplate.count(query, Visitor.class));
	}


}
