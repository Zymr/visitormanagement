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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.repository.LocationRepository;
import com.zymr.zvisitor.util.Constants;

@Service
public class LocationService {
	protected static final Logger logger = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	protected LocationRepository locationRepository;

	@Autowired
	protected ConfigurationService configurationService;

	public void syncLocation() {
		if (locationRepository.count() == 0) {
			List<Location> location = new ArrayList<>();
			Map<String, Map<String, String>> locations = configurationService.getLocations();
			for (Entry<String, Map<String, String>> entry : locations.entrySet()) {
				for (Entry<String, String> value : entry.getValue().entrySet()) {
					location.add(new Location(entry.getKey(), value.getKey(), value.getValue()));
				}
			}
			locationRepository.insert(location);
			logger.info("Syncing of location is done.");
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
	 * @throws DuplicateKeyException
	 */
	public void save(Location location) throws DuplicateKeyException {
		locationRepository.save(location);		
	}

	/**
	 * @param id
	 * @return Location
	 */
	public Location getById(String id) {
		if (StringUtils.isBlank(id.trim())) {
			return null;
		}
		return locationRepository.findById(id);
	}

	/**
	 * @param id
	 */
	public void delete(String id) {
		Location location = getById(id);
		if (Objects.nonNull(location)) {
			locationRepository.delete(location);
		}
	}
}
