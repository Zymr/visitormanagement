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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Origin;
import com.zymr.zvisitor.dto.VisitorOriginDTO;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.util.Util;

@Service
public class OriginConverter implements Converter<Origin, VisitorOriginDTO> {

	@Autowired
	private ConfigurationService configurationService;
	
	@Override
	public VisitorOriginDTO convertToDTO(Origin origin) {
		if (Objects.isNull(origin)) {
			return null;
		}
		return new VisitorOriginDTO(origin.getId(), origin.getIndex(), 
				origin.getCategory(), Util.buildURL(configurationService.getBaseUrl(), origin.getImagePath()));
	}

	@Override
	public Collection<VisitorOriginDTO> convertToDTO(Collection<Origin> origins) {
		if (CollectionUtils.isEmpty(origins)) {
			return null;
		}
		return origins.stream().filter(Objects::nonNull).map(origin -> convertToDTO(origin))
				.collect(Collectors.toList());
	}

	@Override
	public Origin convert(VisitorOriginDTO visitorOriginDTO) {
		if (Objects.isNull(visitorOriginDTO)) {
			return null;
		}
		return new Origin(visitorOriginDTO.getIndex(), visitorOriginDTO.getCategory(),
				visitorOriginDTO.getImagePath()); 
	}

	@Override
	public Collection<Origin> convert(Collection<VisitorOriginDTO> d) {
	  throw new NotImplementedException(OriginConverter.class);
	}
}
