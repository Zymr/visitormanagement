package com.zymr.zvisitor.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.util.Constants;

@ControllerAdvice
public class RestException{

	/**
	 * @param methodArgumentNotValidException
	 * @return ResponseDTO
     */	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidInput(final MethodArgumentNotValidException methodArgumentNotValidException) {
		BindingResult result = methodArgumentNotValidException.getBindingResult();
		String errorMessage = result.getFieldErrors().stream()
													 .map(msg -> msg.getDefaultMessage())
													 .collect(Collectors.joining(", "));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, errorMessage).getResponse());
	}

	/**
	 * @param httpMessageNotReadableException
	 * @return ResponseDTO
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(final HttpMessageNotReadableException httpMessageNotReadableException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.INVALID_CONFIGURATION).getResponse());
	}


	/**
	 * @param methodArgumentTypeMismatchException
	 * @return ResponseDTO
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTypeMismatch(final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.INVALID_CONFIGURATION).getResponse());
	}
}
