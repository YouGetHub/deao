package com.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {
	
	
	/**
	 * 获取requset
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) 
				RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}
	
	/**
	 * 获取session
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

}
