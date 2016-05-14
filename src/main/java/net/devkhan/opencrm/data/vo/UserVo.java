package net.devkhan.opencrm.data.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true, exclude = "password")
@EqualsAndHashCode(callSuper = true)
public class UserVo extends BaseVo {
	private Long id;
	private String username;
	private String password;
	private String name;
	private String description;
	private String groupCd;
	private String roles;
	private Boolean locked;
}
