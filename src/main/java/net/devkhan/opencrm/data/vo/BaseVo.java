package net.devkhan.opencrm.data.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseVo implements Serializable {
	private Boolean deleted = false;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private LocalDateTime deletedTime;
}
