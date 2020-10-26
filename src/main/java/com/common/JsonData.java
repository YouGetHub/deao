package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工具类
 */
@Data
public class JsonData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer code =0; // 状态码
	private Object data; // 数据
	private String msg;// 描述
	private Long count=0L;

	public JsonData(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public JsonData(Long count, Object data) {
		super();
		this.count = count;
		this.data = data;
	}

	public JsonData(Object data) {
		super();
		this.data = data;
	}

	public JsonData(String msg) {
		this.msg = msg;
	}
}
