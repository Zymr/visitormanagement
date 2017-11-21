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

public class InvalidDataException extends ZException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1793964878930313382L;

	public InvalidDataException(String msg) {
		super(msg);
	}
	public InvalidDataException(Throwable t) {
		super(t);
	}
}