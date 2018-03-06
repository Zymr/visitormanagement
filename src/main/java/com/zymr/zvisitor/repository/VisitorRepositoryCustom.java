package com.zymr.zvisitor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dto.VisitorQueryDTO;

public interface VisitorRepositoryCustom {

	Page<Visitor> get(VisitorQueryDTO visitorQueryDTO, Pageable pageable);

}
