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

public abstract class ZException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3507826260522670840L;

	private String message = null;

	public ZException() {
		super();
	}

	public ZException(String message) {
		super(message);
		this.message = message;
	}

	public ZException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}