/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.exception;

public class NoDataFoundException extends ZException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -770438087930214860L;

	public NoDataFoundException(String msg) {
		super(msg);
	}
	public NoDataFoundException(Throwable t) {
		super(t);
	}
}