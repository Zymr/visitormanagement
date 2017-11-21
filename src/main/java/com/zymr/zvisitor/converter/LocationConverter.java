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

import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.dto.LocationDTO;

@Service
public class LocationConverter implements Converter<Location, LocationDTO> {

	@Override
	public LocationDTO convertToDTO(Location location) {
		if (Objects.isNull(location)) {
			return null;
		}
		return new LocationDTO(location.getId(), location.getLocation(), location.getLocationName(),
				location.getGroupId());
	}

	@Override
	public Collection<LocationDTO> convertToDTO(Collection<Location> locations) {
		if (CollectionUtils.isEmpty(locations)) {
			return null;
		}
		return locations.stream().filter(Objects::nonNull).map(loc -> convertToDTO(loc)).
				collect(Collectors.toList());
	}

	@Override
	public Location convert(LocationDTO locationDTO) {
		if (Objects.isNull(locationDTO)) {
			return null;
		}
		return new Location(locationDTO.getLocation(), locationDTO.getGroupName(),
				locationDTO.getLocationName());
	}

	@Override
	public Collection<Location> convert(Collection<LocationDTO> d) {
	  throw new NotImplementedException(LocationConverter.class);
	}
}
