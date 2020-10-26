package com.vo;

import com.domain.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LogVo extends Log {

	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;

	// 接收多个ID
	private Integer[] ids;
	
	// 查询日志时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
