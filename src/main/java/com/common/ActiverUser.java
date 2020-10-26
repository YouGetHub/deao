package com.common;

import java.util.List;
import com.domain.User;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author 温和的洛瑞
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

	private User user;

	private List<String> roles;

	private List<String> permissions;
}
