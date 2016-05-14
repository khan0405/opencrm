package net.devkhan.opencrm.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountDto implements Serializable {
	private static final long serialVersionUID = 1539313509276967194L;
	private String username;
	private String password;
	private String registrationId;
}
