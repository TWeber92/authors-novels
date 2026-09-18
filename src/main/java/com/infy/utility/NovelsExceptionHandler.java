package com.infy.utility;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.dto.ErrorInfo;

// needs to handle exceptions coming out of our APIs
@RestControllerAdvice
public class NovelsExceptionHandler {
	
//	@ExceptionHandler(ConstraintViolationException.class)
//	public ResponseEntity<ErrorInfo> pathVarExHandler(ConstraintViolationException e) {
//		ErrorInfo error = new ErrorInfo();
//		error.setErrorCode(400);
//		error.setErrorMessage(e.getConstraintViolations().stream()
//					       .map(er->er.getMessage())
//					       .collect(Collectors.joining(" , ")));
//		return new ResponseEntity<ErrorInfo>(error,HttpStatus.BAD_REQUEST);
//	}
//	
	
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ErrorInfo> bodyExceptionHandler(MethodArgumentNotValidException e) {
//		ErrorInfo error = new ErrorInfo();
//		error.setErrorCode(400);
//		error.setErrorMessage(e.getBindingResult()
//					.getAllErrors().stream().map(er-> er.getDefaultMessage())
//					.collect(Collectors.joining(" , ")));
//		return new ResponseEntity<ErrorInfo>(error,HttpStatus.BAD_REQUEST);
//	}
	
	
	
	@ExceptionHandler // What all types of exceptions is this method gonna handle
	public ResponseEntity<ErrorInfo> handleEx(Exception exception){
		System.out.println(exception);
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(exception.getMessage());
		return new ResponseEntity<ErrorInfo>(errorInfo,HttpStatus.BAD_REQUEST);
	}
	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ErrorInfo> general(Exception e){
//		e.printStackTrace();
//		ErrorInfo err = new ErrorInfo();
//		err.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		err.setErrorMessage("Something failed; Check Console of Eclipse");
//		return new ResponseEntity<ErrorInfo>(err,HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class,ConstraintViolationException.class})
	// MethodArg...Exception => happens when REQUESTBODY validation fails
	// ConstraintV...Exception => happens when PATHVARIABLE validation fails 
	public ResponseEntity<ErrorInfo> exHandle(Exception e){
		e.printStackTrace();
		ErrorInfo err = new ErrorInfo();
		err.setErrorCode(HttpStatus.BAD_REQUEST.value());
		String errorMessage="";

		if (e instanceof MethodArgumentNotValidException) {
			errorMessage = ((MethodArgumentNotValidException) e).getBindingResult()
					.getAllErrors().stream().map(er-> er.getDefaultMessage())
					.collect(Collectors.joining(" , "));
		}
		
		if (e instanceof ConstraintViolationException) {
			errorMessage = ((ConstraintViolationException) e).getConstraintViolations().stream()
					       .map(er->er.getMessage())
					       .collect(Collectors.joining(" , "));
		}
		
		err.setErrorMessage(errorMessage);
		
		return new ResponseEntity<ErrorInfo>(err,HttpStatus.BAD_REQUEST);
		
	}

}
