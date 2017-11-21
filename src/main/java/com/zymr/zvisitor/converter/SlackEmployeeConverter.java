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

import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dto.slack.SlackEmployee;

@Service
public class SlackEmployeeConverter implements Converter<Employee, SlackEmployee> {

	@Override
	public SlackEmployee convertToDTO(Employee s) {
	  throw new NotImplementedException(SlackEmployeeConverter.class);
	}

	@Override
	public Collection<SlackEmployee> convertToDTO(Collection<Employee> s) {
	  throw new NotImplementedException(SlackEmployeeConverter.class);
	}

	@Override
	public Employee convert(SlackEmployee slackEmployee) {
		if (Objects.isNull(slackEmployee)) {
			return null;
		}
		Employee employee = new Employee();
		employee.setDeleted(slackEmployee.isDeleted());
		employee.setName(slackEmployee.getName());
		employee.setSlackId(slackEmployee.getSlackId());
		employee.setteamId(slackEmployee.getTeamId());
		employee.setSlackUt(slackEmployee.getUpdatedTime());
		
		if (Objects.nonNull(slackEmployee.getProfile())) {
			employee.setEmail(slackEmployee.getProfile().getEmail());
			employee.setFullName(slackEmployee.getProfile().getFullName());
			employee.setImageLarge(slackEmployee.getProfile().getImage192());
			employee.setImageSmall(slackEmployee.getProfile().getImage72());
			employee.setTitle(slackEmployee.getProfile().getTitle());
			employee.setPhone(slackEmployee.getProfile().getPhone());
		}
		return employee;
	}

	@Override
	public Collection<Employee> convert(Collection<SlackEmployee> d) {
	  throw new NotImplementedException(SlackEmployeeConverter.class);
	}
}
