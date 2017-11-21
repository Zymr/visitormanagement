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

public interface Converter<S, D> {
	
	D convertToDTO(S s);
	
	Collection<D> convertToDTO(Collection<S> s);
	
	S convert(D d);
	
	Collection<S> convert(Collection<D> d);
}
