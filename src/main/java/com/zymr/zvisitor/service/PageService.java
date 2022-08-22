package com.zymr.zvisitor.service;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dto.PageDetails;

@Service
public class PageService {

	public PageDetails fillPageDetails(Page<?> page) {
		if (Objects.nonNull(page)) {
			PageDetails pageDetails = new PageDetails();
			pageDetails.setTotalCount(page.getTotalElements());
			pageDetails.setTotalPage(page.getTotalPages());
			pageDetails.setCurrentPage(page.getNumber() + 1);
			pageDetails.setCurrentCount(page.getNumberOfElements());
			pageDetails.setPageData(page.getContent().stream().collect(Collectors.toList()));
			return pageDetails;
		}
		return null;
	}
}
