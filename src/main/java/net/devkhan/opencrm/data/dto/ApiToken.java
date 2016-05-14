package net.devkhan.opencrm.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ApiToken implements Serializable {
	private static final long serialVersionUID = 6791065268793339778L;
	private String apiKey;
	private LocalDateTime expireDate;
}
