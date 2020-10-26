package com.vo;

import com.domain.Inport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class InportVo extends Inport {

	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;

	// 查询日志时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
