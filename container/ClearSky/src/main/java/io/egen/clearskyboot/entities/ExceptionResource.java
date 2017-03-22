package io.egen.clearskyboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionResource {
	
	private int code;
    private String message;
    private String detailedMessage;
    
    public ExceptionResource() { }
    
    public ExceptionResource(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ExceptionResource(int code, String message, String detailedMessage) {
        this.code = code;
        this.message = message;
        this.detailedMessage = detailedMessage;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}
	
}
