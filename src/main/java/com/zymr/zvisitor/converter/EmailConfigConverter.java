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
	public EmailConfigurationDTO convertToDTO(Email s) {
		if (Objects.isNull(s)) {
			return null;
		} 
		return new EmailConfigurationDTO(s.getHost(), s.getPort(), 
				s.getPassword(), s.getPassword(), s.getFrom());
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
