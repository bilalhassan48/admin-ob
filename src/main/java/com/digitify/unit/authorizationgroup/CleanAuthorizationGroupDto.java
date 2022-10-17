package com.digitify.unit.authorizationgroup;

import com.digitify.unit.user.AppUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CleanAuthorizationGroupDto {
    @NotNull
    @JsonProperty("uuid")
    private String uuid;


    @JsonProperty("active")
    private Boolean isActive;

    private String name;
    private String description;
    private Set<AppUserDto> users;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationGroupDto that = (AuthorizationGroupDto) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}