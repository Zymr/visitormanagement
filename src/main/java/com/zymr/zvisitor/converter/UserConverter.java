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
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Users;
import com.zymr.zvisitor.dto.UsersDTO;

@Service
public class UserConverter implements Converter<Users, UsersDTO> {

	@Override
	public UsersDTO convertToDTO(Users user) {
		if (Objects.isNull(user)) {
			return null;
		}
		return new UsersDTO(user.getEmail() , user.getPassword());
	}

	@Override
	public Collection<UsersDTO> convertToDTO(Collection<Users> users) {
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		return users.stream().filter(Objects::nonNull)
				.map(user -> convertToDTO(user))
				.collect(Collectors.toList());
	}

	@Override
	public Users convert(UsersDTO userDTO) {
		if (Objects.isNull(userDTO)) {
			return null;
		}
		return new Users(userDTO.getEmail(), userDTO.getPassword());
	}

	@Override
	public Collection<Users> convert(Collection<UsersDTO> usersDTO) {
		if (CollectionUtils.isEmpty(usersDTO)) {
			return null;
		}
		return usersDTO.stream().filter(Objects::nonNull)
				.map(userDTO -> convert(userDTO))
				.collect(Collectors.toList());
	}
}
