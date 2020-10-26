package com.vo;

import com.domain.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ProviderVo extends Provider {

	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;

	// 接收多个ID
	private Integer[] ids;

}
