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

import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dto.EmailConfigurationDTO;

@Service
public class EmailConfigConverter implements Converter<Email, EmailConfigurationDTO> {

	@Override
	public EmailConfigurationDTO convertToDTO(Email email) {
		if (Objects.isNull(email)) {
			return null;
		} 
		return new EmailConfigurationDTO(email.getHost(), email.getPort(), 
				email.getUsername(), email.getPassword(), email.getFrom());
	}

	@Override
	public Collection<EmailConfigurationDTO> convertToDTO(Collection<Email> s) {
		throw new NotImplementedException(EmailConfigConverter.class);
	}

	@Override
	public Email convert(EmailConfigurationDTO mailConfigurationDTO) {
		if (Objects.isNull(mailConfigurationDTO)) {
			return null;
		}
		return new Email(mailConfigurationDTO.getHost(), mailConfigurationDTO.getPort(), 
				mailConfigurationDTO.getUserName(), mailConfigurationDTO.getPassword(),
				mailConfigurationDTO.getFrom());
	}

	@Override
	public Collection<Email> convert(Collection<EmailConfigurationDTO> d) {
		throw new NotImplementedException(EmailConfigConverter.class);
	}
}
