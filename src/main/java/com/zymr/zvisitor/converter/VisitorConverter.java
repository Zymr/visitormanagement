/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.converter;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dto.VisitorDTO;

@Service
public class VisitorConverter implements Converter<Visitor, VisitorDTO> {
	
	@Autowired
	EmployeeConverter employeeConverter;
	
	@Autowired
	ChannelConverter channelConverter;

	@Override
	public VisitorDTO convertToDTO(Visitor visitor) {
		if (Objects.isNull(visitor)) {
			return null;
		}
		VisitorDTO visitorDTO = new VisitorDTO();
		visitorDTO.setEmail(visitor.getEmail());
		visitorDTO.setMobile(visitor.getMobile());
		visitorDTO.setName(visitor.getName());
		visitorDTO.setPurpose(visitor.getPurpose());
		visitorDTO.setCategoryName(visitor.getCategoryName());
		visitorDTO.setId(visitor.getId());
		visitorDTO.setVisitorPic(visitor.getVisitorPic());
		visitorDTO.setVisitorSignature(visitor.getVisitorSignature());
		visitorDTO.setLocation(visitor.getLocation());
		visitorDTO.setDate(visitor.getCreatedTime().getTime());
		return visitorDTO;
	}

	@Override
	public Collection<VisitorDTO> convertToDTO(Collection<Visitor> visitors) {
		if (CollectionUtils.isEmpty(visitors)) {
			return null;
		}
		return visitors.stream().filter(Objects::nonNull)
				.map(visitor -> convertToDTO(visitor))
				.collect(Collectors.toList());	}

	@Override
	public Visitor convert(VisitorDTO visitorDTO) {
		if (Objects.isNull(visitorDTO)) {
			return null;
		}
		Visitor visitor = new Visitor(visitorDTO.getName());
		visitor.setEmail(visitorDTO.getEmail());
		visitor.setMobile(visitorDTO.getMobile());
		visitor.setPurpose(visitorDTO.getPurpose());
		visitor.setCategoryName(visitorDTO.getCategoryName());
		visitor.setEmployeeId(visitorDTO.getEmpId());
		visitor.setChannelId(visitorDTO.getChannelId());
		visitor.setLocation(visitorDTO.getLocation());
		return visitor;
	}

	@Override
	public Collection<Visitor> convert(Collection<VisitorDTO> d) {
	  throw new NotImplementedException(VisitorConverter.class);
	}
}
