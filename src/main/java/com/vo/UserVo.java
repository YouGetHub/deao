package com.vo;

import com.domain.Permission;
import com.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class UserVo extends User {
	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;
}
