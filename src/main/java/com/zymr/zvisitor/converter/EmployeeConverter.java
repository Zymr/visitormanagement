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
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dto.EmployeeDTO;

@Service
public class EmployeeConverter implements Converter<Employee, EmployeeDTO> {

	@Override
	public EmployeeDTO convertToDTO(Employee employee) {
		if (Objects.isNull(employee)) {
			return null;
		}
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setFullName(employee.getFullName());
		employeeDTO.setDeleted(employee.isDeleted());
		employeeDTO.setEmail(employee.getEmail());
		employeeDTO.setId(employee.getId());
		employeeDTO.setImageLarge(employee.getImageLarge());
		employeeDTO.setImageSmall(employee.getImageSmall());
		employeeDTO.setName(employee.getName());
		employeeDTO.setPhone(employee.getPhone());
		employeeDTO.setSlackId(employee.getSlackId());
		employeeDTO.setTeamId(employee.getteamId());
		employeeDTO.setTitle(employee.getTitle());
		employeeDTO.setSlackUt(employee.getSlackUt());
		employeeDTO.setLocation(employee.getLocation());
		return employeeDTO;
	}

	@Override
	public Collection<EmployeeDTO> convertToDTO(Collection<Employee> employees) {
		if (CollectionUtils.isEmpty(employees)) {
			return null;
		}
		return employees.stream().
				filter(Objects::nonNull).map(employee -> convertToDTO(employee))
				.collect(Collectors.toList());
	}

	@Override
	public Employee convert(EmployeeDTO d) {
	  throw new NotImplementedException(EmployeeConverter.class);
	}

	@Override
	public Collection<Employee> convert(Collection<EmployeeDTO> d) {
	  throw new NotImplementedException(EmployeeConverter.class);
	}
}
