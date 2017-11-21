/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zymr.zvisitor.converter.EmployeeConverter;
import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dto.EmployeeDTO;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.service.EmployeeService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(Constants.EMPLOYEE_URL)
public class EmployeeResource extends BaseResource {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeResource.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeConverter employeeConverter;

	@RequestMapping(value = "/{locId}", method = RequestMethod.GET)
	@ApiOperation(value = "Fetch employees of specific location.", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> get(@PathVariable @Valid String locId) {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.notFound().build();
		try {
			if (StringUtils.isNotBlank(locId)) { 
				List<Employee> employees = employeeService.getByLocation(locId);
				if (CollectionUtils.isNotEmpty(employees)) {
					Collection<EmployeeDTO> employeesDTO = employeeConverter.convertToDTO(employees);
					ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.employees.name(), employeesDTO);
					result = ResponseEntity.ok().body(responseDTO.getResponse());
				}
			}
		} catch(Exception e) {
			logger.error("Exception while fetching employees.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value ="{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") @Valid String slackId) {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			if (StringUtils.isNotBlank(slackId)) {
				employeeService.deleteBySlackId(slackId);
				result = ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			logger.error("Exception while deleting employee.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value="/syncEmployee", method = RequestMethod.GET) 
	public ResponseEntity<Map<String, Object>> syncEmployee() throws Exception {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			employeeService.upsertEmployeeFromSlack();
			result = ResponseEntity.ok().build(); 
		} catch (Exception e) {
			logger.error("Exception while syncing channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result; 
	}
}
