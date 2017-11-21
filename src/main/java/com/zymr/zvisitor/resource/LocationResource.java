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
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zymr.zvisitor.converter.LocationConverter;
import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.dto.LocationDTO;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.service.LocationService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(Constants.LOCATION_URL)
public class LocationResource extends BaseResource {

	private static final Logger logger = LoggerFactory.getLogger(LocationResource.class);

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationConverter locationConverter;

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Fetch locations", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> get() {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.notFound().build();
		try {
			Collection<LocationDTO> locationDTO = locationConverter.convertToDTO(locationService.get());
			if (CollectionUtils.isNotEmpty(locationDTO)) {
				ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.location.toString(), locationDTO);
				result = ResponseEntity.ok(responseDTO.getResponse());
			}
		} catch(Exception e) {
			logger.error("Exception while fetching location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "add location", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid LocationDTO locationDTO) {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Location location = locationConverter.convert(locationDTO);
			locationService.save(location);
			result =  ResponseEntity.status(HttpStatus.CREATED).build();
		} catch(DuplicateKeyException e) {
			logger.error("Location already exists.", e);
		} catch(Exception e) {
			logger.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/{locId}", method = RequestMethod.PUT)
	@ApiOperation(value = "update location", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> update(@RequestBody @Valid LocationDTO locationDTO, @PathVariable String locId) {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			if (StringUtils.isNotBlank(locId)) {
				Location location = locationConverter.convert(locationDTO);
				locationService.update(locId, location);
				result = ResponseEntity.ok().build();
			}
		} catch(Exception e) {
			logger.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value ="{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") @Valid String id) {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			if (StringUtils.isNotBlank(id)) {
				locationService.delete(id);
				result = ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			logger.error("Exception while deleting location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
}
