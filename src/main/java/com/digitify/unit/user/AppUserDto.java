package com.digitify.unit.user;

import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class AppUserDto {

    @JsonProperty("uuid")
    private String uuid;
    private Long id;

    @JsonProperty("active")
    private Boolean isActive;

    @NotNull
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private AppUserStatus status;
    private AppUserType userType;
    private String countryCode;
    @Pattern(regexp = "^[0-9]*$", message = "Invalid mobile number")
    private String mobileNo;
    private String firstName;
    private String lastName;
    @NotNull
    private String email;

    private List<AuthorizationGroupDto> authorizationGroupDtos;

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