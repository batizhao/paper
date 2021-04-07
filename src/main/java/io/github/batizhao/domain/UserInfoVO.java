package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import io.github.batizhao.domain.User;

import java.io.Serializable;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-05-09
 **/
@Data
@Accessors(chain = true)
@ApiModel(description = "用户信息")
public class UserInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户基本信息")
	private User user;

	/**
	 * @mock @pick(["common","admin"])
	 */
	@ApiModelProperty(value = "权限清单")
	private List<String> permissions;

	/**
	 * @mock @pick(["ADMIN","MEMBER","GUEST"])
	 */
	@ApiModelProperty(value = "角色清单 ")
	private List<String> roles;
}
