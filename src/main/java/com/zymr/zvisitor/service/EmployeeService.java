/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.service;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zymr.zvisitor.converter.SlackEmployeeConverter;
import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dbo.Employee.EMPLOYEE_FIELDS;
import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.dto.slack.SlackEmployee;
import com.zymr.zvisitor.repository.EmployeeRepository;

@Service
public class EmployeeService {
	protected static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired 
	private EmployeeRepository employeeRepository;

	@Autowired
	private SlackService slackService;

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private SlackEmployeeConverter slackEmployeeConverter;
	
	@Autowired
	private ConfigurationService configurationService;
	
	/**
	 * Sync employees from slack and save to our database.
	 * 
	 * @return Nothing.
	 * @throws Exception
	 */  
	public void syncEmployeeFromSlack() throws Exception {
		if (employeeRepository.count() == 0) {
			upsertEmployeeFromSlack();
		}		
	}

	/**
	 * 
	 * @return Nothing.
	 * @throws Exception
	 */  
	public void upsertEmployeeFromSlack() throws Exception {
		logger.info("Syncing of updating employees from slack started.");
		List<SlackEmployee> slackEmployees = slackService.getEmployeeList();
		List<String> dbSlackId = getDbEmployee();
		long latestUpdatedTime = getLatestSlackUpdatedTime();
		Map<Set, String> employeesWithLocation = getSlackEmployeesWithLocation();
		
		List<Employee> employees = slackEmployees.stream()
				.filter(emp -> emp.isUpdated(latestUpdatedTime) || emp.isNewEmployee(dbSlackId))
				.filter(emp -> emp.isCompanyEmployee(configurationService.getValidEmailList()))
				.map(emp -> updateLocation(emp, employeesWithLocation))
				.filter(emp -> Objects.nonNull(emp))
				.collect(Collectors.toList());
		update(employees);
	}
	
	/**
	 * @return List<Employee>
	 */
	public List<Employee> get() {
		return employeeRepository.findAll();
	}

	/**
	 * To update employees.
	 * 
	 * @param employees
	 */  
	public void update(List<Employee> employees) {
		employees.forEach (employee -> employeeRepository.upsert(employee, EMPLOYEE_FIELDS.SLACK_ID));
	}
	
	/**
	 * @param id
	 */
	public void delete(String id) {
		Employee employee = getById(id);
		if (Objects.nonNull(employee)) {
			employeeRepository.delete(employee);
		}
	}
	
	/**
	 * To delete employee with their slack id from database.
	 * 
	 * @param slackId 
	 */  
	public void deleteBySlackId(String slackId) {
		Employee employee = getBySlackId(slackId);
		if (Objects.nonNull(employee)) {
			employeeRepository.delete(employee);
		}
	}
	
	/**
	 * @param id
	 * @return Employee
	 */
	public Employee getById(String id) {
		if (StringUtils.isBlank(id.trim())) {
			return null;
		}
		return employeeRepository.findById(id);
	}
	
	/**
	 * @param location
	 * @return List<Employee>
	 */
	public List<Employee> getByLocation(String location) {
		if (StringUtils.isBlank(location)) {
			return null;
		}
		return employeeRepository.findByLocation(location);
	} 

	/**
	 * @param slackId
	 * @return Employee
	 */
	public Employee getBySlackId(String slackId) {
		if (StringUtils.isBlank(slackId)) {
			return null;
		}
		return employeeRepository.findByslackId(slackId);
	}
	
	/**
	 * To get employees of all location.
	 * 
	 * @return Map. 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */  
	private Map<Set, String> getSlackEmployeesWithLocation() throws ClientProtocolException, IOException {
		List<Location> locationList = locationService.get();
		Map<Set, String> employees = new HashMap<>();
		for (Location location:locationList) {
			Set<String> channel= slackService.getEmployeesOfChannel(location.getGroupId());
			if (!CollectionUtils.isEmpty(channel)) {
				employees.put(channel, location.getLocation());
			}
		}
		return employees;
	}

	/**
	 * To add location of employee.
	 * 
	 * @return Employee.
	 */ 
	private Employee updateLocation(SlackEmployee slackEmployee, Map<Set, String> location) {
		Employee employee = slackEmployeeConverter.convert(slackEmployee);
 		Optional<String> value = location.entrySet().stream()
 				.filter(entry -> entry.getKey()
 				.contains(slackEmployee.getSlackId()))
 				.map(Map.Entry::getValue)
 				.findFirst();
	
 		employee.setLocation(value.map(String::toString).orElse(null));
 		return employee;
	}

	
	/** 
	 * To get list of employee slack id from database.
	 * 
	 * @return 
	 */
	private List<String> getDbEmployee() {
		List<Employee> dbEmployees = findAllWithSlackId();
		return dbEmployees.stream()
			   .filter(Objects::nonNull)
			   .map(employee -> employee.getSlackId())
			   .collect(Collectors.toList());
	}

	/**
	 * This method is used to get list of employee slack id.
	 * 
	 * @return List of employees.
	 */  
	private List<Employee> findAllWithSlackId() {
		return employeeRepository.findAll(new Sort(Sort.Direction.ASC, EMPLOYEE_FIELDS.ID));
	}

	private long getLatestSlackUpdatedTime() {
		Employee employee = employeeRepository.findOne(new Sort(Sort.Direction.DESC, EMPLOYEE_FIELDS.SLACK_UPDATEDTIME));
		if (Objects.nonNull(employee)) {
			return employee.getSlackUt();
		}
		return 0; 
	}
}