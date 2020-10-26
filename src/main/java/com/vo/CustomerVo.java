package com.vo;

import com.domain.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CustomerVo extends Customer {

	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;

	// 接收多个ID
	private Integer[] ids;

}
