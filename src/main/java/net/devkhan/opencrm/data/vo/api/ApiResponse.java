package net.devkhan.opencrm.data.vo.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 8013238242672391530L;
	private int code;
	private T data;
	private String error;
	private String description;

	private ApiResponse(int code, T data, String error, String description) {
		this.code = code;
		this.data = data;
		this.error = error;
		this.description = description;
	}

	public static ApiResponse ok() {
		return new ApiResponse<>(200, null, null, null);
	}

	public static ApiResponse ok(int code) {
		return new ApiResponse<>(code, null, null, null);
	}

	public static <T extends Serializable> ApiResponse ok(T data) {
		return new ApiResponse<>(200, data, null, null);
	}

	public static <T extends Serializable> ApiResponse ok(int code, T data) {
		return new ApiResponse<>(code, data, null, null);
	}

	public static ApiResponse bad() {
		return new ApiResponse<>(600, null, null, null);
	}
	public static ApiResponse bad(int code) {
		return new ApiResponse<>(code, null, null, null);
	}

	public static <T extends Serializable> ApiResponse bad(T data) {
		return new ApiResponse<>(600, data, null, null);
	}
	public static ApiResponse bad(String error, String description) {
		return new ApiResponse<>(600, null, error, description);
	}
	public static ApiResponse bad(int code, String error, String description) {
		return new ApiResponse<>(code, null, error, description);
	}
	public static <T extends Serializable> ApiResponse bad(int code, T data, String error, String description) {
		return new ApiResponse<>(code, data, error, description);
	}
}
