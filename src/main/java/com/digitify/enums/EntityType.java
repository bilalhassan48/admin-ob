package com.digitify.enums;

import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Usman
 * @created 8/29/2022 - 10:58 AM
 * @project poc
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityType {
	ADMINUSER("0001", "Admin User"),
	USERDETAIL("0002", "User Detail"),
	APPUSER("0003", "App User"),
	AUTHORIZATIONGROUP("0004", "Authorization Group"),
	ROLE("0005", "Role"),
	AUTHORITY("0006", "Authority"),
	;


	String code;
	String value;

	EntityType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static EntityType getType(String code) {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static EntityType getType(String code, ErrorCode errorCode, String errorMessage) throws ApplicationUncheckException {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElseThrow(() -> new ApplicationUncheckException(errorCode
			,errorMessage));
	}

	public static boolean isValid(String code) {
		return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
	}

	public static List<EntityType> getAllTypes() {
		return stream(values()).collect(toList());
	}

	@Override
	public String toString() {
		return code;
	}
}
