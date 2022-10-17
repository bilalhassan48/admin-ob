package com.digitify.unit.authorizationgroup;

import com.digitify.unit.role.RoleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class AuthorizationGroupDto {

    @JsonProperty("uuid")
    private String uuid;
    private Long id;

    @JsonProperty("active")
    private Boolean isActive;

    private String name;
    private String description;
    /*private Set<AppUserDto> users;*/
    private List<RoleDto> roles;


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