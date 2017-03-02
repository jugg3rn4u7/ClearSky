package io.egen.clearsky.constants;

public final class HTTPStatus {
	
	/**
	 * HTTP SUCCESS
	 */
	public static final int SUCCESS_OK_CODE = 200;
	public static final String SUCCESS_OK_MSG = "OK";
	
	public static final int SUCCESS_CREATED_CODE = 201;
	public static final String SUCCESS_CREATED_MSG = "CREATED";
	
	public static final int SUCCESS_NO_CONTENT_CODE = 204;
	public static final String SUCCESS_NO_CONTENT_MSG = "NO CONTENT";
	
	/**
	 * HTTP REDIRECTION
	 */
	public static final int REDIRECTION_MOVED_PERMANENTLY_CODE = 301;
	public static final String REDIRECTION_MOVED_PERMANENTLY_MSG = "MOVED PERMANENTLY";
	
	/**
	 * HTTP CLIENT ERROR
	 */
	public static final int CLIENT_ERR_BAD_REQUEST_CODE = 400;
	public static final String CLIENT_ERR_BAD_REQUEST_MSG = "BAD REQUEST";
	
	public static final int CLIENT_ERR_UNAUTHORIZED_CODE = 401;
	public static final String CLIENT_ERR_UNAUTHORIZED_MSG = "UNAUTHORIZED";
	
	public static final int CLIENT_ERR_FORBIDDEN_CODE = 403;
	public static final String CLIENT_ERR_FORBIDDEN_MSG = "FORBIDDEN";
	
	public static final int CLIENT_ERR_NOT_FOUND_CODE = 404;
	public static final String CLIENT_ERR_NOT_FOUND_MSG = "NOT FOUND";
	
	/**
	 * HTTP SERVER ERROR
	 */
	public static final int SERVER_ERR_INTERNAL_CODE = 500;
	public static final String SERVER_ERR_INTERNAL_MSG = "INTERNAL SERVER ERROR";
}