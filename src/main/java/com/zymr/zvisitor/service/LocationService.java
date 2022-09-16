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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.repository.LocationRepository;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.service.config.SlackChannelConfig;
import com.zymr.zvisitor.util.Constants;

@Slf4j
@Service
public class LocationService {

	@Autowired
	protected LocationRepository locationRepository;

	@Autowired
	protected AppProperties appProperties;
	
	@Autowired
	protected EmployeeService employeeService;

	public void syncLocation() {
		if (locationRepository.count() == 0) {
			List<Location> location = new ArrayList<>();
			List<SlackChannelConfig> orgConfigs = appProperties.getOrg().getLocations();
			for (SlackChannelConfig slackConfig : orgConfigs) {
				location.add(new Location(slackConfig.getAbbr(), slackConfig.getSlackid(), slackConfig.getName()));
			}
			locationRepository.insert(location);
			log.info("Syncing of location is done.");
		} 
	}
	/**
	 * This method is used to update location.
	 * 
	 * @param String location id
	 * @param Location 
	 * @throws NoDataFoundException 
	 */
	public void update(String locId, Location location) throws NoDataFoundException{
		Location dbLocation = getById(locId);
		if (Objects.isNull(dbLocation)) {
			throw new NoDataFoundException(Constants.NO_DATA_FOUND);
		} 
		dbLocation.setGroupId(location.getGroupId());
		dbLocation.setLocation(location.getLocation());
		dbLocation.setLocationName(location.getLocationName());
		save(dbLocation);
	}

	/**
	 * @return List<Location>
	 */
	public List<Location> get() {
		return locationRepository.findAll();		
	}

	/**
	 * @param locationId  
	 */
	public Location getLocationName(String locationId) {
		return locationRepository.findByLocation(locationId);
	}

	/**
	 * @param location
	 * @throws Exception 
	 */
	public void save(Location location) {
		locationRepository.save(location);		
		try {
			employeeService.upsertEmployeeFromSlack();
		} catch (Exception e) {
			log.error("Exception while saving location",e);
		}
	}

	/**
	 * @param id
	 * @return Location
	 */
	public Location getById(String id) {
		if (StringUtils.isBlank(id.trim())) {
			return null;
		}
		return locationRepository.findById(id).get();
	}

	/**
	 * @param id
	 * @throws NoDataFoundException 
	 * @throws InvalidDataException 
	 */
	public void delete(String id) throws NoDataFoundException, InvalidDataException {
		if (StringUtils.isBlank(id)) {
			throw new InvalidDataException(Constants.INVALID_PARAM);
		}
		long count = locationRepository.countById(id);
		if (count <= 0) {
			throw new NoDataFoundException(Constants.NO_DATA_FOUND);
		}
		locationRepository.deleteById(id);
	}
}
