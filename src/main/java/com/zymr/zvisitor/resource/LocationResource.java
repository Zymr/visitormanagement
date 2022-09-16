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
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

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
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.service.LocationService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;
@Slf4j
@RestController
public class LocationResource {


	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationConverter locationConverter;

	@RequestMapping(value = Constants.LOCATION_URL, method = RequestMethod.GET)
	@Operation(summary = "Fetch locations")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Fetch locations",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> get() {
	  ResponseEntity<Map<String, Object>> result = ResponseEntity.notFound().build();
		try {
			Collection<LocationDTO> locationDTO = locationConverter.convertToDTO(locationService.get());
			if (CollectionUtils.isNotEmpty(locationDTO)) {
				ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.LOCATION.toLowerCase(), locationDTO);
				result = ResponseEntity.ok(responseDTO.getResponse());
			}
		} catch(Exception e) {
			log.error("Exception while fetching location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
	
	@RequestMapping(value = Constants.AUTH_LOCATION_URL, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "add location")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "add location",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> addLocation(@RequestBody @Valid LocationDTO locationDTO) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Location location = locationConverter.convert(locationDTO);
			locationService.save(location);
			result = ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_ADDED_SUCCESSFLLY).getResponse());
		} catch (DuplicateKeyException e) {
			log.error("Location already exists.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_EXISTS).getResponse());
		} catch (Exception e) {
			log.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
	
	@RequestMapping(value = Constants.LOCATION_UPDATE_DELETE_URL, method = RequestMethod.PUT)
	@Operation(summary = "update location")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "update location",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> updateLocation(@RequestBody @Valid LocationDTO locationDTO, @PathVariable @NotBlank String locId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Location location = locationConverter.convert(locationDTO);
			locationService.update(locId, location);
			result = ResponseEntity.ok().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_CONFIGURATION_CONFIGURATION_UPDATED).getResponse());
		} catch (DuplicateKeyException e) {
			log.error("Location already exists.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_EXISTS).getResponse());
		} catch (NoDataFoundException e) {
			log.error("Exception while updating location.", e);
		} catch (Exception e) {
			log.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.LOCATION_UPDATE_DELETE_URL, method = RequestMethod.DELETE)
	@Operation(summary = "delete location")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "delete location",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> deleteLocation(@PathVariable("locId") @NotBlank String id) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
				locationService.delete(id);
				result = ResponseEntity.ok().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_DELETED_SUCCESSFULLY).getResponse());
		} catch (NoDataFoundException e) {
			log.error("Exception while deleting location.", e);
			result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.LOCATION_NOT_FOUND).getResponse());
		} catch (Exception e) {
			log.error("Exception while deleting location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}


}
