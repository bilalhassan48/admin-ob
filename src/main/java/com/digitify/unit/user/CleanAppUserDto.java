package com.digitify.unit.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CleanAppUserDto {
    @NotNull
    @JsonProperty("uuid")
    private String uuid;


    @JsonProperty("active")
    private Boolean isActive;

    @NotNull
    private String username;
    private AppUserStatus status;
    private AppUserType userType;
    private String countryCode;
    @Pattern(regexp = "^[0-9]*$", message = "Invalid mobile number")
    private String mobileNo;
    private String firstName;
    private String lastName;
    @NotNull
    private String email;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUserDto that = (AppUserDto) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}