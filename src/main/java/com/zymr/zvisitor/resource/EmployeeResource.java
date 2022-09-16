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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

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
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.service.EmployeeService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;


import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
public class EmployeeResource {


	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeConverter employeeConverter;

	@RequestMapping(value = Constants.GET_EMPLOYEE, method = RequestMethod.GET)
	@Operation(summary = "Fetch employees of specific location.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Fetch employees of specific location",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> get(@PathVariable @NotBlank String locId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.ok().body(new ResponseDTO(ZvisitorResource.EMPLOYEES.toLowerCase(), Collections.EMPTY_LIST).getResponse());
		try {
			List<Employee> employees = employeeService.getByLocation(locId);
			if (CollectionUtils.isNotEmpty(employees)) {
				Collection<EmployeeDTO> employeesDTO = employeeConverter.convertToDTO(employees);
				ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.EMPLOYEES.toLowerCase(), employeesDTO);
				result = ResponseEntity.ok().body(responseDTO.getResponse());
			} 
		}  catch (Exception e) {
			log.error("Exception while fetching employees.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.DELETE_EMPLOYEE, method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") @NotBlank String slackId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			employeeService.deleteBySlackId(slackId);
			result = ResponseEntity.ok().build();
		} catch (NoDataFoundException e) {
			log.error("Exception while deleting employee.", e);
		}  catch (Exception e) {
			log.error("Exception while deleting employee.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.SYNC_EMPLOYEE, method = RequestMethod.GET) 
	public ResponseEntity<Map<String, Object>> syncEmployee() throws Exception {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			employeeService.upsertEmployeeFromSlack();
			result = ResponseEntity.ok().build(); 
		}  catch (Exception e) {
			log.error("Exception while syncing channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result; 
	}
}
