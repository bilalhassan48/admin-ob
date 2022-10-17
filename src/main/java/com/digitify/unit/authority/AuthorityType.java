package com.digitify.unit.authority;

import com.digitify.framework.annotation.Matchable;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.handler.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthorityType implements Matchable {
    INSTANCE("INSTANCE", "instance"),
    ;

    String code;
    String value;

    AuthorityType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static AuthorityType getType(String code) {
        return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    public static AuthorityType getType(String code, ErrorCode errorCode, String errorMessage) {
        return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst()
                .orElseThrow(() -> new ApplicationException(errorCode, errorMessage));
    }

    public static boolean isValid(String code) {
        return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
    }

    public static List<AuthorityType> getAllTypes() {
        return stream(values()).collect(toList());
    }

    @Override
    public String toMatchAbleString() {
        return this.code;
    }

    @Override
    public String toString() {
        return code;
    }
}
