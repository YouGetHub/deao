package com.vo;

import com.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleVo extends Role {

	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;

}
