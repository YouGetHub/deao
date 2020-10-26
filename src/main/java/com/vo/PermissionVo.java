package com.vo;

import com.domain.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PermissionVo extends Permission {
	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;
	
}
